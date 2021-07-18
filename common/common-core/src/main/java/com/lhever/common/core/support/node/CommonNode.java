package com.lhever.common.core.support.node;

import com.lhever.common.core.consts.CommonConsts;
import com.lhever.common.core.utils.CollectionUtils;
import com.lhever.common.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class CommonNode<NODE extends CommonNode<NODE, ID>, ID> {
    private final static Logger log = LoggerFactory.getLogger(CommonNode.class);
    private final static String UNKNOWN = "unknown";

    protected ID id;

    protected ID parentId;

    protected Integer sort;

    protected boolean hasAuth = false;

    protected String path;

    protected List<NODE> children = null;

    protected String label;

    protected boolean isLeaf;

    protected boolean isRootNode;

    protected int depth;

    public CommonNode() {
    }

    public CommonNode(boolean init) {
        if (init) {
            init();
        }
    }

    protected void init() {
        id = getId();
        parentId = getParentId();
        sort = getSort();
        path = getPath();
        label = getLabel();
        isLeaf = getIsLeaf();
        isRootNode = getIsRootNode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NODE another = (NODE) o;

        return Objects.equals(id, another.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void add(NODE child) {
        if (child == null) {
            return;
        }

        if (children == null) {
            this.children = new ArrayList<>();
        }

        children.add(child);
    }


    public void add(List<NODE> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return;
        }

        if (children == null) {
            this.children = new ArrayList<>();
        }
        for (NODE node : nodes) {
            if (node == null) {
                continue;
            }
            children.add(node);
        }
    }


    public static <B extends CommonNode> Comparator<B> defaultComparator() {
        Comparator<B> comparator = (n1, n2) -> {
            Integer sort1 = n1.getSort() == null ? 0 : n1.getSort();
            Integer sort2 = n2.getSort() == null ? 0 : n2.getSort();

            return sort1.compareTo(sort2);
        };
        return comparator;

    }

    public static <B extends CommonNode> void sort(List<B> nodes, Comparator<B> comparator) {
        if (nodes == null || nodes.size() <= 1) {
            return;
        }
        //排序最外层文档节点
        Collections.sort(nodes, comparator);
    }



    public void sort(Comparator<NODE> c) {
        sort(children, c);
    }

    public void removeRepeat() {
        if (children == null || children.size() <= 1) {
            return;
        }

        this.children = CollectionUtils.removeRepeat(children);
    }


    public void removeChildren() {
       removeChildren(true);
    }

    public void removeChildren(boolean setNull) {
        if (CollectionUtils.isEmpty(children)) {
            return;
        }
        children.clear();
        if (setNull) {
            this.children = null;
        }
    }

    public static List removeChildren(List<? extends CommonNode> nodes) {
        return removeChildren(nodes, true);
    }

    public static List removeChildren(List<? extends CommonNode> nodes, boolean setNull) {
        if (CollectionUtils.isEmpty(nodes)) {
            return nodes;
        }
        for (CommonNode node : nodes) {
            if (node == null) {
                continue;
            }
            node.removeChildren(setNull);
        }
        return nodes;
    }


    public ID getId() {
        ID id = id();
        if (id == null) {
            throw new IllegalArgumentException("node id cannot be null");
        }
        return id;
    }

    public abstract ID id();

    public void setId(ID id) {
        this.id = id;
    }

    public ID getParentId() {
        return parentId();
    }

    public abstract ID parentId();

    public void setParentId(ID parentId) {
        this.parentId = parentId;
    }

    public abstract Integer getSort();

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public abstract String getPath();

    public void setPath(String path) {
        this.path = path;
    }

    public List<NODE> getChildren() {
        return children;
    }

    public int childrenSize() {
        if (children == null) {
            return 0;
        }
        return children.size();
    }

    public void setChildren(List<NODE> children) {
        this.children = children;
    }

    public boolean getHasAuth() {
        return hasAuth;
    }

    public void setHasAuth(boolean hasAuth) {
        this.hasAuth = hasAuth;
    }

    public abstract String getLabel();

    public void setLabel(String label) {
        this.label = label;
    }

    public abstract boolean getIsLeaf();

    public void setIsLeaf(boolean leaf) {
        this.isLeaf = leaf;
    }

    public abstract boolean getIsRootNode();

    public void setIsRootNode(boolean isRootNode) {
        this.isRootNode = isRootNode;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * 该方法清空指定层级的节点的所有子节点，如果节点的层级等于index,则需要清空该节点的所有子节点
     * 注： 根节点的层级等于0，下一级节点的层级等于1，下下一级节点的层级等于2，以此类推....
     *
     * @param parentLevel 父节点的层级
     * @param index       如果节点的层级等于index,则需要清空该节点的所有子节点
     * @return
     * @author lihong10 2019/3/15 15:43
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/15 15:43
     * @modify by reason:{原因}
     */
    protected void removeChildrenFromDepth(int parentLevel, int index) {

        if (index < 0) {
            return;
        }

        if (getIsRootNode()) {
            if ((parentLevel != -1)) { //根节点调用此方法，要求parentLevel == -1
                throw new IllegalArgumentException(" root node, param parentLevel == -1 required!");
            }
        } else {
            if (parentLevel < 0) { //非根节点调用此方法，要求parentLevel >= 0
                throw new IllegalArgumentException(" sub node, param parentLevel >= 0 required!");
            }
        }

        int level = parentLevel + 1;

        if (CollectionUtils.isEmpty(children)) {
            children = null;
            return;
        }

        if (level >= index) {
            children.clear();
            children = null;
        } else {
            for (NODE node : children) {
                node.removeChildrenFromDepth(level, index);
            }
        }
    }

    /**
     * 该方法清空指定层级的节点的所有子节点，如果节点的层级等于index,则需要清空该节点的所有子节点
     * 注： 根节点的层级等于0，下一级节点的层级等于1，下下一级节点的层级等于2，以此类推....
     *
     * @param index 如果节点的层级等于index,则需要清空该节点的所有子节点
     * @return
     * @author lihong10 2019/3/15 15:43
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/15 15:43
     * @modify by reason:{原因}
     */
    public void removeChildrenFromDepth(int index) {
        removeChildrenFromDepth(-1, index);
    }


    public void collectPathEqDepth(int parentLevel, int index, Collection<String> paths) {
        if (paths == null) {
            throw new IllegalArgumentException("paths cannot be null");
        }

        if (index < 0) {
            return;
        }

        if (getIsRootNode()) {
            if ((parentLevel != -1)) { //根节点调用此方法，要求parentLevel == -1
                throw new IllegalArgumentException(" root node, param parentLevel == -1 required!");
            }
        } else {
            if (parentLevel < 0) { //非根节点调用此方法，要求parentLevel >= 0
                throw new IllegalArgumentException(" sub node, param parentLevel >= 0 required!");
            }
        }

        int level = parentLevel + 1;

        if (CollectionUtils.isEmpty(children)) {
            children = null;
            return;
        }

        if (level == index) {
            paths.add(path);
        } else {
            for (NODE node : children) {
                node.collectPathEqDepth(level, index, paths);
            }
        }
    }


    /**
     * 递归的将所有子节点, 不包括自身, 经函数function换算后得到的值存入Collection并返回
     * @author lihong10 2019/7/11 14:24
     * @param function
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/7/11 14:24
     * @modify by reason:{原因}
     */
    public <T> void collectByFuncExclude(Collection<T> set, Function<NODE, T> function) {
        if (set == null) {
            throw new IllegalArgumentException("idSet cannot be null");
        }

        if (CollectionUtils.isEmpty(children)) {
            return;
        }

        for (NODE node : children) {
            T value = function.apply(node);
            set.add(value);
            node.collectByFuncExclude(set, function);
        }
    }

    /**
     * 递归的将所有子节点, 不包括自身,  经函数function换算后得到的值存入list并返回
     * @author lihong10 2019/7/11 14:24
     * @param function
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/7/11 14:24
     * @modify by reason:{原因}
     */
    public <T> List<T> collectToListByFuncExclude(Function<NODE, T> function) {
        List<T> results = new ArrayList<>();
        collectByFuncExclude(results, function);
        return results;
    }

    /**
     * 递归的将所有子节点, 包括自身,  经函数function换算后得到的值存入list并返回
     * @author lihong10 2019/7/11 14:24
     * @param function
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/7/11 14:24
     * @modify by reason:{原因}
     */
    public <T> List<T> collectToListByFuncInclude(Function<NODE, T> function) {
        List<T> results = new ArrayList<>();
        collectByFuncExclude(results, function);

        T value = function.apply((NODE) this);
        results.add(value);
        return results;
    }



    /**
     * 递归的将所有子节点, 不包括自身,  经函数function换算后得到的值存入set并返回
     * @author lihong10 2019/7/11 14:24
     * @param function
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/7/11 14:24
     * @modify by reason:{原因}
     */
    public <T> Set<T> collectToSetByFuncExclude(Function<NODE, T> function) {
        Set<T> results = new HashSet<>();
        collectByFuncExclude(results, function);
        return results;
    }


    /**
     * 递归的将所有子节点, 包括自身,  经函数function换算后得到的值存入set并返回
     * @author lihong10 2019/7/11 14:24
     * @param function
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/7/11 14:24
     * @modify by reason:{原因}
     */
    public <T> Set<T> collectToSetByFuncInclude(Function<NODE, T> function) {
        Set<T> results = new HashSet<>();
        collectByFuncExclude(results, function);

        //把自身节点也换算后加入
        T value = function.apply((NODE) this);
        results.add(value);
        return results;
    }


    /**
     * 递归得将所有子节点的id存入set并返回
     * @author lihong10 2019/7/11 14:07
     * @param
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/7/11 14:07
     * @modify by reason:{原因}
     */
    public Set<ID> childIdSetExclude() {
        return collectToSetByFuncExclude((NODE n) -> n.getId());
    }

    /**
     * 递归得将所有子节点的id, 包括自身的id存入set并返回
     * @author lihong10 2019/7/11 14:07
     * @param
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/7/11 14:07
     * @modify by reason:{原因}
     */
    public Set<ID> childIdSetInclude() {
        return collectToSetByFuncInclude((NODE n) -> n.getId());
    }

    /**
     * 递归得将所有子节点的id存入list并返回
     * @author lihong10 2019/7/11 14:07
     * @param
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/7/11 14:07
     * @modify by reason:{原因}
     */
    public List<ID> childIdListExclude() {
        return collectToListByFuncExclude((NODE n) -> n.getId());
    }

    /**
     * 递归得将所有子节点的id, 包括自身的id存入list并返回
     * @author lihong10 2019/7/11 14:07
     * @param
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/7/11 14:07
     * @modify by reason:{原因}
     */
    public List<ID> childIdListInclude() {
        return collectToListByFuncInclude((NODE n) -> n.getId());
    }


    /**
     * 计算节点的深度
     *
     * @param parentDepth
     * @return
     * @author lihong10 2019/3/26 18:41
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/26 18:41
     * @modify by reason:{原因}
     */
    protected void doCalculateDepth(int parentDepth) {
        if (getIsRootNode()) {
            if (parentDepth != -1) { //根节点调用此方法，要求parentDepth == -1
                throw new IllegalArgumentException(" root node, param parentDepth == -1 required!");
            }
        } else {
            if (parentDepth < 0) { //非根节点调用此方法，要求parentDepth >= 0
                throw new IllegalArgumentException(" sub node, param parentDepth >= 0 required!");
            }
        }

        depth = parentDepth + 1;

        if (CollectionUtils.isEmpty(children)) {
            return;
        }
        for (NODE node : children) {
            node.doCalculateDepth(depth);
        }
    }

    public void calculateDepth() {
        if (!getIsRootNode()) {
            return;
        }
        doCalculateDepth(-1);
    }

    /**
     * 递归计算绝对路径，只有根节点才能调用此方法
     * path字段为空，可以通过该方法动态计算路径
     *
     * @param parentPath
     * @return
     * @author lihong10 2019/3/26 18:54
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/26 18:54
     * @modify by reason:{原因}
     */
    protected void doCalculatePath(String parentPath) {
        String tempPath = null;
        if (getIsRootNode()) {
            tempPath = CommonConsts.SLASH + id;
        } else {
            tempPath = parentPath + CommonConsts.SLASH + id;
        }

        setPath(tempPath);

        if (CollectionUtils.isEmpty(children)) {
            return;
        }

        for (NODE child : children) {
            child.doCalculatePath(tempPath);
        }
    }

    /**
     * 递归计算绝对路径，只有根节点才能调用此方法
     * @author lihong10 2019/7/11 14:15
     * @param
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/7/11 14:15
     * @modify by reason:{原因}
     */
    public void calculatePath() {
        if (!getIsRootNode()) {
            return;
        }

        doCalculatePath(null);
    }


    /**
     * 递归计算相对路径
     * @author lihong10 2019/7/11 14:11
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/7/11 14:11
     * @modify by reason:{原因}
     */
    protected void calcuRelativePath(String parentPath) {
        String tempPath = null;
        if (StringUtils.isNotBlank(parentPath)) {
            tempPath = parentPath + CommonConsts.SLASH + id;
        } else {
            tempPath = CommonConsts.SLASH + id;
        }

        setPath(tempPath);

        if (CollectionUtils.isEmpty(children)) {
            return;
        }

        for (NODE child : children) {
            child.calcuRelativePath(tempPath);
        }
    }


    public void collectPathEqDepth(int index, Collection<String> paths) {
        collectPathEqDepth(-1, index, paths);

    }

    public Set<String> collectPathEqDepthToSet(int index) {
        Set<String> paths = new HashSet<String>();
        collectPathEqDepth(-1, index, paths);
        return paths;
    }

    public List<String> collectPathEqDepthToList(int index) {
        List<String> paths = new ArrayList<>();
        collectPathEqDepth(-1, index, paths);
        return paths;
    }


    public void setAuth(Collection<ID> auths) {
        if (auths.contains(id)) {
            propagateAuth(true);
            return;//父节点的权限传递到子节点后，setAuth()方法不再需要递归
        } else {
            setHasAuth(false);
        }

        if (CollectionUtils.isEmpty(getChildren())) {
            return;
        }


        for (NODE child : children) {
            child.setAuth(auths);
        }
    }


    public void propagateAuth(boolean parentAuth) {
        setHasAuth(parentAuth);
        if (CollectionUtils.isEmpty(getChildren())) {
            return;
        }

        for (NODE child : children) {
            child.propagateAuth(parentAuth);
        }
    }

    public static <A> Set<String> splitAndCollect(List<A> nodes, Function<A, String> pathFunc) {
        //查找该节点，以及上层路径上的所有节点
        if (nodes == null) {
            return new HashSet<String>(0);
        }

        Set<String> pathIds = new HashSet<String>();
        for (A node : nodes) {
            if (node == null) {
                continue;
            }
            String path = pathFunc.apply(node);
            if (StringUtils.isBlank(path)) {
                continue;
            }
            String[] splitId = path.split(CommonConsts.SLASH);

            for (String id : splitId) {

                if (StringUtils.isNotBlank(id)) {
                    pathIds.add(id);
                }
            }
        }
        return pathIds;
    }


    public static <A extends CommonNode> void distinctAndSort(List<A> nodes, Comparator comparator) {
        if (nodes == null) {
            return;
        }
        for (A node : nodes) {
            if (node == null) {
                continue;
            }
            //菜单去重，有必要吗?
            node.removeRepeat();
            //菜单排序
            node.sort(comparator);
        }
    }


    public static <A, B extends CommonNode> List<B> convertToTree(List<A> datas, Function<A, B> func) {
        List<B> commonNodes = datas.stream().map(item -> func.apply(item)).collect(Collectors.toList());
        List<B> nodes = buildTree(commonNodes);
        return nodes;
    }

    public static <ID, A, B extends CommonNode> List<B> convertToTree(List<A> datas, Function<A, B> func, Map<ID, B> idNodeMap) {
        List<B> commonNodes = datas.stream().map(item -> func.apply(item)).collect(Collectors.toList());
        return buildTree(commonNodes, idNodeMap);
    }

    public static <ID, A, B extends CommonNode> List<B> convertToTree(List<A> datas, List<B> nodes, Function<A, B> func, Map<ID, B> idNodeMap) {
        List<B> commonNodes = datas.stream().map(item -> {
            B n = func.apply(item);
            nodes.add(n);
            return n;
        }).collect(Collectors.toList());
        return buildTree(commonNodes, idNodeMap);
    }


    public static <B extends CommonNode> List<B> buildTree(List<B> nodes) {
        List<B> commonNodes = doBuildTree(nodes);
        return commonNodes;
    }

    public static <ID, B extends CommonNode> List<B> buildTree(List<B> nodes, Map<ID, B> idNodeMap) {
        List<B> commonNodes = doBuildTree(nodes, idNodeMap);
        return commonNodes;
    }

    public static <ID, B extends CommonNode> List<B> doBuildTree(List<B> nodes) {
        ////////////////////////// 拼接树////////////////////////////
        List<B> roots = doBuildTree(nodes, new HashMap<ID, B>());
        return roots;
    }

    public static <ID, B extends CommonNode> List<B> doBuildTree(List<B> nodes, Map<ID, B> idNodeMap) {
        return doBuildTree(nodes, idNodeMap, defaultComparator());
    }

    public static <ID, B extends CommonNode> List<B> doBuildTree(List<B> nodes, Map<ID, B> idNodeMap, Comparator<B> comparator) {
        if (CollectionUtils.isEmpty(nodes)) {
            return new ArrayList<>(0);
        }
        ////////////////////////// 拼接树////////////////////////////

        for (B node : nodes) {
            idNodeMap.put((ID)node.getId(), node);
        }

        //组装菜单树形结构
        List<B> roots = new LinkedList<>();
        for (B node : nodes) {

            if (node.getIsRootNode()) {
                roots.add(node);
                //根节点没有上层节点
                continue;
            }

            B parent = idNodeMap.get(node.getParentId());
            if (parent == null) {
                log.error("节点(label = {}, id = {})丢失父节点", node.getLabel(), node.getId());
                continue;
            }
            //子节点添加到父节点
            parent.add(node);
        }

        //排序和去重
        comparator = (comparator == null) ? defaultComparator() : comparator;
        distinctAndSort(nodes, comparator);

        //计算节点深度
        for (B root : roots) {
            root.doCalculateDepth(-1);
        }

        //排序和去重节点
        sort(roots, comparator);

        return roots;
    }

    /**
     * 移除子路径，保留根路径,
     * 比如:
     * 移除前列表：    [aa/bb/cc/dd, aa/bb, ee/ff/gg, ee, cc, cc]
     * 移除后的列表：  [aa/bb, ee, cc]
     *
     * @param paths
     * @return
     * @author lihong10 2019/3/21 15:01
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/21 15:01
     * @modify by reason:{原因}
     */
    public static List<String> removeChildPath(List<String> paths) {
        if (CollectionUtils.isEmpty(paths)) {
            return paths;
        }

        for (int i = 0; i < paths.size(); i++) {
            for (int j = i + 1; j < paths.size(); j++) {
                String a = paths.get(i);
                String b = paths.get(j);
                if (b.startsWith(a)) {
                    paths.remove(j);
                    j--;
                } else if (a.startsWith(b)) {
                    paths.remove(i);
                    j--;
                }
            }
        }
        return paths;
    }


    /**
     * 筛选路径包含在paths中， 或以paths中的路径开头的文档
     *
     * @param all   文档列表
     * @param paths 字段路径列表
     * @return
     * @author lihong10 2019/3/21 15:44
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/21 15:44
     * @modify by reason:{原因}
     */
    public  static <A extends CommonNode> List<A> getByPathStartwith(List<A> all, Collection<String> paths) {
        if (CollectionUtils.isEmpty(all) || CollectionUtils.isEmpty(paths)) {
            return new ArrayList<>(0);
        }

        List<A> filtered = all
                .stream()
                .filter(startsWithPredicate(node -> node.getPath(), paths))
                .collect(Collectors.toList());

        return filtered;
    }

    public static <A> List<A> getByPathStartwith(List<A> all, Function<A, String> pathFunc, Collection<String> paths) {
        if (CollectionUtils.isEmpty(all) || CollectionUtils.isEmpty(paths)) {
            return new ArrayList<A>(0);
        }
        List<A> filtered = all
                .stream()
                .filter(startsWithPredicate(pathFunc, paths))
                .collect(Collectors.toList());

        return filtered;
    }

    public static List<String> getPathStartwith(List<String> all, Collection<String> paths) {
        return getByPathStartwith(all, Function.identity(), paths);
    }


    public static <C> Predicate<C> startsWithPredicate(Function<C, String> pathFunc, Collection<String> paths) {
        Predicate<C> predicate = item -> {
            if (item == null) {
                return false;
            }

            String p = pathFunc.apply(item);
            if (StringUtils.isBlank(p)) {
                return false;
            }
            for (String path : paths) {
                if (p.startsWith(path)) {
                    return true;
                }
            }

            return false;
        };

        return predicate;
    }


    public static <A> List<A> getValueIn(List<A> all, Function<A, String> func, Collection<String> values) {
        if (CollectionUtils.isEmpty(all) || CollectionUtils.isEmpty(values)) {
            return new ArrayList<>(0);
        }

        List<A> results = new ArrayList();
        for (A obj : all) {
            String value = func.apply(obj);
            if (value != null && values.contains(value)) {
                results.add(obj);
            }
        }
        return results;
    }

    public static <A, B> List<A> getValueIn(List<A> data, Function<A, String> dataFunc, List<B> pathSrc, Function<B, String> pathFunc) {
        Set<String> ids = splitAndCollect(pathSrc, pathFunc);
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>(0);
        }
        return getValueIn(data, dataFunc, ids);
    }

    public static <ID, A extends CommonNode> Map<ID, A> toIdMap(List<A> nodes) {

        if (CollectionUtils.isEmpty(nodes)) {
            return new HashMap<>(0);
        }
        return toMap((A item) -> (ID) item.getId(), nodes);
    }

    public static <A, R> Map<R, A> toMap(Function<A, R> func, List<A> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return new HashMap<>(0);
        }
        Map<R, A> idNodeMap = new HashMap<>();
        for (A node : nodes) {
            if (node == null) {
                continue;
            }
            R key = func.apply(node);
            idNodeMap.put(key, node);
        }
        return idNodeMap;
    }

    private static <A extends CommonNode>  Function<A, String> getLabelFunc() {
        Function<A, String> func = (A node) -> node.getLabel();
        return func;
    }


    public static <ID, A extends CommonNode> String getFullNameById(Map<ID, A> idNodeMap, ID nodeId, String joiner, boolean includeRoot) {
        return getFullNameById(getLabelFunc(), idNodeMap, nodeId, joiner, includeRoot);
    }

    public static <ID, A extends CommonNode> String getFullNameById(List<A> nodes, ID nodeId, String joiner, boolean includeRoot) {
        return getFullNameById(getLabelFunc(), toIdMap(nodes), nodeId, joiner, includeRoot);
    }


    public static <ID, A extends CommonNode> String getFullNameById(Function<A, String> func, Map<ID, A> idNodeMap, ID nodeId, String joiner, boolean includeRoot) {
        assert idNodeMap != null : "<id, node> map is null";
        A targetNode = idNodeMap.get(nodeId);
        if (targetNode == null) {
            return null;
        }

        if (targetNode.getIsRootNode()) {
            if (includeRoot) {
                return func.apply(targetNode);
            } else {
                return null;
            }
        }

        List<String> names = new LinkedList<>();
        while (targetNode != null) {
            String label = func.apply(targetNode);
            label = StringUtils.isBlank(label) ? UNKNOWN : label;

            if (!targetNode.getIsRootNode()) {
                names.add(label);
            } else {
                if (includeRoot) {
                    names.add(label);
                }
            }
            targetNode = idNodeMap.get(targetNode.getParentId());
        }

        Collections.reverse(names);
        return StringUtils.join(names, joiner);
    }

    public static <ID, A extends CommonNode> String getFullNameById(Function<A, String> func, List<A> nodes, ID nodeId, String joiner, boolean includeRoot) {
        return getFullNameById(func, toIdMap(nodes), nodeId, joiner, includeRoot);
    }


    public static <ID, A extends CommonNode> Map<ID, String> getFullNameByIds(Function<A, String> func, Map<ID, A> idNodeMap, List<ID> nodeIds, String joiner, boolean includeRoot) {
        Map<ID, String> idNameMap = new HashMap<>();
        for (ID id : nodeIds) {
            String fullName = getFullNameById(func, idNodeMap, id, joiner, includeRoot);
            idNameMap.put(id, fullName);
        }
        return idNameMap;
    }

    public static <ID, A extends CommonNode> Map<ID, String> getFullNameByIds(Map<ID, A> idNodeMap, List<ID> nodeIds, String joiner, boolean includeRoot) {
       return getFullNameByIds(getLabelFunc(), idNodeMap, nodeIds, joiner, includeRoot);
    }


    public static <ID, A extends CommonNode> Map<ID, String> getFullNameByNodes(Function<A, String> func, Map<ID, A> idNodeMap, List<A> nodes, String joiner, boolean includeRoot) {
        if (CollectionUtils.isEmpty(nodes)) {
            return new HashMap<>(0);
        }

        List<ID> nodeIds = nodes.stream().map(n -> (ID) n.getId()).collect(Collectors.toList());
        return getFullNameByIds(func, idNodeMap, nodeIds, joiner, includeRoot);
    }

    public static <ID, A extends CommonNode> Map<ID, String> getFullNameByNodes(Function<A, String> func, List<A> all, List<A> nodes, String joiner, boolean includeRoot) {
        return getFullNameByNodes(func, toIdMap(all), nodes, joiner, includeRoot);
    }

    public static <ID, A extends CommonNode> Map<ID, String> getFullNameByNodes(Map<ID, A> idNodeMap, List<A> nodes, String joiner, boolean includeRoot) {
        return getFullNameByNodes(getLabelFunc(), idNodeMap, nodes, joiner, includeRoot);
    }

    public static <ID, A extends CommonNode> Map<ID, String> getFullNameByNodes(List<A> all, List<A> nodes, String joiner, boolean includeRoot) {
        return getFullNameByNodes(getLabelFunc(), all, nodes, joiner, includeRoot);
    }

    public static <ID, A extends CommonNode> Map<ID, String> getFullName(Function<A, String> func, List<A> all, String joiner, boolean includeRoot) {
        return getFullNameByNodes(func, all, all, joiner, includeRoot);
    }

    public static <ID, A extends CommonNode> Map<ID, String> getFullName(List<A> all, String joiner, boolean includeRoot) {
        return getFullName(getLabelFunc(), all , joiner, includeRoot);
    }



}