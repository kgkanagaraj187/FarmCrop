package com.sourcetrace.eses.adapter.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sourcetrace.eses.util.DateUtil;

public class DateTest 
{
	
	public static void main(String args[]) throws ParseException
	{
		
		String input="Wed, 03 Jan 2018 04:10:50 GMT";
	    SimpleDateFormat formatter = new SimpleDateFormat(DateUtil.WEATHER_DATE_FORMAT_TIME);
	    SimpleDateFormat formatter1 = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);
	    Date date = formatter.parse(input);
	    String strDate= formatter1.format(date);  
	    Date date1=formatter1.parse(strDate);
	    System.out.println(strDate);  
	}

}
