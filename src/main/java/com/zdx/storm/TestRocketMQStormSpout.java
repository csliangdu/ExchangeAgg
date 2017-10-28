package com.zdx.storm;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Values;
//import backtype.storm.topology.IRichSpout;

public class TestRocketMQStormSpout extends BaseRichSpout implements MessageListenerConcurrently{  
	private static final long serialVersionUID = -3085994102089532269L;   
	private SpoutOutputCollector collector;  
	private transient DefaultMQPushConsumer consumer;  
	private static final Logger logger = LogManager.getLogger(TestRocketMQStormSpout.class);
	
	@SuppressWarnings("rawtypes")  
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) { 
		
		logger.info("init DefaultMQPushConsumer");  
		consumer = new DefaultMQPushConsumer(RaceConfig.MetaConsumerGroup);  
		//   consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);  
		consumer.setNamesrvAddr("ip:port");
		try {  
			consumer.subscribe(RaceConfig.MqTmallTradeTopic, "*");  
			consumer.subscribe(RaceConfig.MqTaobaoTradeTopic, "*");  
			consumer.subscribe(RaceConfig.MqPayTopic, "*");  
		} catch (MQClientException e) {  
			e.printStackTrace();  
		}  
		consumer.registerMessageListener(this);  
		try {  
			consumer.start();  
		} catch (MQClientException e) {  
			e.printStackTrace();  
		}  


		logger.info("Consumer Started.");  
		this.collector = collector;  
	}  

	@Override  
	public void nextTuple() {  
		//do nothing  
	}  

	@Override  
	public void declareOutputFields(OutputFieldsDeclarer declarer) {  
		//...  
	}  

	@Override  
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,ConsumeConcurrentlyContext context) {  


		for (MessageExt msg : msgs) {  
			byte[] body = msg.getBody();  
			if (body.length == 2 && body[0] == 0 && body[1] == 0) {
				logger.info("Got the end signal");  
				collector.emit("stop",new Values("stop"));  
				continue;  
			}  
			if (msg.getTopic().equals(RaceConfig.MqPayTopic)) {  
				return doPayTopic(body);  
			}else if (msg.getTopic().equals(RaceConfig.MqTaobaoTradeTopic)) {  
				putTaobaoTradeToTair(body);  
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;  
			} else if (msg.getTopic().equals(RaceConfig.MqTmallTradeTopic)) {  
				putTmallTradeToTair(body);  
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;  
			}else {  
				return ConsumeConcurrentlyStatus.RECONSUME_LATER;  
			} 
		}  
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;  
	}
}  