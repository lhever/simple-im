package com.lhever.simpleim.router.basic.cfg;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Properties;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/9/24 16:30
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/9/24 16:30
 * @modify by reason:{方法名}:{原因}
 */
@Configuration
@MapperScan(basePackages = {"com.lhever.simpleim.router.dao"})
public class DataSourceConfig {
    private static final String AOP_POINTCUT_EXPRESSION = "execution (* com.lhever.simpleim.router.***.service..*.*(..))";



    @Bean("dataSource")
    public DruidDataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:postgresql://10.33.65.9:5432/postgres?useUnicode=true&characterEncoding=utf-8&useLegacyDatetimeCode=false&serverTimezone=UTC");
        dataSource.setUsername("postgres");
        dataSource.setPassword("123qaz!@#");
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setMinIdle(5);
        dataSource.setValidationQuery("select 1");
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setMaxWait(60000);
        dataSource.setInitialSize(5);
        dataSource.setMaxActive(100);
        return dataSource;
    }

    // 创建事务管理器, 使用JPA事物管理器
    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager transactionManager() {
        PlatformTransactionManager jpaTransactionManager = new DataSourceTransactionManager(dataSource());
        Properties prop = new Properties();
        prop.setProperty("proxy-target-class", "true");
        return jpaTransactionManager;
    }

    @Bean(name = "txAdvice")
    public TransactionInterceptor txAdvice() {

        DefaultTransactionAttribute txAttrRequired = new DefaultTransactionAttribute();
        txAttrRequired.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        DefaultTransactionAttribute txAttr_REQUIRED_READONLY = new DefaultTransactionAttribute();
        txAttr_REQUIRED_READONLY.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        txAttr_REQUIRED_READONLY.setReadOnly(true);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        // required事务
        source.addTransactionalMethod("insert*", txAttrRequired);
        source.addTransactionalMethod("put*", txAttrRequired);
        source.addTransactionalMethod("save*", txAttrRequired);
        source.addTransactionalMethod("add*", txAttrRequired);
        source.addTransactionalMethod("register*", txAttrRequired);
        source.addTransactionalMethod("edit*", txAttrRequired);
        source.addTransactionalMethod("update*", txAttrRequired);
        source.addTransactionalMethod("set*", txAttrRequired);
        source.addTransactionalMethod("merge*", txAttrRequired);
        source.addTransactionalMethod("del*", txAttrRequired);
        source.addTransactionalMethod("delete*", txAttrRequired);
        source.addTransactionalMethod("remove*", txAttrRequired);
        source.addTransactionalMethod("use*", txAttrRequired);
        source.addTransactionalMethod("start*", txAttrRequired);
        source.addTransactionalMethod("generate*", txAttrRequired);
        source.addTransactionalMethod("sync*", txAttrRequired);
        source.addTransactionalMethod("finish*", txAttrRequired);
        source.addTransactionalMethod("grade*", txAttrRequired);
        source.addTransactionalMethod("execute*", txAttrRequired);
        source.addTransactionalMethod("exec*", txAttrRequired);
        source.addTransactionalMethod("modify*", txAttrRequired);
        source.addTransactionalMethod("change*", txAttrRequired);
        //required readOnly事务
        source.addTransactionalMethod("get*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("query*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("find*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("list*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("count*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("*", txAttr_REQUIRED_READONLY);
        return new TransactionInterceptor(transactionManager(), source);
    }


    @Bean("txadvisor")
    public Advisor txadvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }


    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(dataSource());// 指定数据源(这个必须有，否则报错)
        // 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
        fb.setTypeAliasesPackage("com.lhever.simpleim.router.pojo");// 指定基包
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().
                getResources("classpath*:com.lhever.simpleim.router.dao,/com/lhever/simpleim/router/dao"));
        fb.setPlugins(new Interceptor[]{pageInterceptor()});
        return fb.getObject();
    }


    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        // 使用上面配置的Factory
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory());
        return template;
    }


    @Bean("pageInterceptor")
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


}
