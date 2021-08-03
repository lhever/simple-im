package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/8/3 21:35
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/8/3 21:35
 * @modify by reason:{方法名}:{原因}
 */
@Getter
@Setter
@NoArgsConstructor
public class MessageAck extends Msg {
    @Override
    public Integer getType() {
        return MsgType.MESSAGE_ACK;
    }

    private String msgId;
    private String receiveId;
}
