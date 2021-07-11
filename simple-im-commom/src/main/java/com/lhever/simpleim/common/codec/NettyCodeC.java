package com.lhever.simpleim.common.codec;

import com.lhever.simpleim.common.consts.MsgType;
import com.lhever.simpleim.common.msg.Msg;
import com.lhever.simpleim.common.msg.PingPong;
import com.lhever.simpleim.common.msg.AuthReq;
import com.lhever.simpleim.common.msg.AuthResp;
import com.lhever.simpleim.common.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sunnick on 2019/1/13/013.
 *
 * 编解码对象
 */
public class NettyCodeC {
    /**
     * 魔数
     */
    public static final int MAGIC_NUMBER = 1314520;
    public static final byte[] EMPTY_BYTES = new byte[0];

    public static NettyCodeC instance = new NettyCodeC();

    /**
     * 采用单例模式
     */
    public static NettyCodeC getInstance(){
        return instance;
    }

    private static final Map<Integer, Class<? extends Msg>> packetTypeMap;

    static {
        packetTypeMap = new HashMap<Integer,Class<? extends Msg>>();
        packetTypeMap.put(MsgType.HEART_BEAT, PingPong.class);
        packetTypeMap.put(MsgType.LOGIN_REQ, AuthReq.class);
        packetTypeMap.put(MsgType.LOGIN_RESP, AuthResp.class);
    }

    private NettyCodeC(){

    }

    /**
     * 编码
     * 魔数（4字节） + 版本号（1字节） + + 数据长度（4字节）  + 指令（4字节）  + 数据（N字节）
     */
    public ByteBuf encode(ByteBufAllocator alloc, Msg packet){
        //创建ByteBuf对象
        ByteBuf buf = alloc.ioBuffer();
       return encode(buf,packet);
    }

    public ByteBuf encode(ByteBuf buf,Msg packet){
        //序列化java对象
        byte[] objBytes = packet == null ? EMPTY_BYTES : JsonUtils.obj2Byte(packet);

        //实际编码过程，即组装通信包
        //魔数（4字节） + 版本号（1字节） + 序列化算法（1字节） + 指令（1字节） + 数据长度（4字节） + 数据（N字节）
        buf.writeInt(objBytes.length);
        buf.writeInt(MAGIC_NUMBER);
        buf.writeByte(packet.getVersion());
        buf.writeInt(packet.getType());
        buf.writeBytes(objBytes);
        return buf;
    }

    public ByteBuf encode(Msg packet){
        return encode(ByteBufAllocator.DEFAULT, packet);
    }

    /**
     * 解码
     *
     * 魔数（4字节） + 版本号（1字节） + 序列化算法（1字节） + 指令（1字节） + 数据长度（4字节） + 数据（N字节）
     */
    public Msg decode(ByteBuf buf) {
        int len = buf.readInt();
        //魔数校验（暂不做）
        buf.skipBytes(4);
        //版本号校验（暂不做）
        buf.skipBytes(1);
        //指令
        int type = buf.readInt();
        //数据
        byte[] dataBytes = new byte[len];
        buf.readBytes(dataBytes);

        Class<? extends Msg> packetType = getRequestType(type);

        Msg msg = JsonUtils.byte2Obj(packetType, dataBytes);
        return msg;
    }


    /**
     *
     * 根据指令类型获取对应的packet
     * @param command
     * @return packet
     */
    private Class<? extends Msg> getRequestType(Integer command) {
        return packetTypeMap.get(command);
    }


}
