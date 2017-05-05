package Warriors;

public class WarriorType {
	public static final int DRAGON = 0;
	public static final int NINJA = 1;
	public static final int ICEMAN = 2;
	public static final int LION = 3;
	public static final int WOLF = 4;
	public static final String[] WarriorNames={"dragon","ninja","iceman","lion","wolf"};
	//Arributes of Warriors
	public static int[] HP_LIST = {10,20,50,50,30};
	public static int[] ATTACK_LIST = {20,50,50,50,50};
        // N: Added numLE and NumT
	
	public int getHPByIndex(int i){
		return HP_LIST[i];
	}
	
	public int getAttackByIndex(int i){
		return ATTACK_LIST[i];
	}
}
