package org.duodo.cmpp3c.factory.pipeline;

import java.util.concurrent.TimeUnit;

import org.duodo.cmpp3c.decoder.CmppActiveTestResponseMessageDecoder;
import org.duodo.cmpp3c.decoder.CmppCancelResponseMessageDecoder;
import org.duodo.cmpp3c.decoder.CmppConnectResponseMessageDecoder;
import org.duodo.cmpp3c.decoder.CmppHeaderDecoder;
import org.duodo.cmpp3c.decoder.CmppQueryResponseMessageDecoder;
import org.duodo.cmpp3c.decoder.CmppSubmitResponseMessageDecoder;
import org.duodo.cmpp3c.decoder.CmppTerminateResponseMessageDecoder;
import org.duodo.cmpp3c.encoder.CmppActiveTestRequestMessageEncoder;
import org.duodo.cmpp3c.encoder.CmppCancelRequestMessageEncoder;
import org.duodo.cmpp3c.encoder.CmppConnectRequestMessageEncoder;
import org.duodo.cmpp3c.encoder.CmppHeaderEncoder;
import org.duodo.cmpp3c.encoder.CmppQueryRequestMessageEncoder;
import org.duodo.cmpp3c.encoder.CmppSubmitRequestMessageEncoder;
import org.duodo.cmpp3c.encoder.CmppTerminateRequestMessageEncoder;
import org.duodo.cmpp3c.handler.CmppActiveTestResponseMessageHandler;
import org.duodo.cmpp3c.handler.CmppCancelResponseMessageHandler;
import org.duodo.cmpp3c.handler.CmppCommonsHeaderHandler;
import org.duodo.cmpp3c.handler.CmppCommonsMessageHandler;
import org.duodo.cmpp3c.handler.CmppConnectResponseMessageHandler;
import org.duodo.cmpp3c.handler.CmppIdleStateHandler;
import org.duodo.cmpp3c.handler.CmppQueryResponseMessageHandler;
import org.duodo.cmpp3c.handler.CmppSubmitRequestMessageHeaderHandler;
import org.duodo.cmpp3c.handler.CmppSubmitResponseMessageHandler;
import org.duodo.cmpp3c.handler.CmppTerminateResponseMessageHandler;
import org.duodo.netty3ext.config.session.SessionConfig;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

/**
 * @author huzorro(huzorro@gmail.com)
 *
 */
public class CmppDownstreamClientChannelPipelineFactory implements ChannelPipelineFactory {
	private final Timer timer;
	private final SessionConfig config;
	/**
	 * 
	 */
	public CmppDownstreamClientChannelPipelineFactory(SessionConfig config) {
		this(config, new HashedWheelTimer());
	}
	
	
	public CmppDownstreamClientChannelPipelineFactory(SessionConfig config,
			Timer timer) {
		this.timer = timer;
		this.config = config;
	}


	@Override
	public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        
        pipeline.addLast("IdleStateHandler", new IdleStateHandler(timer, 0, 0, config.getIdleTime(), TimeUnit.SECONDS));
        pipeline.addLast("CmppIdleStateHandler", new CmppIdleStateHandler());
        
        pipeline.addLast("CmppHeaderDecoder", new CmppHeaderDecoder());
        
        pipeline.addLast("CmppSubmitResponseMessageDecoder", new CmppSubmitResponseMessageDecoder());
        pipeline.addLast("CmppSubmitRequestMessageEncoder", new CmppSubmitRequestMessageEncoder());
        
        pipeline.addLast("CmppConnectResponseMessageDecoder", new CmppConnectResponseMessageDecoder());
        pipeline.addLast("CmppConnectRequestMessageEncoder", new CmppConnectRequestMessageEncoder());
        
        pipeline.addLast("CmppCancelResponseMessageDecoder", new CmppCancelResponseMessageDecoder());
        pipeline.addLast("CmppCancelRequestMessageEncoder", new CmppCancelRequestMessageEncoder());
        
        pipeline.addLast("CmppQueryResponseMessageDecoder", new CmppQueryResponseMessageDecoder());
        pipeline.addLast("CmppQueryRequestMessageEncoder", new CmppQueryRequestMessageEncoder());
        
        pipeline.addLast("CmppActiveTestResponseMessageDecoder", new CmppActiveTestResponseMessageDecoder());
        pipeline.addLast("CmppActiveTestRequestMessageEncoder", new CmppActiveTestRequestMessageEncoder());
        
        pipeline.addLast("CmppTerminateResponseMessageDecoder", new CmppTerminateResponseMessageDecoder());
        pipeline.addLast("CmppTerminateRequestMessageEncoder", new CmppTerminateRequestMessageEncoder());
        
        pipeline.addLast("CmppHeaderEncoder", new CmppHeaderEncoder());          
        pipeline.addLast("CmppSubmitRequestMessageHeaderHandler", new CmppSubmitRequestMessageHeaderHandler());
        pipeline.addLast("CmppCommonsHeaderHandler", new CmppCommonsHeaderHandler());
        pipeline.addLast("CmppCommonsMessageHandler", new CmppCommonsMessageHandler());
        
	    pipeline.addLast("CmppSubmitResponseMessageHandler", new CmppSubmitResponseMessageHandler());
	    pipeline.addLast("CmppConnectResponseMessageHandler", new CmppConnectResponseMessageHandler());
	    pipeline.addLast("CmppCancelResponseMessageHandler", new CmppCancelResponseMessageHandler());
	    pipeline.addLast("CmppQueryResponseMessageHandler", new CmppQueryResponseMessageHandler());
	    pipeline.addLast("CmppActiveTestResponseMessageHandler", new CmppActiveTestResponseMessageHandler());
	    pipeline.addLast("CmppTerminateResponseMessageHandler", new CmppTerminateResponseMessageHandler());
		return pipeline;
	}

}
