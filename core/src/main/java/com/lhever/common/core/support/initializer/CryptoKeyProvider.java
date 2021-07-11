package com.lhever.common.core.support.initializer;

import java.util.List;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/5/15 14:30
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/5/15 14:30
 * @modify by reason:{方法名}:{原因}
 */
@FunctionalInterface
public interface CryptoKeyProvider {

    List<String> croptoKeys();


}
