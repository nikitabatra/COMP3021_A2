package Warriors;

public class Ninja_multi extends Warrior_multi {

	public Ninja_multi(int Type, int Party, int ProductionID) {
		super(Type, Party, ProductionID);
		// TODO Auto-generated constructor stub
	}
	
	public boolean getAttack(ClassAttack_multi myAttack){
		if (myAttack.Object != this){
			System.err.println("Error: Attack Subject and Object don't match");
		}
		
		int HPBeforeDeath = this.HP;
		
		// if it still survives, attack back
		if (removeHP(myAttack.attackValue)){
			//BeatBack
			//Ninja will not beat back
			/*
			if (myAttack.isBeatBack == false){
				ClassAttack myBeatback = new ClassAttack(this,myAttack.Subject,this.AttackValue/2,true);
				myAttack.Subject.getAttack(myBeatback);
			}
			*/
		}
		// if it dies, send a message to the attacker
		else {
			ClassDeathAnnounce_multi da = new ClassDeathAnnounce_multi(this, HPBeforeDeath);
			myAttack.Subject.getDeathAnnounce(da);
			die();
		}
		return true;
	}

}
