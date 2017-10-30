package com.zdx.storm;

public class AggConfig {
	public static String RocketMQNameServerHost = "182.92.150.57";
	public static String RocketMQNameServerPort = "9876";
	public static String RocketMQNameServerAddress = RocketMQNameServerPort + ":" + RocketMQNameServerHost;
	public static String webSocketServerIP = "182.92.150.57";
	public static String webSocketServerPort = "8001";
	public static String consumerGroup = "WufengTest1";
	public static String getRocketMQNameServerAddress(){
		return "182.92.150.57" + ":" + "9876";
	}
}
