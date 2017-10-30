package com.zdx.test;
import java.util.HashMap;
import java.util.Map;

public class Test {
	public static void main(String[] args) {
		final Map<String, String> pathMap = new HashMap<String, String>();
		pathMap.put("api/v1/ticker.do?symbol=btc_usd", "btc_usd");
		pathMap.put("api/v1/ticker.do?symbol=eth_usd", "eth_usd");
		pathMap.put("api/v1/ticker.do?symbol=ltc_usd", "ltc_usd");
		pathMap.put("api/v1/ticker.do?symbol=btc_cny", "btc_cny");
		pathMap.put("api/v1/ticker.do?symbol=eth_cny", "eth_cny");

		String path = "api/v1/ticker.do?symbol=btc_usd";

		System.out.println("=============================================");
		System.out.println(path);
		System.out.println(pathMap.get(path));
		System.out.println(pathMap.toString());
		System.out.println("=============================================");
		String Path = "ltc_usd";
		System.out.println(Path.split("_")[1]);
	}
}
