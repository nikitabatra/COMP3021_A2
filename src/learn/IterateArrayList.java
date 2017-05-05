package learn;

import java.util.ArrayList;

public class IterateArrayList {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Integer> l = new ArrayList<Integer>();
		l.add(Integer.valueOf(0));
		l.add(Integer.valueOf(1));
		l.add(Integer.valueOf(2));
		
		for (int index=l.size()-1;index>=0;index--){
			Integer tmp = l.get(index);
			System.out.println(tmp);
			l.remove(tmp);
		}
		
		
	}

}
