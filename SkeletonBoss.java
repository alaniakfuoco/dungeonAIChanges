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
 * SkeletonBoss hero class. Sets base level 1 SkeletonBoss fields. 
 * Checks to insure the class only has Soldier Abilities.
 * @author Kevin
 *
 */

public class SkeletonBoss extends Monster {
	private static int experiencePerLevel = 100;
	private static final String IMAGE = "/boss_skele.gif";
	private int baseStrength;
	private int strengthItemBonus;
	private int attackPower;
	private BaseAttack baseAttack;
	private OffensiveAbility hamString = new SoldierAbility.HamString();
	private OffensiveAbility shieldBash = new SoldierAbility.ShieldBash();
	private Defend SkeletonBossDefend = new SoldierAbility.Defend(3);
	
	/**
	 * Default SkeletonBoss constructor
	 * @param controlledBy: who will control this character, player or AI?
	 */
	public SkeletonBoss(String controlledBy)
	{
		// Set experience to 0, level 0, health 6, AB 5, defense 2, speed 2
		super(SkeletonBoss.IMAGE,0,0/SkeletonBoss.experiencePerLevel,8,5,3,2,controlledBy);
		this.baseStrength = 4;
		this.strengthItemBonus = 1;
		this.attackPower = this.baseStrength + this.strengthItemBonus;
		this.baseAttack = new BaseAttack(this.attackPower);
		this.setAbilityCheckType(baseAttack);
		this.setAbilityCheckType(hamString);
		this.setAbilityCheckType(SkeletonBossDefend);
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
		return SkeletonBoss.IMAGE;
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

		double random = Math.random();
		if (random > 0.75 && availableAbilities.contains(shieldBash)) {
			ability = shieldBash;
			target = AI.selectByStat(playerParty,"abilityPoints",false); //Highest AP
		} else if (random > 0.5 && availableAbilities.contains(SkeletonBossDefend)) {
			ability = SkeletonBossDefend;
			target = this; //Sets target to itself
		} else if (random > 0.25 && availableAbilities.contains(hamString)) {
			ability = hamString;
			target = AI.selectByStat(playerParty,"health",false); //Highest Health
		} else {
			ability = baseAttack;
			target = AI.selectByStat(playerParty,"health",true); //Lowest Health
		}
		
		return new AiBattleReturnType(target, ability);
	}
	
}
