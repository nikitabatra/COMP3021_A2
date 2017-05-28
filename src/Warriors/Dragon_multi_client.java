package Warriors;

import World.*;

public class Dragon_multi_client extends Warrior_multi_client {

	boolean hasCheered;
	
	public Dragon_multi_client(int Type, int Party, int ProductionID) {
		super(Type, Party, ProductionID);
		// TODO Auto-generated constructor stub
	}
	
	public void cheers(){
		if (this.hasCheered) return;
		this.hasCheered = true;
		//003:40 blue dragon 2 yelled in city 4
		System.out.format("%s %s yelled in city %d\n", 
				World_multi.WorldClock.getTime(), this.WarriorNameCard, this.Location);
	}
	
	public boolean attack(Warrior_multi_client Object){
		hasCheered = false;
		super.attack(Object);
		if (this.Dead == false){
			cheers();
		}
		return true;
	}
	

}
