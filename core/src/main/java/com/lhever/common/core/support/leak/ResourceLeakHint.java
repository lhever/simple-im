package com.lhever.common.core.support.leak;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/7/31 9:20
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/7/31 9:20
 * @modify by reason:{方法名}:{原因}
 */
public interface ResourceLeakHint {
    /**
     * Returns a human-readable message that potentially enables easier resource leak tracking.
     */
    String toHintString();
}

