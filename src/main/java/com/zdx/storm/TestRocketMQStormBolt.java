package com.zdx.storm;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.drafts.Draft_6455;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.jstorm.metric.MetaType;
import com.zdx.rocketmq.WebSocketLocalClient;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;


public class TestRocketMQStormBolt implements IRichBolt {

	private static final long serialVersionUID = 2495121976857546346L;
	private static final Logger logger = LoggerFactory.getLogger(TestRocketMQStormBolt.class);
	protected OutputCollector collector;
	java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");  
	Map<String, Map<String, Double>> coinPrices = new HashMap<String, Map<String, Double>>();
	WebSocketLocalClient wsClient = null;

	public TestRocketMQStormBolt(){


	}

	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		System.out.println("Exception1 ==================================================================");
		System.out.println("Exception1 ==================================================================");
		System.out.println("Exception1 ==================================================================");
		System.out.println("Exception1 ==================================================================");
		System.out.println("Exception1 ==================================================================");
		System.out.println("Exception1 ==================================================================");
		System.out.println("Exception1 ==================================================================");
		System.out.println("Exception1 ==================================================================");
		System.out.println("Exception1 ==================================================================");
		WebSocketLocalClient wsClient = null;
		try {
			wsClient = new WebSocketLocalClient( new URI( "ws://" + AggConfig.webSocketServerIP + ":" + AggConfig.webSocketServerPort), new Draft_6455() );
			wsClient.connectBlocking();
			this.wsClient = wsClient;
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.collector = collector;
	}

	@SuppressWarnings("null")
	public void execute(Tuple tuple) {
		// TODO Auto-generated method stub
		//MetaType metaTuple = (MetaType)tuple.getValue(0);
		System.out.println(tuple.getValue(0).toString());
		System.out.println(tuple.getValue(1).toString());
		System.out.println("Exception11 ==================================================================");
		System.out.println("Exception11 ==================================================================");
		System.out.println("Exception11 ==================================================================");
		System.out.println("Exception11 ==================================================================");
		System.out.println("Exception11 ==================================================================");
		String tickerType = tuple.getValue(0).toString();
		String tickerInfo = tuple.getValue(1).toString();
		JSONObject jsonObject = JSON.parseObject(tickerInfo);
		String host = jsonObject.getString("host");
		JSONObject tickerJson = JSON.parseObject(jsonObject.getString("ticker"));
		Double coinPrice = 0.0;

		System.out.println(coinPrices.toString());
		if (tickerJson.containsKey("buy") && tickerJson.containsKey("sell")){
			coinPrice = tickerJson.getDouble("buy") + tickerJson.getDouble("sell");
			System.out.println(new java.text.DecimalFormat("#.00").format(coinPrice / 2));

			Map<String, Double> cp = new HashMap<String, Double>();
			if (coinPrices.containsKey(tickerType) ){
				cp = coinPrices.get(tickerType);
			}
			cp.put(host, coinPrice);	
			coinPrices.put(tickerType, cp);
			if (this.wsClient != null){
				wsClient.send("prices========" + coinPrices);
			}
		} else {
			System.out.println("buy/sell dose not exists, they may use other words");
		}


		System.out.println(tickerInfo);
		System.out.println(host);
		System.out.println(jsonObject.getString("ticker"));
		/*
		 */
		System.out.println(coinPrices.toString());
		System.out.println("Exception11 ==================================================================");
		System.out.println("Exception11 ==================================================================");
		System.out.println("Exception11 ==================================================================");
		System.out.println("Exception11 ==================================================================");

		/*try {

			//logger.info("Messages:" + metaTuple.toString());
		} catch (Exception e) {
			collector.fail(tuple);
			return ;
			//throw new FailedException(e);
		}*/
		collector.ack(tuple);
	}

	public void cleanup() {
		// TODO Auto-generated method stub
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}
}