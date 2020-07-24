package com.sourcetrace.eses.web.service;

public class Test {
	
	public static void main(String[] arr) {
		StringBuilder sb = new StringBuilder();
		
		for(Long i=20190130104531L;i<=1702;i++){
			sb.append("INSERT INTO dynamic_menu_field_map(`MENU_ID`, `FIELD_ID`, `ORDERZ`) VALUES (1, "+i+","+i+");\n");
		}
		System.out.println(sb.toString());
	}

}
