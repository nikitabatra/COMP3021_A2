package Warriors;

import World.*;

import javafxapplication1.SampleController_single;

public abstract class Warrior_single {
	public int HP;
	public int AttackValue;
	public int Type;
	public int Party;
	public boolean Dead;
	public int ProductionID;
	public int NumberOfKilledWarrior;
	public int MovedDistance;
	public String WarriorNameCard;
	public int Location;
	
        public static SampleController_single ssc;
        
	public Warrior_single(int Type, int Party,int ProductionID){
		this.HP = WarriorType.HP_LIST[Type];
		this.AttackValue = WarriorType.ATTACK_LIST[Type];
		this.Type = Type;
		this.Party = Party;
		this.Dead = false;
		this.ProductionID = ProductionID;
		this.NumberOfKilledWarrior = 0;
		this.MovedDistance = 0;
		this.WarriorNameCard = WorldProperty.PartyNames[this.Party]+" "+
				WarriorType.WarriorNames[this.Type]+" " + this.ProductionID;
		
		if (this.Party == WorldProperty.RED){
			Location = 0;
		}
		else if (this.Party == WorldProperty.BLUE){
			Location = WorldProperty.NumberOfCity + 1;
		}
		else {
			System.err.println("Error in Warrior: Undefined Party");
		}
		
		System.out.println(World_single.WorldClock.getTime()+" "
				+this.WarriorNameCard+" born");
	}
	
	public boolean move(){
		MovedDistance++;
		if (this.Party == WorldProperty.RED && this.Location < WorldProperty.NumberOfCity + 1  ){
			City_single CurrentCity =  World_single.CityList.get(Location);
			City_single NextCity = World_single.CityList.get(Location+1);
			NextCity.addWarrior(this);
			CurrentCity.removeWarrior(this);
			Location++;
			if (Location == WorldProperty.NumberOfCity + 1){
				Headquarters_single RedHeadquarters = (Headquarters_single) World_single.CityList.get(Location);
				RedHeadquarters.setNewArrival();
			}
			
		}
		if (this.Party == WorldProperty.BLUE && this.Location > 0){
			City_single CurrentCity = World_single.CityList.get(Location);
			City_single NextCity = World_single.CityList.get(Location-1);
			NextCity.addWarrior(this);
			CurrentCity.removeWarrior(this);
			Location--;
			if (Location == 0){
				Headquarters_single BlueHeadquarters = (Headquarters_single) World_single.CityList.get(Location);
				BlueHeadquarters.setNewArrival();
			}
		}
		return true;
	}

	public boolean attack(Warrior_single Object){
		
		//000:40 red iceman 1 attacked blue lion 1 in city 1 with 20 elements and force 30
		System.out.format("%s %s attacked %s in city %d with %d elements and force %d\n", 
				World_single.WorldClock.getTime(),this.WarriorNameCard, Object.WarriorNameCard, this.Location,
				this.HP, this.AttackValue);
		
		//Give an attack to Object
		ClassAttack_single myAttack = new ClassAttack_single(this,Object,this.AttackValue,false);
		Object.getAttack(myAttack);
		
		//record this battle.
		if (this.Dead == false && Object.Dead == false){
			World_single.CityList.get(this.Location).PartyOfLastRoundWinner = -1; 
		}
		
		return true;
	}
	
	
	//Modify this and Wolf.java
        public static Warrior_single deadWarrior = null;
        public static int deadLocation;
	public boolean getDeathAnnounce(ClassDeathAnnounce_single da){
		//Declare enemy's death note
		System.out.format("%s %s was killed in city %d\n", 
				World_single.WorldClock.getTime(),da.DeadWarrior.WarriorNameCard, da.DeadWarrior.Location);
		deadWarrior = da.DeadWarrior;
                deadLocation = da.DeadWarrior.Location;
		//Well, this is needed for the correct order of output.
		//One more condition: this should be an active attack.
		if (this instanceof Dragon_single && this.Party == World_single.CityList.get(this.Location).activeAttackParty){
			((Dragon_single)this).cheers();
		}
		
		//Earn Life Element Declaration.
		//Declare here, realized in City.payTribute().
		System.out.format("%s %s earned %d elements for his headquarter\n", 
				World_single.WorldClock.getTime(), this.WarriorNameCard, World_single.CityList.get(this.Location).LifeElement);
		
		//Only Count for active killings
		if (World_single.CityList.get(this.Location).activeAttackParty == this.Party){
			//I killed one more warrior.
			this.NumberOfKilledWarrior++;
		}
		
		//Get LIONS' HP
		if (da.DeadWarrior.Type == WarriorType.LION){
			this.HP += da.HPBeforeDeath;
		}
		
		//Now I have right to change flag of my city.
		tryToChangeFlag();
		
		return true;
	}

	/**
	 * Try to change the flag of current city.
	 */
	protected void tryToChangeFlag() {
		City_single myCity = World_single.CityList.get(this.Location);
		if (myCity.PartyOfLastRoundWinner == this.Party){
			if (myCity.Flag != this.Party){
				//Change Flag Declaration.
				//004:40 blue flag raised in city 4
				System.out.format("%s %s flag raised in city %d\n", 
						World_single.WorldClock.getTime(), WorldProperty.PartyNames[this.Party],this.Location);
			//if(WorldProperty.PartyNames[this.Party] == "red") ssc.redRaised = true;
                        //if(WorldProperty.PartyNames[this.Party] == "blue") ssc.blueRaised = true;
                        }
			//Keep the flag.
			myCity.Flag = this.Party;
		}
		myCity.PartyOfLastRoundWinner = this.Party;
	}
	
	
	public boolean getAttack(ClassAttack_single myAttack){
		if (myAttack.Object != this){
			System.err.println("Error: Attack Subject and Object don't match");
		}
		
		int HPBeforeDeath = this.HP;
		
		// if it still survives, attack back
		if (removeHP(myAttack.attackValue)){
			//BeatBack
			if (myAttack.isBeatBack == false){
				//Beat back Declaration.
				//001:40 blue dragon 2 fought back against red lion 2 in city 1
				System.out.format("%s %s fought back against %s in city %d\n", 
						World_single.WorldClock.getTime(), this.WarriorNameCard, 
						myAttack.Subject.WarriorNameCard, this.Location);
				
				
				ClassAttack_single myBeatback = new ClassAttack_single(this,myAttack.Subject,this.AttackValue/2,true);
				myAttack.Subject.getAttack(myBeatback);
			}
		}
		// if it dies, send a message to the attacker
		else {
			ClassDeathAnnounce_single da = new ClassDeathAnnounce_single(this, HPBeforeDeath);
			die();
			myAttack.Subject.getDeathAnnounce(da);
		}
		
		return true;
	}
	

	//return true if survive, false if die
	public boolean removeHP(int r){
		HP -= r;
		if (HP <= 0){
			return false;
		}
		return true;
	}
	
	//return true is die successfully
	public boolean die(){
		//Death Declaration.
		//001:40 red lion 2 was killed in city 1
		//Moved to getDeathAnnounce.

		
		Dead = true;
		
		City_single c = World_single.CityList.get(this.Location);
		c.removeWarrior(this);
		
		return true;
	}
}
