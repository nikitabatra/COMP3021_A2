package Warriors;

public class ClassAttack_multi {
	public ClassAttack_multi(Warrior_multi subject, Warrior_multi object, int attackValue, boolean isBeatBack) {
		super();
		Subject = subject;
		Object = object;
		this.attackValue = attackValue;
		this.isBeatBack = isBeatBack;
	}
	public Warrior_multi Subject;
	public Warrior_multi Object;
	public int attackValue;
	boolean isBeatBack;
	
}
