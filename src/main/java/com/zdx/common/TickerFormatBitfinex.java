package com.zdx.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TickerFormatBitfinex {
	public static void format(String tickerinfo, TickerFormat x){
		JSONObject jsonObject = JSON.parseObject(tickerinfo);
		String tmp = jsonObject.getString("timestamp");
		x.timestamp = Long.parseLong(tmp.substring(0, tmp.indexOf(".")));
		x.bid = jsonObject.getDouble("bid");
		x.ask = jsonObject.getDouble("ask");
		x.mid = jsonObject.getDouble("mid");
		x.low = jsonObject.getDouble("low");
		x.high = jsonObject.getDouble("high");
		x.volume = jsonObject.getDouble("volume");
		x.last_price = jsonObject.getDouble("last_price");
		x.setExchangeType();
		x.setMidUSD();
	}
	public static void BitfinexTest(){
		TickerFormat tickerData = new TickerFormat();
		String tickerinfo = "{\"mid\":\"0.0356425\",\"bid\":\"0.035605\",\"ask\":\"0.03568\",\"last_price\":\"0.035601\",\"low\":\"0.035438\",\"high\":\"0.038908\",\"volume\":\"6868.37723064\",\"timestamp\":\"1509509761.7267964\"}";
		String path = "eth_btc";
		tickerData.exchangeName = "bitfinex";
		String[] coinAB = path.split("_");
		tickerData.coinA = coinAB[0];
		tickerData.coinB = coinAB[1];
		format(tickerinfo, tickerData);
	}
	public static void main(String[] args){
		BitfinexTest();
	}

}
