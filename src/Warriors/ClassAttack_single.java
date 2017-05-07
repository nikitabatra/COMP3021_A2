package Warriors;

public class ClassAttack_single {
	public ClassAttack_single(Warrior_single subject, Warrior_single object, int attackValue, boolean isBeatBack) {
		super();
		Subject = subject;
		Object = object;
		this.attackValue = attackValue;
		this.isBeatBack = isBeatBack;
	}
	public Warrior_single Subject;
	public Warrior_single Object;
	public int attackValue;
	boolean isBeatBack;
	
}
