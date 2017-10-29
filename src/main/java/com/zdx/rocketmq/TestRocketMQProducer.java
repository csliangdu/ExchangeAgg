package com.zdx.rocketmq;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.parallec.core.ParallecResponseHandler;
import io.parallec.core.ParallelClient;
import io.parallec.core.ParallelTaskBuilder;
import io.parallec.core.RequestProtocol;
import io.parallec.core.ResponseOnSingleTask;

public class TestRocketMQProducer {
	public static String serverUrl = "localhost:9876";

	public static void main(String[] args) throws MQClientException, InterruptedException{
		ParallelClient pc = new ParallelClient();

		final HashMap<String, Object> responseContext = new HashMap<String, Object>();

		DefaultMQProducer producer = new DefaultMQProducer("WufengTest1");
		producer.setNamesrvAddr(serverUrl);		
		producer.start();
		responseContext.put("producer", producer);

		List<String> targetHosts = new ArrayList<String>(Arrays.asList("www.okcoin.com",
				"www.okcoin.cn"));
		List<List<String>> replaceLists = new ArrayList<List<String>>();

		replaceLists.add(Arrays.asList("api/v1/ticker.do?symbol=btc_usd", 
				"api/v1/ticker.do?symbol=eth_usd", 
				"api/v1/ticker.do?symbol=ltc_usd"));
		replaceLists.add(Arrays.asList("api/v1/ticker.do?symbol=btc_cny",
				"api/v1/ticker.do?symbol=eth_cny"));
		
		final Map<String, String> hostMap = new HashMap<String, String>();
		hostMap.put("www.okcoin.com", "okcoin.com");
		hostMap.put("www.okcoin.cn", "okcoin.cn");
		final Map<String, String> pathMap = new HashMap<String, String>();
		pathMap.put("api/v1/ticker.do?symbol=btc_usd", "btc_usd");
		pathMap.put("api/v1/ticker.do?symbol=eth_usd", "eth_usd");
		pathMap.put("api/v1/ticker.do?symbol=ltc_usd", "ltc_usd");
		pathMap.put("api/v1/ticker.do?symbol=btc_cny", "btc_cny");
		pathMap.put("api/v1/ticker.do?symbol=eth_cny", "eth_cny");
		
		/*
		 .setReplaceVarMapToSingleTargetSingleVar("JOB_ID", Arrays.asList("api/v1/ticker.do?symbol=btc_usd", 
			"api/v1/ticker.do?symbol=eth_usd",
			"api/v1/ticker.do?symbol=ltc_usd"), "www.okcoin.com")
		 */
		ParallelTaskBuilder ptb = 
				pc.prepareHttpGet("/$JOB_ID")
				.setProtocol(RequestProtocol.HTTPS)
				.setHttpPort(443)
				.setReplaceVarMapToMultipleTarget("JOB_ID", replaceLists, targetHosts)
				.setResponseContext(responseContext);
		boolean f1 = true;
		while (f1){
			ptb.execute(new ParallecResponseHandler(){
				public void onCompleted(ResponseOnSingleTask res, Map<String, Object> responseContext) {
					Message msg = new Message();
					msg.setTopic("tiker");
					msg.setTags("TagA");
					
					String Host = res.getRequest().getHostUniform(); //www.okcoin.com
					String path = res.getRequest().getResourcePath(); ///api/v1/ticker.do?symbol=btc_usd
					JSONObject jsonObject = JSON.parseObject(res.getResponseContent());
					jsonObject.put("host", hostMap.get(Host));
					jsonObject.put("path", pathMap.get(path));
					msg.setBody(jsonObject.toJSONString().getBytes());
					System.out.println("Res = " + jsonObject.toJSONString());					
					System.out.println("Res = " + res.getResponseContent());
					
					try {
						DefaultMQProducer producer = (DefaultMQProducer)responseContext.get("producer");
						producer.sendOneway(msg);
					} catch (MQClientException e) {
						System.out.println("Exception1 ==================================================================");
						e.printStackTrace();
					} catch (RemotingException e) {
						System.out.println("Exception2 ==================================================================");
						e.printStackTrace();
					} catch (InterruptedException e) {
						System.out.println("Exception3 ==================================================================");
						e.printStackTrace();				
					} 
				}
			});
			System.out.println(" ===========================Done==============================" + pc.getRunningJobCount());
			f1 = (pc.getRunningJobCount() == 0);
		}
		producer.shutdown();
	}

}
