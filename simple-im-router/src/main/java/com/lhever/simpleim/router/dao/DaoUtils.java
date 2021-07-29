package com.lhever.simpleim.router.dao;

import java.util.Objects;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/28 22:43
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/28 22:43
 * @modify by reason:{方法名}:{原因}
 */
public class DaoUtils {

    public static int getIndex(Object key, int total) {
        if (total == 0) {
            return 0;
        }
        int i = Objects.hash(key) % total;
        return i;
    }
}
