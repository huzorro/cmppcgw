/**
 * 
 */
package org.duodo.cmpp3c.handler;

import org.duodo.cmpp3c.message.CmppDeliverRequestMessage;
import org.duodo.cmpp3c.packet.CmppPacketType;
import org.duodo.cmpp3c.packet.CmppReportRequest;
import org.duodo.netty3ext.message.Message;
import org.duodo.netty3ext.packet.PacketType;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * @author huzorro(huzorro@gmail.com)
 *
 */
public class CmppDeliverRequestMessageHeaderHandler extends OneToOneEncoder {
	private PacketType packetType;
	/**
	 * 
	 */
	public CmppDeliverRequestMessageHeaderHandler() {
		this(CmppPacketType.CMPPDELIVERREQUEST);
	}

	public CmppDeliverRequestMessageHeaderHandler(PacketType packetType) {
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
        
        if(message.getPacketType().getPacketStructures()[0].isFixPacketLength()) {
        	return msg;
        }
        CmppDeliverRequestMessage requestMessage = (CmppDeliverRequestMessage) message;
        
		requestMessage.getHeader().setBodyLength(
				requestMessage.getHeader().getBodyLength()
						+ (requestMessage.isReport() ? CmppReportRequest.values()[0].getBodyLength() :requestMessage.getMsgLength()));
		requestMessage.getHeader().setPacketLength(
				requestMessage.getHeader().getPacketLength()
						+ (requestMessage.isReport() ? CmppReportRequest.values()[0].getBodyLength() :requestMessage.getMsgLength()));
		return message;
	}

}
