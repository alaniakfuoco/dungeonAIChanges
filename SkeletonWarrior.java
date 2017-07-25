package Heros;

import java.util.ArrayList;
import java.util.Collection;

import BattleCommands.Ability;
import BattleCommands.BaseAttack;
import BattleCommands.OffensiveAbility;
import BattleCommands.SoldierAbility;
import BattleCommands.SoldierAbility.Defend;
import PartyContainers.AI;
import PartyContainers.AiBattleReturnType;

/**
 * SkeletonWarrior hero class. Sets base level 1 SkeletonWarrior fields. 
 * Checks to insure the class only has Soldier Abilities.
 * @author Kevin
 *
 */
public class SkeletonWarrior extends Monster {
	private static int experiencePerLevel = 100;
	private static final String IMAGE = "/warrior.gif";
	private int baseStrength;
	private int strengthItemBonus;
	private int attackPower;
	private BaseAttack baseAttack;
	private OffensiveAbility hamString = new SoldierAbility.HamString();
	private OffensiveAbility shieldBash = new SoldierAbility.ShieldBash();
	
	/**
	 * Default SkeletonWarrior constructor
	 * @param controlledBy: who will control this character, player or AI?
	 */
	public SkeletonWarrior(String controlledBy)
	{
		// Set experience to 0, level 0, health 6, AB 5, defense 2, speed 2
		super(SkeletonWarrior.IMAGE,0,0/SkeletonWarrior.experiencePerLevel,6,5,2,1,controlledBy);
		this.baseStrength = 3;
		this.strengthItemBonus = 1;
		this.attackPower = this.baseStrength + this.strengthItemBonus;
		this.baseAttack = new BaseAttack(this.attackPower);
		Defend SkeletonWarriorDefend = new SoldierAbility.Defend(3);
		this.setAbilityCheckType(baseAttack);
		this.setAbilityCheckType(hamString);
		this.setAbilityCheckType(SkeletonWarriorDefend);
		this.setAbilityCheckType(shieldBash);
	}
	
	/**
	 * Set this hero's abilities, used to insure they only receive SoldierAbilities.
	 */
	@Override
	public void setAbilityCheckType(Ability ability) {
		if(ability.getClassOwner().equals("ALL") || ability.getClassOwner().equals("SOLDIER"))
		{
			ability.setAbility(this);
		}
	}
	
	/**
	 * Get the image path to be displayed by the view.
	 * @return image path
	 */
	public String getImage() {
		return SkeletonWarrior.IMAGE;
	}

	@Override
	public AiBattleReturnType selectCommand(Collection<Hero> playerParty) {
		
		Hero target = null;
		Ability ability = null;
		
		Collection<Ability> abilities = this.getAbilities().values();
		Collection<Ability> availableAbilities = new ArrayList<>();
		for (Ability a : abilities) {
			if (a.getPointCost() < this.getAbilityPoints()) {
				availableAbilities.add(a);
			}
		}
		
		if (availableAbilities.size() == 1) {
			ability = baseAttack;
			target = AI.selectByStat(playerParty,"health",true); //Lowest Health
		}
		else {
			double random = Math.random();
			if (random > 0.75 && availableAbilities.contains(shieldBash)) {
				ability = shieldBash;
				target = AI.selectByStat(playerParty,"health",false); //Highest Health
			} else if (random > 0.4 && availableAbilities.contains(hamString)) {
				ability = hamString;
				target = AI.selectByStat(playerParty,"abilityPoints",true);  //Lowest ability points
			} else {
				ability = baseAttack;
				target = AI.selectByStat(playerParty,"health",true); //Lowest Health
			}
		}
		
		return new AiBattleReturnType(target, ability);
	}
	
}
