package org.duodo.cmpp3c.encoder;

import org.duodo.cmpp3c.message.CmppConnectRequestMessage;
import org.duodo.cmpp3c.packet.CmppConnectRequest;
import org.duodo.cmpp3c.packet.CmppPacketType;
import org.duodo.netty3ext.global.GlobalVars;
import org.duodo.netty3ext.message.Message;
import org.duodo.netty3ext.packet.PacketType;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.google.common.primitives.Bytes;

/**
 *
 * @author huzorro(huzorro@gmail.com)
 */
public class CmppConnectRequestMessageEncoder extends OneToOneEncoder {
    private PacketType packetType;
    /**
     * 
     */
    public CmppConnectRequestMessageEncoder() {
        this(CmppPacketType.CMPPCONNECTREQUEST);
    }
    public CmppConnectRequestMessageEncoder(PacketType packetType) {
        this.packetType = packetType;
    }
    /* (non-Javadoc)
     * @see org.jboss.netty.handler.codec.oneone.OneToOneEncoder#encode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, java.lang.Object)
     */
    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel,
            Object msg) throws Exception {
    	if(!(msg instanceof Message)) return msg;
    	Message message = (Message) msg;
        long commandId = ((Long) message.getHeader().getCommandId()).longValue();
        if(commandId != packetType.getCommandId()) return msg;
        CmppConnectRequestMessage requestMessage = (CmppConnectRequestMessage) message;

        ChannelBuffer bodyBuffer = ChannelBuffers.dynamicBuffer();
		bodyBuffer.writeBytes(Bytes.ensureCapacity(requestMessage
				.getSourceAddr().getBytes(GlobalVars.defaultTransportCharset),
				CmppConnectRequest.SOURCEADDR.getLength(), 0));
		
        bodyBuffer.writeBytes(requestMessage.getAuthenticatorSource());
        
        bodyBuffer.writeByte(requestMessage.getVersion());
        bodyBuffer.writeInt((int) requestMessage.getTimestamp());
        
        message.setBodyBuffer(bodyBuffer.copy().array());
        
        ChannelBuffer messageBuffer = ChannelBuffers.dynamicBuffer();
        messageBuffer.writeBytes(message.getHeader().getHeadBuffer());
        messageBuffer.writeBytes(message.getBodyBuffer());
        
        
        return messageBuffer;
    }

}
