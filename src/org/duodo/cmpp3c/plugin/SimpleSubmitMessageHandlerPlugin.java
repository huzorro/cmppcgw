/**
 * 
 */
package org.duodo.cmpp3c.plugin;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import net.xeoh.plugins.base.annotations.meta.Author;
import net.xeoh.plugins.base.annotations.meta.Version;

import org.duodo.cmpp3c.message.CmppSubmitRequestMessage;
import org.duodo.netty3ext.plugin.SubmitMessageHandlerPlugin;
import org.duodo.netty3ext.plugin.SubmitMessageServicePlugin;

/**
 * @author huzorro(huzorro@gmail.com)
 *
 */
@Author(name = "huzorro(huzorro@gmail.com)")
@Version(version = 10000)
@PluginImplementation
public class SimpleSubmitMessageHandlerPlugin implements
		SubmitMessageHandlerPlugin {

	@InjectPlugin
	public SubmitMessageServicePlugin submitMessageServicePlugin;
	@Override
	public void submit() throws Exception {
		for (int i = 0; i < 1; i++) {
				CmppSubmitRequestMessage cmppSubmitRequestMessage = new CmppSubmitRequestMessage();
				cmppSubmitRequestMessage.setChannelIds("901077");
				cmppSubmitRequestMessage.setMsgContent("中文");
				cmppSubmitRequestMessage.setRegisteredDelivery((short)0);
				submitMessageServicePlugin.submit(cmppSubmitRequestMessage);
		}
	}

}
