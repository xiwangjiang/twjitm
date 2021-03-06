package com.twjitm.common.netstack.coder.decode;

import com.alibaba.dubbo.registry.RegistryFactory;
import com.twjitm.common.factory.MessageRegistryFactory;
import com.twjitm.common.manager.LocalManager;
import com.twjitm.common.netstack.entity.AbstractNettyNetProtoBufMessage;
import com.twjitm.common.netstack.entity.NettyNetMessageBody;
import com.twjitm.common.netstack.entity.NettyNetMessageHead;
import com.twjitm.common.netstack.entity.udp.NettyUDPMessageHead;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by 文江 on 2017/11/16.
 * tcp解码器
 */
public class NettyNetProtoBuffTCPToMessageDecoderFactory implements INettyNetProtoBuffTCPToMessageDecoerFactory {

    public AbstractNettyNetProtoBufMessage praseMessage(ByteBuf byteBuf) {
        NettyNetMessageHead netMessageHead=new NettyUDPMessageHead();
        byteBuf.skipBytes(2);
        netMessageHead.setLength(byteBuf.readInt());
        netMessageHead.setVersion(byteBuf.readByte());
        //read message context
        //读取内容
        short cmd = byteBuf.readShort();
        netMessageHead.setCmd((short) 2);
        netMessageHead.setSerial(byteBuf.readInt());
        MessageRegistryFactory registryFactory = LocalManager.getInstance().getRegistryFactory();
        AbstractNettyNetProtoBufMessage nettyMessage = registryFactory.get(2);
        nettyMessage.setNettyNetMessageHead(netMessageHead);
        NettyNetMessageBody nettyNetMessageBody=new NettyNetMessageBody();

        int byteLength = byteBuf.readableBytes();
        ByteBuf bodyByteBuffer = Unpooled.buffer(256);
        byte[] bytes = new byte[byteLength];
        bodyByteBuffer = byteBuf.getBytes(byteBuf.readerIndex(), bytes);
        nettyNetMessageBody.setBytes(bytes);
        nettyMessage.setNettyNetMessageBody(nettyNetMessageBody);
        try {
            nettyMessage.decoderNetProtoBufMessageBody();
        } catch (Exception e) {
            e.printStackTrace();
            nettyMessage.release();
        }
        return nettyMessage;
    }


}
