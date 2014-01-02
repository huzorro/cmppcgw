package org.duodo.cmpp3c.factory.session;

import java.util.concurrent.ScheduledExecutorService;

import org.duodo.netty3ext.config.session.SessionConfig;
import org.duodo.netty3ext.factory.Factory;
import org.duodo.netty3ext.factory.session.DefaultClientSessionFactory;
import org.duodo.netty3ext.future.QFuture;
import org.duodo.netty3ext.message.Message;
import org.duodo.netty3ext.pool.session.SessionPool;
import org.duodo.netty3ext.queue.BdbQueueMap;
import org.duodo.netty3ext.session.Session;
import org.duodo.netty3ext.tcp.client.NettyTcpClient;
import org.jboss.netty.channel.ChannelFuture;

/**
 *
 * @author huzorro(huzorro@gmail.com)
 */
public class CmppClientSessionFactory<T extends Session> extends DefaultClientSessionFactory<T> {

	/**
	 * 
	 * @param nettyTcpClient
	 * @param messageFactory
	 * @param config
	 * @param deliverQueue
	 * @param responseQueue
	 * @param receiveQueue
	 * @param scheduleExecutor
	 * @param sessionPool
	 */
    public CmppClientSessionFactory(
            NettyTcpClient<ChannelFuture> nettyTcpClient,
            Factory<?> messageFactory,
            SessionConfig config,
            BdbQueueMap<Long, QFuture<Message>> deliverQueue,
            BdbQueueMap<Long, QFuture<Message>> responseQueue,
            BdbQueueMap<Long, QFuture<Message>> receiveQueue,
            ScheduledExecutorService scheduleExecutor, SessionPool sessionPool) {
		super(nettyTcpClient, messageFactory, config, deliverQueue,
				responseQueue, receiveQueue, scheduleExecutor, sessionPool);
    }

}
