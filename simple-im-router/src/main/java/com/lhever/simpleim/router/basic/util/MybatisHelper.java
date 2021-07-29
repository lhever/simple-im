package com.lhever.simpleim.router.basic.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import com.lhever.simpleim.router.basic.cfg.DataSourceProp;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.executor.loader.ProxyFactory;
import org.apache.ibatis.io.DefaultVFS;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.*;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.JdbcType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class MybatisHelper {


    public DefaultSqlSessionFactory defaultSqlSessionFactory(DataSourceProp dataSourc) {
        return new DefaultSqlSessionFactory(getCfg(dataSourc));
    }

    public Configuration getCfg(DataSourceProp dataSource) {
        Configuration cfg = new Configuration();

        Properties properties = new Properties();
        properties.put("drive", dataSource.getDriver());
        properties.put("url", dataSource.getUrl());
        properties.put("username", dataSource.getUsername());
        properties.put("password", dataSource.getPassword());
        cfg.setVariables(properties);

        cfg.setVfsImpl(DefaultVFS.class);
        cfg.setLogImpl(Slf4jImpl.class);


        cfg.getTypeAliasRegistry().registerAliases("com.lhever.simpleim.router.pojo");
        cfg.addInterceptor(pageInterceptor());

        ObjectFactory objectFactory = new DefaultObjectFactory();
        ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();
        ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
        cfg.setObjectFactory(objectFactory);
        cfg.setObjectWrapperFactory(objectWrapperFactory);
        cfg.setReflectorFactory(reflectorFactory);


        Properties settings = new Properties();
        settings.put("useColumnLabel", "true");
        settings.put("defaultExecutorType", "REUSE");
        settings.put("useGeneratedKeys", "false");
        settings.put("defaultStatementTimeout", "25000");
        settings.put("localCacheScope", "STATEMENT");
        settings.put("multipleResultSetsEnabled", "true");
        settings.put("lazyLoadingEnabled", "true");
        settings.put("cacheEnabled", "false");

        settingsElement(cfg, settings);

        Environment.Builder builder = new Environment.Builder("default");
        builder.dataSource(dataSource(dataSource));
        JdbcTransactionFactory txf =  new JdbcTransactionFactory();
        builder.transactionFactory(txf);


        cfg.setEnvironment(builder.build());
//        cfg.setDatabaseId("did");

        cfg.getTypeHandlerRegistry().register("com.lhever.simpleim.router");

        cfg.addMappers("com.lhever.simpleim.router.dao");

        return cfg;

    }


    public PageInterceptor pageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        //配置postgresql数据库的方言
        //properties.setProperty("dialect", "postgresql");
        pageInterceptor.setProperties(properties);
        /* PageHelper分页配置结束 */
        return pageInterceptor;
    }


    private void settingsElement(Configuration configuration, Properties props) {
        configuration.setAutoMappingBehavior(AutoMappingBehavior.valueOf(props.getProperty("autoMappingBehavior", "PARTIAL")));
        configuration.setAutoMappingUnknownColumnBehavior(AutoMappingUnknownColumnBehavior.valueOf(props.getProperty("autoMappingUnknownColumnBehavior", "NONE")));
        configuration.setCacheEnabled(booleanValueOf(props.getProperty("cacheEnabled"), true));
        configuration.setProxyFactory((ProxyFactory) createInstance(configuration, props.getProperty("proxyFactory")));
        configuration.setLazyLoadingEnabled(booleanValueOf(props.getProperty("lazyLoadingEnabled"), false));
        configuration.setAggressiveLazyLoading(booleanValueOf(props.getProperty("aggressiveLazyLoading"), false));
        configuration.setMultipleResultSetsEnabled(booleanValueOf(props.getProperty("multipleResultSetsEnabled"), true));
        configuration.setUseColumnLabel(booleanValueOf(props.getProperty("useColumnLabel"), true));
        configuration.setUseGeneratedKeys(booleanValueOf(props.getProperty("useGeneratedKeys"), false));
        configuration.setDefaultExecutorType(ExecutorType.valueOf(props.getProperty("defaultExecutorType", "SIMPLE")));
        configuration.setDefaultStatementTimeout(integerValueOf(props.getProperty("defaultStatementTimeout"), null));
        configuration.setDefaultFetchSize(integerValueOf(props.getProperty("defaultFetchSize"), null));
        configuration.setDefaultResultSetType(resolveResultSetType(props.getProperty("defaultResultSetType")));
        configuration.setMapUnderscoreToCamelCase(booleanValueOf(props.getProperty("mapUnderscoreToCamelCase"), false));
        configuration.setSafeRowBoundsEnabled(booleanValueOf(props.getProperty("safeRowBoundsEnabled"), false));
        configuration.setLocalCacheScope(LocalCacheScope.valueOf(props.getProperty("localCacheScope", "SESSION")));
        configuration.setJdbcTypeForNull(JdbcType.valueOf(props.getProperty("jdbcTypeForNull", "OTHER")));
        configuration.setLazyLoadTriggerMethods(stringSetValueOf(props.getProperty("lazyLoadTriggerMethods"), "equals,clone,hashCode,toString"));
        configuration.setSafeResultHandlerEnabled(booleanValueOf(props.getProperty("safeResultHandlerEnabled"), true));
        configuration.setDefaultScriptingLanguage(resolveClass(configuration, props.getProperty("defaultScriptingLanguage")));
        configuration.setDefaultEnumTypeHandler(resolveClass(configuration, props.getProperty("defaultEnumTypeHandler")));
        configuration.setCallSettersOnNulls(booleanValueOf(props.getProperty("callSettersOnNulls"), false));
        configuration.setUseActualParamName(booleanValueOf(props.getProperty("useActualParamName"), true));
        configuration.setReturnInstanceForEmptyRow(booleanValueOf(props.getProperty("returnInstanceForEmptyRow"), false));
        configuration.setLogPrefix(props.getProperty("logPrefix"));
        configuration.setConfigurationFactory(resolveClass(configuration, props.getProperty("configurationFactory")));
    }

    protected Boolean booleanValueOf(String value, Boolean defaultValue) {
        return value == null ? defaultValue : Boolean.valueOf(value);
    }

    protected Integer integerValueOf(String value, Integer defaultValue) {
        return value == null ? defaultValue : Integer.valueOf(value);
    }

    protected Set<String> stringSetValueOf(String value, String defaultValue) {
        value = value == null ? defaultValue : value;
        return new HashSet<>(Arrays.asList(value.split(",")));
    }

    protected <T> Class<? extends T> resolveClass(Configuration configuration, String alias) {
        if (alias == null) {
            return null;
        }
        try {
            return configuration.getTypeAliasRegistry().resolveAlias(alias);
        } catch (Exception e) {
            throw new BuilderException("Error resolving class. Cause: " + e, e);
        }
    }

    protected ResultSetType resolveResultSetType(String alias) {
        if (alias == null) {
            return null;
        }
        try {
            return ResultSetType.valueOf(alias);
        } catch (IllegalArgumentException e) {
            throw new BuilderException("Error resolving ResultSetType. Cause: " + e, e);
        }
    }

    protected Object createInstance(Configuration cfg, String alias) {
        Class<?> clazz = resolveClass(cfg, alias);
        if (clazz == null) {
            return null;
        }
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new BuilderException("Error creating instance. Cause: " + e, e);
        }
    }

    public DruidDataSource dataSource(DataSourceProp dataSourceProp) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dataSourceProp.getUrl());
        dataSource.setUsername(dataSourceProp.getUsername());
        dataSource.setPassword(dataSourceProp.getPassword());
        dataSource.setDriverClassName(dataSourceProp.getDriver());
        dataSource.setMinIdle(5);
        dataSource.setValidationQuery("select 1");
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setMaxWait(60000);
        dataSource.setInitialSize(5);
        dataSource.setMaxActive(100);
        return dataSource;
    }


}
