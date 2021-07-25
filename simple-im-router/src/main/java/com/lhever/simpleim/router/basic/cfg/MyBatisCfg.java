package com.lhever.simpleim.router.basic.cfg;


import com.lhever.common.core.utils.CollectionUtils;
import com.lhever.simpleim.router.basic.util.MybatisHelper;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MyBatisCfg {


    @Bean
    public SessionFactoryHolder sessionFactoryHolder() {
        List<DataSourceProp> dataSources = RouterConfig.dataSources;
        List<DataSourceProp> dataSourceProps = CollectionUtils.filter(dataSources, ds -> ds != null);

        DefaultSqlSessionFactory[] arrs = new DefaultSqlSessionFactory[dataSourceProps.size()];
        MybatisHelper helper = new MybatisHelper();
        for (int i = 0; i < dataSourceProps.size(); i++) {
            DataSourceProp  ds = dataSourceProps.get(i);
            DefaultSqlSessionFactory defaultSqlSessionFactory = helper.defaultSqlSessionFactory(ds);
            arrs[i] = defaultSqlSessionFactory;
        }

        SessionFactoryHolder holder = new SessionFactoryHolder(arrs);
        return holder;

    }















}
