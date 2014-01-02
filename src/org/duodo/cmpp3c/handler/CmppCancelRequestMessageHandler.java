/**
 * 
 */
package org.duodo.cmpp3c.handler;

import org.duodo.cmpp3c.message.CmppCancelRequestMessage;
import org.duodo.cmpp3c.message.CmppCancelResponseMessage;
import org.duodo.cmpp3c.packet.CmppPacketType;
import org.duodo.netty3ext.message.Message;
import org.duodo.netty3ext.packet.PacketType;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

/**
 * @author huzorro(huzorro@gmail.com)
 *
 */
public class CmppCancelRequestMessageHandler extends
		SimpleChannelUpstreamHandler {
	private PacketType packetType;
	
	public CmppCancelRequestMessageHandler() {
		this(CmppPacketType.CMPPCANCELREQUEST);
	}

	public CmppCancelRequestMessageHandler(PacketType packetType) {
		this.packetType = packetType;
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Message message = (Message) e.getMessage();
        long commandId = ((Long) message.getHeader().getCommandId()).longValue();
        if(commandId != packetType.getCommandId()){
            super.messageReceived(ctx, e);
            return;
        }		
        
        CmppCancelRequestMessage requestMessage = (CmppCancelRequestMessage) message;
		CmppCancelResponseMessage responseMessage = new CmppCancelResponseMessage();
		
		responseMessage.setRequest(requestMessage);
		
		responseMessage.setSuccessId(0L);
		
		ctx.getChannel().write(responseMessage);
		//TODO requestMessage write in queue
		
		super.messageReceived(ctx, e);
	}
	
}
