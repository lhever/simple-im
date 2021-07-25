package com.lhever.simpleim.router.basic.cfg;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.session.SqlSessionFactory;

@Getter
@Setter
@NoArgsConstructor
public class SessionFactoryHolder {

    private SqlSessionFactory[] factories;

    public SessionFactoryHolder(SqlSessionFactory[] factories) {
        this.factories = factories;
    }



}
