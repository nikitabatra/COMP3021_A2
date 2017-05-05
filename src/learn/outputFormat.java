package learn;

import java.text.DecimalFormat;

public class outputFormat {
	
   static public void customFormat(String pattern, double value ) {
	      DecimalFormat myFormatter = new DecimalFormat(pattern);
	      String output = myFormatter.format(value);
	      System.out.println(value + "  " + pattern + "  " + output);
   }
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	     
	      customFormat("000", 1);
	       

	}

}
