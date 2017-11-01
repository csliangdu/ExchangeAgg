package com.zdx.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TickerFormatOkcoin {
	public static void format(String tickerinfo, TickerFormat x){
		JSONObject jsonObject = JSON.parseObject(tickerinfo);

		x.timestamp = jsonObject.getLong("date");
		JSONObject tickerJsonObject = JSON.parseObject(jsonObject.getString("ticker"));
		x.bid = tickerJsonObject.getDouble("buy");
		x.ask = tickerJsonObject.getDouble("sell");
		x.mid = (x.bid + x.ask) / 2;
		x.low = tickerJsonObject.getDouble("low");
		x.high = tickerJsonObject.getDouble("high");
		x.volume = tickerJsonObject.getDouble("vol");
		x.last_price = tickerJsonObject.getDouble("last");
		x.setExchangeType();
		x.setMidUSD();
	}
	public static void OkcoinTest(){
		TickerFormat tickerData = new TickerFormat();
		String tickerinfo = "{\"ticker\":{\"vol\":\"1723.00\",\"last\":\"309.05\",\"sell\":\"310.45\",\"buy\":\"308.00\",\"high\":\"318.10\",\"low\":\"308.00\"},\"date\":\"1509507834\"}";
		String path = "btc_usd";
		tickerData.exchangeName = "okcoin";
		String[] coinAB = path.split("_");
		tickerData.coinA = coinAB[0];
		tickerData.coinB = coinAB[1];
		format(tickerinfo, tickerData);
	}
}
