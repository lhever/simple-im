package com.lhever.common.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * 类操作工具类
 * (1)写代码时，注意String类的replace方法和replaceAll方法的区别
 * (2) 内部类生成的class文件有有美元符号"$"
 * @author lihong10
 * @since 1.0.0
 */
public final class ClassUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtils.class);
    private static final String INNER_CLASS_IDENTIFIER = "$";

    /**
     * 获取类加载器
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure", e);
            throw new RuntimeException(e);
        }
        return cls;
    }

    /**
     * 加载类（默认将初始化类）
     */
    public static Class<?> loadClass(String className) {
        return loadClass(className, true);
    }


    public static Set<Class<?>> getClassSet(String packageName, boolean recursive) {
        Set<Class<?>> classSet = (Set<Class<?>>) getClass(new HashSet<>(), packageName, recursive);
        return classSet;
    }

    /**
     * 获取指定包名下的所有类
     */
    public static Collection<Class<?>> getClass(Collection<Class<?>> classSet, String packageName, boolean recursive) {
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath().replaceAll("%20", " ");
                        addClassFromFile(classSet, packagePath, packageName, recursive);
                    } else if (protocol.equals("jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if (jarURLConnection != null) {
                            addClassFromJar(classSet, jarURLConnection, packageName, recursive);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("get class set failure", e);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    private static void addClassFromJar(Collection<Class<?>> classSet, JarURLConnection jarURLConnection, String packageName, boolean recursive) throws IOException {
        JarFile jarFile = jarURLConnection.getJarFile();
        if (jarFile != null) {
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                String jarEntryName = jarEntry.getName();
                if (jarEntryName.endsWith(".class")) {
                    String classFullName = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                    String simpleName = classFullName.substring(classFullName.lastIndexOf(".") + 1);
                    if (isNotInnerClass(simpleName)) {
                        if (recursive) {
                            if (classFullName.startsWith(packageName)) {
                                doAddClass(classSet, classFullName);
                            }
                        } else {
                            if (packageName.equals(classFullName.substring(0, classFullName.lastIndexOf(".")))) {
                                doAddClass(classSet, classFullName);
                            }
                        }
                    }
                }
            }
        }

    }

    private static void addClassFromFile(Collection<Class<?>> classSet, String packagePath, String packageName, boolean recursive) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File file) {
                //文件名不允许包含$符号是为了排除内部类
                return (file.isFile() && file.getName().endsWith(".class") && isNotInnerClass(file.getName())) || file.isDirectory();
            }
        });
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtils.isNotEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet, className);
            } else {
                if (recursive) {
                    String subPackagePath = fileName;
                    if (StringUtils.isNotEmpty(packagePath)) {
                        subPackagePath = packagePath + "/" + fileName;
                    }
                    String subPackageName = fileName;
                    if (StringUtils.isNotEmpty(packageName)) {
                        subPackageName = packageName + "." + fileName;
                    }
                    addClassFromFile(classSet, subPackagePath, subPackageName, recursive);
                }
            }
        }
    }

    private static void doAddClass(Collection<Class<?>> classSet, String className) {
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }

    private static boolean isNotInnerClass(String className) {
        return className.indexOf(INNER_CLASS_IDENTIFIER) == -1;
    }


    private static void test(String packageName, boolean recursive) {
        List<Class<?>> classSet = (List<Class<?>>) getClass(new ArrayList<>(), packageName, recursive);
        Collections.sort(classSet, (c1, c2) -> c1.getName().compareTo(c2.getName()));
        classSet.stream().forEach(
                c -> System.out.println(c.getName())
        );
        Map<String, List<Class<?>>> collect = new ArrayList<>(classSet).stream().collect(Collectors.groupingBy(c -> c.getSimpleName()));
        System.out.println("重名的类如下:");
        collect.forEach((k, v) -> {
            List<String> classNames = v.stream().map(c -> c.getName()).collect(Collectors.toList());
            if (v != null && v.size() > 1) {
                System.out.println(k + " -> " + classNames);
            }
        });

    }


    public static void main(String[] args) {
        test("com.hikvision.eits", true);
        test("com.hikvision.eits.rhm.core.support.concurrent", true);
    }
}
