package com.zdx.storm;

public class AggConfig {
	public static String RocketMQNameServerHost = "localhost";
	public static String RocketMQNameServerPort = "9876";
	public static String RocketMQNameServerAddress = RocketMQNameServerPort + ":" + RocketMQNameServerHost;

	public static String consumerGroup = "WufengTest1";
	public static String getRocketMQNameServerAddress(){
		return RocketMQNameServerPort + ":" + RocketMQNameServerHost;
	}
}
