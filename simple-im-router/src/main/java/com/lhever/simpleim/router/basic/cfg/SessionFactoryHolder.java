package com.lhever.simpleim.router.basic.cfg;

import com.lhever.common.core.utils.ArrayUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class SessionFactoryHolder {

    private SqlSessionFactory[] factories;
    private int len;

    public SessionFactoryHolder(SqlSessionFactory[] factories) {
        this.factories = factories;
        this.len = factories == null ? 0 : factories.length;
    }



    public SqlSessionFactory getSessionFactory(Object key) {
        if (ArrayUtils.isEmpty(factories)) {
            return null;
        }
        int hash = Objects.hash(key);
        int idx = hash % len;
        return factories[idx];
    }

    public SqlSessionFactory getDefault() {
        if (ArrayUtils.isEmpty(factories)) {
            return null;
        }
        return factories[0];
    }



}
