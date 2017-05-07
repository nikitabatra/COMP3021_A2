package World;

import java.util.ArrayList;

import Warriors.Warrior_single;

public class City_single {
	public int CityID;
	public int LifeElement;
	public ArrayList<Warrior_single> RedWarriorStation, BlueWarriorStation;
	public int Flag;
	public int PartyOfLastRoundWinner;
	public boolean Status_AfterBattle;
	public int activeAttackParty;
	
	public City_single(int id){
		CityID = id;
		LifeElement = 0;
		Flag = -1;
		RedWarriorStation = new ArrayList<Warrior_single>();
		BlueWarriorStation = new ArrayList<Warrior_single>();
		PartyOfLastRoundWinner = -1;
		Status_AfterBattle = false;
		activeAttackParty = -1;
	}

	public void produceLifeElement(){
		LifeElement += 10;
	}
	
	public int popLifeElements(){
		int temp = LifeElement;
		this.LifeElement = 0;
		return  temp;
	}
	
	public boolean addWarrior(Warrior_single w){
		switch (w.Party){
			case WorldProperty.RED:
				RedWarriorStation.add(w);
				return true;
			case WorldProperty.BLUE:
				BlueWarriorStation.add(w);
				return true;
			default:
				return false;
		}
	}
	
	public boolean removeWarrior(Warrior_single w){
		if (w.Party == WorldProperty.RED){
			RedWarriorStation.remove(w);
		}
		else if (w.Party == WorldProperty.BLUE){
			BlueWarriorStation.remove(w);
		} else {
			System.err.println("Remove Warrior Error: No such party.");
		}
		
		return true;
	}
	
	public static boolean checkIfBattle = false;
	public boolean organizeBattle(){
		//At least one station is empty, that means no battle.
		if (this.RedWarriorStation.isEmpty() || this.BlueWarriorStation.isEmpty()){
                        checkIfBattle = false;
			return false;
		}
		
		//Both stations have warriors.
		Warrior_single RedWarrior = this.RedWarriorStation.get(0);
		Warrior_single BlueWarrior = this.BlueWarriorStation.get(0);
		
		if (this.Flag == WorldProperty.RED){
			activeAttackParty = WorldProperty.RED;
		} else if (this.Flag == WorldProperty.BLUE){
			activeAttackParty = WorldProperty.BLUE;
		} else if (this.CityID % 2 == 1){
			activeAttackParty = WorldProperty.RED;
		} else if (this.CityID % 2 == 0){
			activeAttackParty = WorldProperty.BLUE;
		} else {
			System.err.println("Error organizing Battle: Undefined Situation.");
		}
		
		if (this.activeAttackParty == WorldProperty.RED){
                        checkIfBattle = true;
			RedWarrior.attack(BlueWarrior);
		} else if (this.activeAttackParty == WorldProperty.BLUE){
                        checkIfBattle = true;
			BlueWarrior.attack(RedWarrior);
		} else{
			
		}

		this.activeAttackParty = -1;
		this.Status_AfterBattle = true;
		return true;
	}
	
	/*
	 * TODO: this city pay tribute to Headquarters 
	 * 	and Status_AfterBattle = false;
	 */
	public void payTribute(){
		if (Status_AfterBattle == true){
			if (this.RedWarriorStation.size()+this.BlueWarriorStation.size() == 1){
				if (!this.RedWarriorStation.isEmpty()){
					Headquarters_single RedHeadquarters = (Headquarters_single) World_single.CityList.get(0);
					//001:40 blue dragon 2 earned 10 elements for his headquarter
					//Earn Life Element Declaration is moved to right after battle.
					//Declaration is moved to Warrior.getDeathAnnounce(); 
					

					RedHeadquarters.addLifeElement(this.popLifeElements());
				} else if (!this.BlueWarriorStation.isEmpty()){
					Headquarters_single BlueHeadquarters = (Headquarters_single) World_single.CityList.get(WorldProperty.NumberOfCity+1);
					BlueHeadquarters.addLifeElement(this.popLifeElements());
					//Earn Life Element Declaration.
					//Earn Life Element Declaration is moved to right after battle.
					//Declaration is moved to Warrior.getDeathAnnounce();
				}
			}
			Status_AfterBattle = false;
		}
	}

    boolean checkIfBattle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	
}
