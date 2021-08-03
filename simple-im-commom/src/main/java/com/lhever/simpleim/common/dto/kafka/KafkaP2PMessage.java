package com.lhever.simpleim.common.dto.kafka;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/31 20:51
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/31 20:51
 * @modify by reason:{方法名}:{原因}
 */
@Getter
@Setter
@NoArgsConstructor
public class KafkaP2PMessage {
    private String id;
    private String sendId;
    private String receiveId;
    private String message;
    private Date createTime;

    private boolean saveOnly = false;

    public boolean getSaveOnly() {
        return saveOnly;
    }

    public void setSaveOnly(boolean saveOnly) {
        this.saveOnly = saveOnly;
    }
}
