package Warriors;

public class ClassAttack_multi_client {
	public ClassAttack_multi_client(Warrior_multi_client subject, Warrior_multi_client object, int attackValue, boolean isBeatBack) {
		super();
		Subject = subject;
		Object = object;
		this.attackValue = attackValue;
		this.isBeatBack = isBeatBack;
	}
	public Warrior_multi_client Subject;
	public Warrior_multi_client Object;
	public int attackValue;
	boolean isBeatBack;
	
}
