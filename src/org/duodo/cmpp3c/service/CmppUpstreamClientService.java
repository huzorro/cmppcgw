package org.duodo.cmpp3c.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import org.duodo.cmpp3c.factory.message.CmppConnectRequestMessageFactory;
import org.duodo.cmpp3c.factory.pipeline.CmppUpstreamClientChannelPipelineFactory;
import org.duodo.cmpp3c.factory.session.CmppClientSessionFactory;
import org.duodo.cmpp3c.message.CmppConnectRequestMessage;
import org.duodo.netty3ext.config.session.SessionConfig;
import org.duodo.netty3ext.factory.Factory;
import org.duodo.netty3ext.factory.tcp.NettyTcpClientFactory;
import org.duodo.netty3ext.future.QFuture;
import org.duodo.netty3ext.message.Message;
import org.duodo.netty3ext.pool.session.SessionPool;
import org.duodo.netty3ext.queue.BdbQueueMap;
import org.duodo.netty3ext.service.Service;
import org.duodo.netty3ext.session.Session;
import org.duodo.netty3ext.tcp.client.NettyTcpClient;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author huzorro(huzorro@gmail.com)
 */
public class CmppUpstreamClientService implements Service {
    private final Logger logger = LoggerFactory.getLogger(CmppUpstreamClientService.class);
    private Map<String, SessionConfig> configMap;
    private Map<SessionConfig, ClientBootstrap> clientBootstrapMap;
    private Map<Object, BdbQueueMap<Long, QFuture<Message>>> receiveMsgQueueMap;
    private Map<Object, BdbQueueMap<Long, QFuture<Message>>> responseMsgQueueMap;
    private Map<Object, BdbQueueMap<Long, QFuture<Message>>> deliverMsgQueueMap;
    private Map<SessionConfig, ScheduledExecutorService> scheduleExecutorMap;
    private Map<SessionConfig, SessionPool> sessionPoolMap;
    private Map<SessionConfig, Factory<Session>> sessionFactoryMap;
    private List<String> configList;
    private List<SessionConfig> upstreamServicesRunningList;
    public CmppUpstreamClientService(
            Map<String, SessionConfig> configMap,
            Map<SessionConfig, ClientBootstrap> clientBootstrapMap,
            Map<Object, BdbQueueMap<Long, QFuture<Message>>> receiveMsgQueueMap,
            Map<Object, BdbQueueMap<Long, QFuture<Message>>> responseMsgQueueMap,
            Map<Object, BdbQueueMap<Long, QFuture<Message>>> deliverMsgQueueMap,
            Map<SessionConfig, ScheduledExecutorService> scheduleExecutorMap,
            Map<SessionConfig, SessionPool>  sessionPoolMap,
            Map<SessionConfig, Factory<Session>> sessionFactoryMap,
            List<SessionConfig> upstreamServicesRunningList) {
		this(configMap, clientBootstrapMap, receiveMsgQueueMap,
				responseMsgQueueMap, deliverMsgQueueMap, scheduleExecutorMap,
				sessionPoolMap, sessionFactoryMap, upstreamServicesRunningList, null);
    }    
    /**
     * 
     * @param configMap
     * @param clientBootstrapMap
     * @param requestMsgQueueMap
     * @param responseMsgQueueMap
     * @param deliverMsgQueueMap
     * @param messageQueueMap
     * @param scheduleExecutorMap
     * @param sessionPoolMap
     * @param configList
     */
    public CmppUpstreamClientService(
            Map<String, SessionConfig> configMap,
            Map<SessionConfig, ClientBootstrap> clientBootstrapMap,
            Map<Object, BdbQueueMap<Long, QFuture<Message>>> receiveMsgQueueMap,
            Map<Object, BdbQueueMap<Long, QFuture<Message>>> responseMsgQueueMap,
            Map<Object, BdbQueueMap<Long, QFuture<Message>>> deliverMsgQueueMap,
            Map<SessionConfig, ScheduledExecutorService> scheduleExecutorMap,
            Map<SessionConfig, SessionPool>  sessionPoolMap,
            Map<SessionConfig, Factory<Session>> sessionFactoryMap,
            List<SessionConfig> upstreamServicesRunningList,
            List<String> configList) {
        this.configMap = configMap;
        this.clientBootstrapMap = clientBootstrapMap;
        this.receiveMsgQueueMap = receiveMsgQueueMap;
        this.responseMsgQueueMap = responseMsgQueueMap;
        this.deliverMsgQueueMap = deliverMsgQueueMap;
        this.scheduleExecutorMap = scheduleExecutorMap;
        this.sessionPoolMap = sessionPoolMap;
        this.sessionFactoryMap = sessionFactoryMap;
        this.upstreamServicesRunningList = upstreamServicesRunningList;
        this.configList = configList;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            process();
        } catch (Exception e) {
            logger.error("Cmpp Upstream Client Service failed {}", e);
            Runtime.getRuntime().exit(-1);
        }
    }

    /* (non-Javadoc)
     * @see me.huzorro.gateway.Service#process()
     */
    @Override
    public void process() throws Exception {
        for(SessionConfig config : configMap.values()) {
        	if(configList != null && !configList.contains(config.getChannelIds())) continue;
        	create(config);
        	upstreamServicesRunningList.add(config);
        }
         
    }
    
    protected void create(SessionConfig config) throws Exception {
        ChannelPipelineFactory pipelineFactory = new CmppUpstreamClientChannelPipelineFactory(config);
        NettyTcpClientFactory<NettyTcpClient<ChannelFuture>> tcpClientFactory = 
                new NettyTcpClientFactory<NettyTcpClient<ChannelFuture>>(
                        config.getHost(), config.getPort(), pipelineFactory, clientBootstrapMap.get(config));
        NettyTcpClient<ChannelFuture> tcpClient = tcpClientFactory.create();
        
        CmppConnectRequestMessageFactory<CmppConnectRequestMessage> connectRequestMessageFacotry = 
                new CmppConnectRequestMessageFactory<CmppConnectRequestMessage>(config);
        CmppClientSessionFactory<Session> sessionFactory = new CmppClientSessionFactory<Session>(
                tcpClient, 
                connectRequestMessageFacotry, 
                config, 
                deliverMsgQueueMap.get(config), 
                responseMsgQueueMap.get(config),
                receiveMsgQueueMap.get(config), 
                scheduleExecutorMap.get(config),
                sessionPoolMap.get(config));
        sessionFactoryMap.put(config, sessionFactory);
        for(int i = 0; i < config.getMaxSessions(); i++) {
            sessionFactory.create();
        }    	
    }

}
