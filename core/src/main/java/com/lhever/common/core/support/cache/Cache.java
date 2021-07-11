package com.lhever.common.core.support.cache;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/7/26 19:23
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/7/26 19:23
 * @modify by reason:{方法名}:{原因}
 */

public interface Cache {

    /**
     * @return The identifier of this cache
     */
    String getId();

    /**
     * @param key Can be any object but usually it is a CacheKey
     * @param value The result of a select.
     */
    void putObject(Object key, Object value);

    /**
     * @param key The key
     * @return The object stored in the cache.
     */
    Object getObject(Object key);

    /**
     * As of 3.3.0 this method is only called during a rollback
     * for any previous value that was missing in the cache.
     * This lets any blocking cache to release the lock that
     * may have previously put on the key.
     * A blocking cache puts a lock when a value is null
     * and releases it when the value is back again.
     * This way other threads will wait for the value to be
     * available instead of hitting the database.
     *
     *
     * @param key The key
     * @return Not used
     */
    Object removeObject(Object key);

    /**
     * Clears this cache instance
     */
    void clear();

    /**
     * Clears the item in cache, size * clearFactor instances will be removed randomly
     */
    void clear(float clearFactor);

    /**
     * Optional. This method is not called by the core.
     *
     * @return The number of elements stored in the cache (not its capacity).
     */
    int getSize();

}
