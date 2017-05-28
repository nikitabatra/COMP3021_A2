package Warriors;

public class ClassDeathAnnounce_multi_client {
	Warrior_multi_client DeadWarrior;
	int HPBeforeDeath;
	public ClassDeathAnnounce_multi_client(Warrior_multi_client deadWarrior, int lifeBeforeDeath) {
		super();
		DeadWarrior = deadWarrior;
		HPBeforeDeath = lifeBeforeDeath;
	}
	
}
