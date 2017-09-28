package com.me.dn;

public class Main {
public static void main(String[] args) {
	DNHashMap<String, String> hashMap = new DNHashMap<String, String>();
	hashMap.put("1", "动脑11");
	hashMap.put("2", "动脑22");
	hashMap.put("3", "动脑33");
	
	System.out.println(hashMap.get("3"));
}
}
