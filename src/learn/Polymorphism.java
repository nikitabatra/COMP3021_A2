package learn;

/*
 * Java class has polymorphism by itself.
 */

abstract class C{
	public void show(){
		System.out.println("Show from C");
	}
}

class D extends C{
	public void show(){
		super.show();
		System.out.println("Show from D");
	}
}
class E extends C{
	
}


public class Polymorphism {
	public static void main(String[] args){
		C c = new D();
		c.show();
		if (c instanceof C){
			System.out.println("abc");
		}
		if (c instanceof D){
			System.out.println("ddd");
		}

	}
}
