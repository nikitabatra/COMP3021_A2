package Warriors;

public class ClassAttack {
	public ClassAttack(Warrior subject, Warrior object, int attackValue, boolean isBeatBack) {
		super();
		Subject = subject;
		Object = object;
		this.attackValue = attackValue;
		this.isBeatBack = isBeatBack;
	}
	public Warrior Subject;
	public Warrior Object;
	public int attackValue;
	boolean isBeatBack;
	
}
