package World;

import java.text.DecimalFormat;

public class Clock {
	private int hour;
	private int minute;
	public Clock(){
		hour = 0;
		minute = 0;
	}
	
	//TODO: Add customFormat to hint
        static public String customFormat(String pattern, double value ) {
                   DecimalFormat myFormatter = new DecimalFormat(pattern);
                   String output = myFormatter.format(value);
                   return output;
        }
	
	public String getTime(){
		return customFormat("000",hour) + ":" + customFormat("00",minute);
	}
	
	public int getMinute(){
		return minute;
	}
	
	public void increase(){
		minute += 10;
		hour += minute / 60;
		minute %= 60;
	}
}
