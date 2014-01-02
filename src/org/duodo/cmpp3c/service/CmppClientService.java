package org.duodo.cmpp3c.service;

import java.util.List;

import org.duodo.cmpp3c.global.CmppGlobalVarsInitialize;
import org.duodo.netty3ext.global.GlobalVars;
import org.duodo.netty3ext.plugin.DefaultReceivedMsgPluginManagerService;
import org.duodo.netty3ext.plugin.DefaultSubmitMsgPluginManagerService;
import org.duodo.netty3ext.processor.MessageBindToChannelSubmitProcessor;
import org.duodo.netty3ext.service.MessageBindToChannelSubmitService;
import org.duodo.netty3ext.service.Service;
import org.duodo.netty3ext.service.manager.ClientServices;
import org.duodo.netty3ext.service.manager.DefaultSessionPoolWatchService;

/**
 * 
 * @author huzorro(huzorro@gmail.com)
 */
public class CmppClientService implements ClientServices {
	private String configName;
	private static final CmppGlobalVarsInitialize globalVarsInitialize = new CmppGlobalVarsInitialize();

	public CmppClientService() {
		this("cmppsession");
	}

	public CmppClientService(String configName) {
		this.configName = configName;
	}

	@Override
	public ClientServices upstreamGlobalVarsInit() throws Exception {
		return upstreamGlobalVarsInit(null);
	}

	@Override
	public ClientServices upstreamGlobalVarsInit(List<String> configList)
			throws Exception {
		globalVarsInitialize.upstreamSessionConfigInitialize(configList)
				.upstreamSessionPoolInitialize(configList)
				.upstreamMessageQueueInitialize(configList)
				.upstreamThreadPoolInitialize(configList)
				.upstreamClientBootstrapInitialize(configList)
				.upstreamMessagePluginManagerInitialize(configList);
		return this;
	}

	@Override
	public ClientServices downstreamGlobalVarsInit() throws Exception {
		return downstreamGlobalVarsInit(null);
	}

	@Override
	public ClientServices downstreamGlobalVarsInit(List<String> configList)
			throws Exception {
		globalVarsInitialize.downstreamSessionConfigInitialize(configList)
				.downstreamSessionPoolInitialize(configList)
				.downstreamMessageQueueInitialize(configList)
				.downstreamThreadPoolInitialize(configList)
				.downstreamClientBootstrapInitialize(configList)
				.downstreamMessagePluginManagerInitialize(configList);
		return this;
	}

	@Override
	public ClientServices duplexstreamGlobalVarsInit() throws Exception {
		return duplexstreamGlobalVarsInit(null);
	}

	@Override
	public ClientServices duplexstreamGlobalVarsInit(List<String> configList)
			throws Exception {
		globalVarsInitialize.duplexstreamSessionConfigInitialize(configList)
				.duplexstreamSessionPoolInitialize(configList)
				.duplexstreamMessageQueueInitialize(configList)
				.duplexstreamThreadPoolInitialize(configList)
				.duplexstreamClientBootstrapInitialize(configList)
				.duplexstreamMessagePluginManagerInitialize(configList);
		return this;
	}

	public Service upstreamServiceInit() {
		return upstreamServiceInit(null);
	}

	public Service upstreamServiceInit(List<String> configList) {
		return new CmppUpstreamClientService(
				GlobalVars.upstreamSessionConfigMap.get(configName),
				GlobalVars.clientBootstrapMap, GlobalVars.receiveMsgQueueMap,
				GlobalVars.responseMsgQueueMap, GlobalVars.deliverMsgQueueMap,
				GlobalVars.scheduleExecutorMap, GlobalVars.sessionPoolMap,
				GlobalVars.sessionFactoryMap,
				GlobalVars.upstreamServicesRunningList, configList);
	}

	public Service downstreamServiceInit() {
		return downstreamServiceInit(null);
	}

	public Service downstreamServiceInit(List<String> configList) {
		return new CmppDownstreamClientService(
				GlobalVars.downstreamSessionConfigMap.get(configName),
				GlobalVars.clientBootstrapMap, GlobalVars.receiveMsgQueueMap,
				GlobalVars.responseMsgQueueMap, GlobalVars.deliverMsgQueueMap,
				GlobalVars.scheduleExecutorMap, GlobalVars.sessionPoolMap,
				GlobalVars.sessionFactoryMap,
				GlobalVars.downstreamServicesRunningList, configList);
	}

