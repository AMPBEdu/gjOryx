package com.oryxhatesjava.util;

public final class HexValueParser {
	public static int parseInt(String string) {
		String sub;
		try {
			sub = string.substring(0, 2);
		} catch (Exception e) {
			return Integer.parseInt(string);
		}
		
		if (sub.equals("0x") || sub.equals("0X")) {
			return Integer.parseInt(string.substring(2), 16);
		} else {
			return Integer.parseInt(string);
		}
	}
}
