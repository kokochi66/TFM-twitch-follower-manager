package com.kokochi.samp.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
	
	public Date toDate(String at) throws Exception {
		
		SimpleDateFormat transformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
		Date time = transformat.parse(at);
		
		return time;
	}

}
