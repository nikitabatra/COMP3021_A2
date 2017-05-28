package Warriors;

import World.World_multi_client;

public class Wolf_multi_client extends Warrior_multi_client {

	public Wolf_multi_client(int Type, int Party, int ProductionID) {
		super(Type, Party, ProductionID);
		// TODO Auto-generated constructor stub
	}
	
	public boolean getDeathAnnounce(ClassDeathAnnounce_multi_client da){
	
		//Declare enemy's death note
		System.out.format("%s %s was killed in city %d\n", 
				World_multi_client.WorldClock.getTime(),da.DeadWarrior.WarriorNameCard, da.DeadWarrior.Location);
		
		//Earn Life Element Declaration.
		//Declare here, realized in City.payTribute().
		System.out.format("%s %s earned %d elements for his headquarter\n", 
				World_multi_client.WorldClock.getTime(), this.WarriorNameCard, World_multi_client.CityList.get(this.Location).LifeElement);
		
		//Only count for active killings
		if (World_multi_client.CityList.get(this.Location).activeAttackParty == this.Party){
			//I killed one more warrior.
			this.NumberOfKilledWarrior++;
		
			//Wolf get doubled when he kills two
			if (NumberOfKilledWarrior %2 == 0){
				this.HP *= 2;
				this.AttackValue *= 2;
			}	
		}
		
		
		
		//Get LIONS' HP
		if (da.DeadWarrior.Type == WarriorType.LION){
			this.HP += da.HPBeforeDeath;
		}
		
		//Now I have right to change flag of my city.
		tryToChangeFlag();
		
		return true;
	}
}
