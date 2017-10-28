package com.zdx.rocketmq;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;


import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.java_websocket.drafts.Draft_6455;

/**
 * This example shows how to subscribe and consume messages using providing {@link DefaultMQPushConsumer}.
 */
public class TestRocketMQConsumer {

	public static String serverUrl = "localhost:9876";

	public static String webSocketServerIP = "182.92.150.57";
	public static String webSocketServerPort = "8001";

	public static void main(String[] args) throws InterruptedException, MQClientException, URISyntaxException {

		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("WufengTest1");

		consumer.setNamesrvAddr(serverUrl);

		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

		consumer.subscribe("TopicTest", "*");

		final WebSocketLocalClient wsClient = new WebSocketLocalClient( new URI( "ws://" + webSocketServerIP + ":" + webSocketServerPort), new Draft_6455() );
		try {		 
			wsClient.connectBlocking();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		consumer.registerMessageListener(new MessageListenerConcurrently() {
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
					ConsumeConcurrentlyContext context) {
				for (int i = 0; i < msgs.size(); i++){
					MessageExt msg = msgs.get(i);
					String body = new String(msg.getBody());
					System.out.println("Body = " + body);					
					wsClient.send("rocketmq" + body);
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});

		/*
		 *  Launch the consumer instance.
		 */
		consumer.start();

		System.out.println("Consumer Started...");
	}
}