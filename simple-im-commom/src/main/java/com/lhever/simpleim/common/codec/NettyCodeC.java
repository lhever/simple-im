package com.lhever.simpleim.common.codec;

import com.lhever.simpleim.common.consts.MsgType;
import com.lhever.simpleim.common.msg.*;
import com.lhever.simpleim.common.pojo.Group;
import com.lhever.simpleim.common.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sunnick on 2019/1/13/013.
 * <p>
 * 编解码对象
 */
public class NettyCodeC {
    private static final Logger logger = LoggerFactory.getLogger(NettyCodeC.class);
    /**
     * 魔数
     */
    public static final int MAGIC_NUMBER = 1314;
    public static final byte[] EMPTY_BYTES = new byte[0];

    public static final byte BYTE_PING = 0;
    public static final byte[] PING_BYTES = new byte[]{BYTE_PING};
    public static final byte BYTE_PONG = 1;
    public static final byte[] PONG_BYTES = new byte[]{BYTE_PONG};

    public static NettyCodeC instance = new NettyCodeC();

    /**
     * 采用单例模式
     */
    public static NettyCodeC getInstance() {
        return instance;
    }

    private static final Map<Integer, Class<? extends Msg>> packetTypeMap;

    static {
        packetTypeMap = new HashMap<Integer, Class<? extends Msg>>();
        packetTypeMap.put(MsgType.HEART_BEAT, PingPong.class);

        packetTypeMap.put(MsgType.LOGIN_REQ, loginReq.class);
        packetTypeMap.put(MsgType.LOGIN_RESP, loginResp.class);

        packetTypeMap.put(MsgType.MESSAGE_REQUEST, MessageReq.class);
        packetTypeMap.put(MsgType.MESSAGE_RESPONSE, MessageResp.class);

        packetTypeMap.put(MsgType.CREATE_GROUP_REQUEST, CreateGroupReq.class);
        packetTypeMap.put(MsgType.CREATE_GROUP_RESPONSE, CreateGroupResp.class);

        packetTypeMap.put(MsgType.GROUP_MESSAGE_REQUEST, GroupMessageReq.class);
        packetTypeMap.put(MsgType.GROUP_MESSAGE_RESPONSE, GroupMessageResp.class);


        packetTypeMap.put(MsgType.MESSAGE_ACK, MessageAck.class);

        packetTypeMap.put(MsgType.GROUP_MESSAGE_ACK, GroupMessageAck.class);
    }



    private NettyCodeC() {

    }

    /**
     * 编码
     * 魔数（4字节） + 版本号（1字节） + + 数据长度（4字节）  + 指令（4字节）  + 数据（N字节）
     */
    public ByteBuf encode(ByteBufAllocator alloc, Msg packet) {
        //创建ByteBuf对象
        ByteBuf buf = alloc.ioBuffer();
        return encode(buf, packet);
    }

    public ByteBuf encode(ByteBuf buf, Msg msg) {
        //序列化java对象
        byte[] objBytes = null;
        if (msg == null) {
            objBytes = EMPTY_BYTES;
        } else if (msg == PingPong.PING) {
            objBytes = PING_BYTES;
        } else if (msg == PingPong.PONG) {
            objBytes = PONG_BYTES;
        } else {
            objBytes = JsonUtils.obj2Byte(msg);
            objBytes = (objBytes == null) ? EMPTY_BYTES : objBytes;
        }

        //实际编码过程，即组装通信包
        //数据长度（4字节） + 魔数（4字节） + 版本号（1字节） +  + 指令（4字节）  + 数据（N字节）
        buf.writeInt(objBytes.length);
        buf.writeInt(MAGIC_NUMBER);
        buf.writeByte(msg.getVersion());
        buf.writeInt(msg.getType());
        buf.writeBytes(objBytes);
        return buf;
    }

    public ByteBuf encode(Msg packet) {
        return encode(ByteBufAllocator.DEFAULT, packet);
    }

    /**
     * 解码
     * <p>
     * 数据长度（4字节） + 魔数（4字节） + 版本号（1字节） +  + 指令（4字节）  + 数据（N字节）
     */
    public Msg decode(ByteBuf buf) {
        int len = buf.readInt();
        //魔数校验（暂不做）
        buf.skipBytes(4);
        //版本号校验（暂不做）
        buf.skipBytes(1);
        //指令
        int type = buf.readInt();

        Class<? extends Msg> packetType = getRequestType(type);
        if (packetType == PingPong.class) {
            byte b = buf.readByte();
            if ( b == BYTE_PING) {
                return PingPong.PING;
            } else {
                return PingPong.PONG;
            }
        }
        if (packetType == null) {
            logger.error("类型没有注册:{}, 无法解析");
        }
        //数据
        byte[] dataBytes = new byte[len];
        buf.readBytes(dataBytes);
        Msg msg = JsonUtils.byte2Obj(packetType, dataBytes);
        return msg;
    }


    /**
     * 根据指令类型获取对应的packet
     *
     * @param command
     * @return packet
     */
    private Class<? extends Msg> getRequestType(Integer command) {
        return packetTypeMap.get(command);
    }


}
