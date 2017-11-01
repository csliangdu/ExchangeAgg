package com.zdx.common;

import java.util.Comparator;

public class SortByPrice implements Comparator<Object> {
	public int compare(Object o1, Object o2) {
		TickerFormat s1 = (TickerFormat) o1;
		TickerFormat s2 = (TickerFormat) o2;
		return s1.mid.compareTo(s2.mid);
	}
}
