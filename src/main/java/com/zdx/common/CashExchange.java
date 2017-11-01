package com.zdx.common;

import java.util.HashMap;
import java.util.Map;

public class CashExchange {
	public Map<String, Double> priceToUSD = new HashMap<String, Double>();
	public CashExchange(){
		priceToUSD.put("cny", 0.1512);
		priceToUSD.put("usd", 1.0);
	}
	public Double toUSD(String cashType, Double amount){
		System.out.println(cashType);
		System.out.println(priceToUSD.toString());
		if (priceToUSD.containsKey(cashType)){
			return priceToUSD.get(cashType) * amount;
		} else {
			return 0.0;
		}
	}

}
