package learn;

class A{
	public A(){
		System.out.println("Constructor of A");
	}
	public A(int i){
		System.out.println("Integer Constructor of A");
	}
}

class B extends A{
	public B(){
		super(5);
		System.out.println("Constructor of B");
	}
}


public class InherentConstructor {

	public static void main(String[] args) {
		A a = new B();

	}

}
