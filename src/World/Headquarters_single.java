package World;

import Warriors.Dragon_single;
import Warriors.Iceman_single;
import Warriors.Lion_single;
import Warriors.Ninja_single;
import Warriors.Warrior_single;
import Warriors.WarriorType;
import Warriors.Warrior_single;
import Warriors.Wolf_single;

public class Headquarters_single extends City_single{
	private int[] ProductionOrder;
	private int WarriorIndex = 0;
	private int ProductionID = 1; //Production ID starts from 1
	private int Party;
	private boolean HasNewArrival;
	
	public int getParty(){
		return Party;
	}
	
	public void addLifeElement(int e){
		this.LifeElement += e;
	}
	
	public void increaseLifeElement(int n){
		LifeElement += n;
	}
	public Headquarters_single(int[] order, int party, int initLifeElements, int cityID){
		super(cityID);
		ProductionOrder = new int[5];
		System.arraycopy(order, 0, ProductionOrder, 0, order.length);
		LifeElement = initLifeElements;
		this.Party = party;
		this.HasNewArrival = false;
	}
	public void showOrder(){
		for (int i:ProductionOrder){
			System.out.println(i);
		}
	}
	
	public void setNewArrival(){
		this.HasNewArrival = true;
	}
	
	public void clearNewArrival(){
		this.HasNewArrival = false;
	}
	
	public Warrior_single getNewArrivedWarrior(){
		if (this.Party == WorldProperty.RED)
			return this.BlueWarriorStation.get(this.BlueWarriorStation.size()-1);
		if (this.Party == WorldProperty.BLUE)
			return this.RedWarriorStation.get(this.RedWarriorStation.size()-1);
		System.err.println("Error in Headquarters: NO such party");
		return null;
	}
	
	public boolean checkNewArrival(){
		return this.HasNewArrival;
	}
	
	/*
	 * return true if enough elements,
	 * return false otherwise.
	 */
	public boolean rewardWarrior(Warrior_single Object,int RewardNumber){
		if (this.LifeElement >= RewardNumber){
			this.LifeElement -= RewardNumber;
			Object.HP += RewardNumber;
			return true;
		} else {
			return false;
		}
		
	}
	
	//TODO: Both Headquarters reward army.
	/*
	 * return true if enough elements,
	 * return false otherwise.
	 */
	public void rewardArmy(){
		//Red party reward.
		if (this.Party == WorldProperty.RED){
			for (int i=WorldProperty.NumberOfCity; i>=1; i--){
				City_single c = World_single.CityList.get(i);
				if (c.Status_AfterBattle == true && (!c.RedWarriorStation.isEmpty()) && c.BlueWarriorStation.isEmpty() ){
					Warrior_single w = c.RedWarriorStation.get(0);
					this.rewardWarrior(w, WorldProperty.rewardNumber);
				}
			}
		}
		//Blue party reward.
		if (this.Party == WorldProperty.BLUE){
			for (int i=1;i<=WorldProperty.NumberOfCity;i++){
				City_single c = World_single.CityList.get(i);
				if (c.Status_AfterBattle == true && c.RedWarriorStation.isEmpty() && (!c.BlueWarriorStation.isEmpty()) ){
					Warrior_single w = c.BlueWarriorStation.get(0);
					this.rewardWarrior(w, WorldProperty.rewardNumber);
				}
			}
		}
	}
	
	
	
	//DRAGON = 0;
	//NINJA = 1;
	//ICEMAN = 2;
	//LION = 3;
	//WOLF = 4;
        public static int whichBlueWarrior;
        public static int whichRedWarrior;
        public static boolean blueProductionSuccess = false;
        public static boolean redProductionSuccess = false;
        
	public boolean tryToProduceWarrior(){
                
		int warriorType = ProductionOrder[WarriorIndex];

                if(this.getParty() == WorldProperty.RED){
                    whichRedWarrior = World_single.warriorChosen;
                }
                if(this.getParty() == WorldProperty.BLUE){
                    whichBlueWarrior = warriorType;
                }
                
		int HPValue = WarriorType.HP_LIST[warriorType];
		if (this.LifeElement >= HPValue){
			LifeElement -= HPValue;
                        if(this.getParty() == WorldProperty.RED){
                            redProductionSuccess = true;
                        }
                        if(this.getParty() == WorldProperty.BLUE){
                            blueProductionSuccess = true;
                        }
			return produceWarrior(warriorType);
                        
		} else {
                        if(this.getParty() == WorldProperty.RED){
                            redProductionSuccess = false;
                        }       
                        if(this.getParty() == WorldProperty.BLUE){
                            blueProductionSuccess = false;
                        }
//			System.out.println("Not enought Life Element");
			return false;
		}
	}
	
	/**
	 * @param warriorType
	 * @return
	 */
        public static int whichWarrior;
	private boolean produceWarrior(int warriorType) {
                whichWarrior = warriorType;
		Warrior_single w;
		switch (warriorType){
			case 0:
                System.out.println(warriorType + " "+ this.Party + " "+ this.ProductionID);            
		System.out.println("boom1");
				w = new Dragon_single(warriorType, this.Party, this.ProductionID);
				break;
			case 1:
				w = new Ninja_single(warriorType, this.Party, this.ProductionID);
				break;
			case 2:
				w = new Iceman_single(warriorType, this.Party, this.ProductionID);
				break;
			case 3:
				w = new Lion_single(warriorType, this.Party, this.ProductionID);
				break;
			case 4:
				w = new Wolf_single(warriorType, this.Party, this.ProductionID);
				break;
			default:
				w = null;
				System.err.println("Error: No such Warrior Type in Headquarters.java");
				return false;
		}
		System.out.println("boom2");
		if (this.Party == WorldProperty.RED){
			RedWarriorStation.add(w);
		} else if (this.Party == WorldProperty.BLUE){
			BlueWarriorStation.add(w);
		} else {
			System.err.println("Error: Undefined warrior type.");
			return false;
		}
		this.WarriorIndex = (this.WarriorIndex + 1) % 5;
		this.ProductionID ++;
		
		return true;
	}
	
	/*
	 * return false for not occupied.
	 * return true for occupied.
	 */
	public boolean checkOccupied(){
		if (this.Party == WorldProperty.RED && this.BlueWarriorStation.size() >= 2){
			//003:10 blue headquarter was taken
			System.out.format("%s red headquarter was taken\n", World_single.WorldClock.getTime());
			return true;
		}
		if (this.Party == WorldProperty.BLUE && this.RedWarriorStation.size() >= 2){
			System.out.format("%s blue headquarter was taken\n", World_single.WorldClock.getTime());	
			return true;
		}
		return false;
	}
	
}