	public Service duplexstreamServiceInit() {
		return duplexstreamServiceInit(null);
	}

	public Service duplexstreamServiceInit(List<String> configList) {
		return new CmppDuplexstreamClientService(
				GlobalVars.duplexSessionConfigMap.get(configName),
				GlobalVars.clientBootstrapMap, GlobalVars.receiveMsgQueueMap,
				GlobalVars.responseMsgQueueMap, GlobalVars.deliverMsgQueueMap,
				GlobalVars.scheduleExecutorMap, GlobalVars.sessionPoolMap,
				GlobalVars.sessionFactoryMap,
				GlobalVars.duplexstreamServicesRunningList, configList);
	}

	public Service upstreamPoolWatchServiceInit() {
		return upstreamPoolWatchServiceInit(null);
	}

	public Service upstreamPoolWatchServiceInit(List<String> configList) {
		return new DefaultSessionPoolWatchService(GlobalVars.sessionPoolMap,
				GlobalVars.sessionFactoryMap,
				GlobalVars.upstreamSessionConfigMap.get(configName),
				GlobalVars.externalScheduleExecutorMap, configList);
	}

	public Service downstreamPoolWatchServiceInit() {
		return downstreamPoolWatchServiceInit(null);
	}

	public Service downstreamPoolWatchServiceInit(List<String> configList) {
		return new DefaultSessionPoolWatchService(GlobalVars.sessionPoolMap,
				GlobalVars.sessionFactoryMap,
				GlobalVars.downstreamSessionConfigMap.get(configName),
				GlobalVars.externalScheduleExecutorMap, configList);
	}

	public Service duplexstreamPoolWatchServiceInit() {
		return duplexstreamPoolWatchServiceInit(null);
	}

	public Service duplexstreamPoolWatchServiceInit(List<String> configList) {
		return new DefaultSessionPoolWatchService(GlobalVars.sessionPoolMap,
				GlobalVars.sessionFactoryMap,
				GlobalVars.duplexSessionConfigMap.get(configName),
				GlobalVars.externalScheduleExecutorMap, configList);
	}

	@Override
	public Service downstreamDeliverServiceInit() {
		return downstreamDeliverServiceInit(null);
	}

	@Override
	public Service downstreamDeliverServiceInit(List<String> configList) {
		return new MessageBindToChannelSubmitService<MessageBindToChannelSubmitProcessor>(
				GlobalVars.sessionPoolMap,
				GlobalVars.deliverMsgQueueMap, 
				GlobalVars.reserveMsgQueueMap, 
				GlobalVars.executorServiceMap,
				GlobalVars.downstreamSessionConfigMap.get(configName), 
				false, 
				MessageBindToChannelSubmitProcessor.class,
				configList);
	}

	@Override
	public Service downstreamReserveDeliverServiceInit() {
		return downstreamReserveDeliverServiceInit(null);
	}

	@Override
	public Service downstreamReserveDeliverServiceInit(List<String> configList) {
		return new MessageBindToChannelSubmitService<MessageBindToChannelSubmitProcessor>(
				GlobalVars.sessionPoolMap,
				GlobalVars.reserveMsgQueueMap, 
				GlobalVars.reserveMsgQueueMap, 
				GlobalVars.executorServiceMap,
				GlobalVars.downstreamSessionConfigMap.get(configName), 
				false, 
				MessageBindToChannelSubmitProcessor.class,
				configList);
	}

	@Override
	public Service duplexstreamDeliverServiceInit() {
		return duplexstreamDeliverServiceInit(null);
	}

	@Override
	public Service duplexstreamDeliverServiceInit(List<String> configList) {
		return new MessageBindToChannelSubmitService<MessageBindToChannelSubmitProcessor>(
				GlobalVars.sessionPoolMap,
				GlobalVars.deliverMsgQueueMap, 
				GlobalVars.reserveMsgQueueMap, 
				GlobalVars.executorServiceMap,
				GlobalVars.duplexSessionConfigMap.get(configName), 
				false, 
				MessageBindToChannelSubmitProcessor.class,
				configList);
	}

