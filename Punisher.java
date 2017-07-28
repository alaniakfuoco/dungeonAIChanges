package Heros;

import java.util.Collection;

import BattleCommands.Ability;
import BattleCommands.CrowdControlAbility;
import BattleCommands.DefensiveAbility;
import BattleCommands.OffensiveAbility;
import PartyContainers.AiBattleReturnType;
import Statuses.StatusEffectAbility;

/**
 * Punisher Monster type. 
 *  Defines the method selectCommand for this specific Monster type.
 *  Will use abilities as such:
 *  10% chance to use an DefensiveAbility
 *  15% chance to use a CrowdControlAbility
 *  	Target defined by highest Health
 *  20% chance to use a StatusEffectAbility
 *  	Target defined by if anyone is CrowdControlled OR highest defenseRating
 *  40% chance to use an OffensiveAbility
 *  	Target defined by if anyone is CrowdControlled OR lowest Health
 *  15% chance to use baseAttack
 *  	Target defined by lowest health
 * @author Andrew
 */
public abstract class Punisher extends Monster {

	
	public Punisher(String image, int experience, int level, int health, int abilityPoints, int defenseRating,
			int speed, String controlledBy) {
		super(image, experience, level, health, abilityPoints, defenseRating, speed, controlledBy);
		setHealRange(0.3);
		setHealChance(0.2);
		setRecoverPointsRange(0.5);
		setRecoverPointsChance(0.5);
		setCureChance(0.75);
	}

	public AiBattleReturnType selectCommand(Collection<Hero> playerParty) {
			
			Hero target = null;
			Ability ability = null;
			
			
			Collection<Ability> availableAbilities = getAvailableAbilities();
			Collection<Hero> availableTargets = getAvailableTargets(playerParty);
			
			if (availableAbilities.size() < 2) {
				ability = baseAttack;
				target = selectByStat(availableTargets,"health",true); //Lowest Health
				return new AiBattleReturnType(target, ability);
			}
			else {
				double random = Math.random();
				//System.out.println(random);
				if(random > 0.90) {
					ability = getDefensiveAbility(availableAbilities);
				}
				else if(random > 0.75) {
					ability = getCrowdControlAbility(availableAbilities);
					target = selectByStat(availableTargets,"health",false); //Highest Health
				}
				else if(random > 0.55) {
					ability = getOffensiveStatusAbility(availableAbilities);
					target = selectIfCrowdControlled(availableTargets);	//Crowd Controlled
					if (target == null) { target = selectByStat(availableTargets,"defenseRating",false); } //OR highest defense
				}
				else if(random > 0.15) {
					ability = getOffensiveAbility(availableAbilities);
					target = selectIfCrowdControlled(availableTargets);	//Crowd Controlled
					if (target == null) { target = selectByStat(availableTargets,"health",true); } //OR lowest health
				}
			}
			if (ability == null) { 
				ability = baseAttack;
				target = selectByStat(availableTargets,"health",true); //Lowest Health 
			}
			return new AiBattleReturnType(target, ability);
		}
}