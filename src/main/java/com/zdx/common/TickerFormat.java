package com.zdx.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TickerFormat {
	public String exchangeName = "";
	public String exchangeType = "";
	public String coinA = "";
	public String coinB = "";		
	public Double mid = 0.0;
	public Double bid = 0.0;
	public Double ask = 0.0;
	public Double last_price = 0.0;
	public Double low = 0.0;
	public Double high = 0.0;
	public Double volume = 0.0;
	public Long timestamp;	
	public Double midUSD = 0.0;
	
	public TickerFormat(){
	}

	public String toJsonString(){
		return "{\"exchangeName\":\"" + exchangeName + 
				"\",\"exchangeType\":\"" + exchangeType +
				"\",\"coinA\":\"" + coinA + 
				"\",\"coinB\":\"" + coinB +
				"\",\"midUSD\":\"" + midUSD +
				"\",\"mid\":\"" + mid +
				"\",\"bid\":\"" + bid +
				"\",\"ask\":\"" + ask +
				"\",\"last_price\":\"" + last_price +
				"\",\"low\":\"" + low +
				"\",\"high\":\"" + high +
				"\",\"volume\":\"" + volume +
				"\",\"timestamp\":\"" + timestamp +
				"\"}";

	}

	public JSONObject toJson(){
		return JSON.parseObject(this.toJsonString());
	}

	public TickerFormat setExchangeType(){
		if (this.coinB.isEmpty()){
			return this;
		} else {
			if (CoinCashCommon.getCashSet().contains(this.coinB)){
				this.exchangeType = "coin2cash";
			} else {
				this.exchangeType = "coin2coin";
			}
			return this;
		}
	}
	
	public TickerFormat formatJsonString(String jsonString){
		JSONObject jsonObject = JSON.parseObject(jsonString);
		if (jsonObject.containsKey("exchangeName")){
			this.exchangeName = jsonObject.getString("exchangeName");
		}
		if (jsonObject.containsKey("exchangeType")){
			this.exchangeType = jsonObject.getString("exchangeType");
		}
		if (jsonObject.containsKey("coinA")){
			this.coinA = jsonObject.getString("coinA");
		}
		if (jsonObject.containsKey("coinB")){
			this.coinB = jsonObject.getString("coinB");
		}
		if (jsonObject.containsKey("midUSD")){
			this.midUSD = jsonObject.getDouble("midUSD");
		}
		if (jsonObject.containsKey("mid")){
			this.mid = jsonObject.getDouble("mid");
		}
		if (jsonObject.containsKey("bid")){
			this.bid = jsonObject.getDouble("bid");
		}
		if (jsonObject.containsKey("ask")){
			this.ask = jsonObject.getDouble("ask");
		}
		if (jsonObject.containsKey("last_price")){
			this.last_price = jsonObject.getDouble("last_price");
		}
		if (jsonObject.containsKey("low")){
			this.low = jsonObject.getDouble("low");
		}
		if (jsonObject.containsKey("high")){
			this.high = jsonObject.getDouble("high");
		}
		if (jsonObject.containsKey("volume")){
			this.volume = jsonObject.getDouble("volume");
		}
		if (jsonObject.containsKey("timestamp")){
			this.timestamp = jsonObject.getLong("timestamp");
		}
		return this;
	}
	
	public TickerFormat setMidUSD(){
		System.out.println(this.toJsonString());
		if (CoinCashCommon.getCashSet().contains(this.coinB)){
			CashExchange ce = new CashExchange();
			this.midUSD = ce.toUSD(this.coinB, this.mid);//coin 2 cash
		} else {
			this.midUSD = this.mid;//coin 2 coin
		}
		return this;
	}
}