	@Override
	public Service duplexstreamReserveDeliverServiceInit() {
		return duplexstreamReserveDeliverServiceInit(null);
	}

	@Override
	public Service duplexstreamReserveDeliverServiceInit(List<String> configList) {
		return new MessageBindToChannelSubmitService<MessageBindToChannelSubmitProcessor>(
				GlobalVars.sessionPoolMap,
				GlobalVars.reserveMsgQueueMap, 
				GlobalVars.reserveMsgQueueMap, 
				GlobalVars.executorServiceMap,
				GlobalVars.duplexSessionConfigMap.get(configName), 
				false, 
				MessageBindToChannelSubmitProcessor.class,
				configList);
	}

	@Override
	public Service upstreamReceiverMsgPluginManagerServiceInit() {
		return upstreamReceiverMsgPluginManagerServiceInit(null);
	}

	@Override
	public Service upstreamReceiverMsgPluginManagerServiceInit(
			List<String> configList) {
		return new DefaultReceivedMsgPluginManagerService(
				GlobalVars.upstreamSessionConfigMap.get(configName),
				GlobalVars.receiveMsgQueueMap, 
				GlobalVars.executorServiceMap, 
				GlobalVars.pluginManagerUtilMap,
				configList);
	}

	@Override
	public Service duplexstreamReceiverMsgPluginManagerServiceInit() {
		return duplexstreamReceiverMsgPluginManagerServiceInit(null);
	}

	@Override
	public Service duplexstreamReceiverMsgPluginManagerServiceInit(
			List<String> configList) {
		return new DefaultReceivedMsgPluginManagerService(
				GlobalVars.duplexSessionConfigMap.get(configName),
				GlobalVars.receiveMsgQueueMap, 
				GlobalVars.executorServiceMap, 
				GlobalVars.pluginManagerUtilMap,
				configList);
	}

	@Override
	public Service downstreamDeliverMsgPluginManagerServiceInit() {
		return downstreamDeliverMsgPluginManagerServiceInit(null);
	}

	@Override
	public Service downstreamDeliverMsgPluginManagerServiceInit(
			List<String> configList) {
		return new DefaultSubmitMsgPluginManagerService(
				GlobalVars.downstreamSessionConfigMap.get(configName), 
				GlobalVars.deliverMsgQueueMap,
				GlobalVars.executorServiceMap, 
				GlobalVars.pluginManagerUtilMap, 
				configList);
	}


	@Override
	public Service downstreamResponseMsgPluginManagerServiceInit() {
		return downstreamResponseMsgPluginManagerServiceInit(null);
	}

	@Override
	public Service downstreamResponseMsgPluginManagerServiceInit(
			List<String> configList) {
		return new DefaultReceivedMsgPluginManagerService(
				GlobalVars.downstreamSessionConfigMap.get(configName),
				GlobalVars.responseMsgQueueMap, 
				GlobalVars.executorServiceMap, 
				GlobalVars.pluginManagerUtilMap,
				configList);
	}

	@Override
	public Service duplexstreamResponseMsgPluginManagerServiceInit() {
		return duplexstreamResponseMsgPluginManagerServiceInit(null);
	}

	@Override
	public Service duplexstreamResponseMsgPluginManagerServiceInit(
			List<String> configList) {
		return new DefaultReceivedMsgPluginManagerService(
				GlobalVars.duplexSessionConfigMap.get(configName),
				GlobalVars.responseMsgQueueMap, 
				GlobalVars.executorServiceMap, 
				GlobalVars.pluginManagerUtilMap,
				configList);		
	}

	@Override
	public Service duplexstreamDeliverMsgPluginManagerServiceInit() {
		return duplexstreamDeliverMsgPluginManagerServiceInit(null);
	}

	@Override
	public Service duplexstreamDeliverMsgPluginManagerServiceInit(
			List<String> configList) {
		return new DefaultSubmitMsgPluginManagerService(
				GlobalVars.duplexSessionConfigMap.get(configName), 
				GlobalVars.deliverMsgQueueMap,
				GlobalVars.executorServiceMap, 
				GlobalVars.pluginManagerUtilMap, 
				configList);
	}
	@Override
	public void process() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
}
