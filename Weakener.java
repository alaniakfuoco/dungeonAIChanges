package Heros;

import java.util.Collection;

import BattleCommands.Ability;
import BattleCommands.CrowdControlAbility;
import BattleCommands.DefensiveAbility;
import BattleCommands.OffensiveAbility;
import PartyContainers.AiBattleReturnType;
import Statuses.StatusEffectAbility;

/**
 * Weakener Monster type. 
 *  Defines the method selectCommand for this specific Monster type.
 *  Will use abilities as such:
 *  10% chance to use an DefensiveAbility
 *  15% chance to use a OffensiveAbility that is not a StatusEffectAbility
 *  	Target defined by highest health
 *  20% chance to use a CrowdControlAbility
 *  	Target defined by highest health
 *  40% chance to use an StatusEffectAbility
 *  	Target defined by Highest defenseRating
 *  15% chance to use baseAttack
 *  	Target defined by highest health
 * @author Andrew
 */
public abstract class Weakener extends Monster {

	
	public Weakener(String image, int experience, int level, int health, int abilityPoints, int defenseRating,
			int speed, String controlledBy) {
		super(image, experience, level, health, abilityPoints, defenseRating, speed, controlledBy);
		setHealRange(0.4);
		setHealChance(0.5);
		setRecoverPointsRange(0.5);
		setRecoverPointsChance(0.75);
		setCureChance(0.15);
	}

	public AiBattleReturnType selectCommand(Collection<Hero> playerParty) {
			
			Hero target = null;
			Ability ability = null;
			
			
			Collection<Ability> availableAbilities = getAvailableAbilities();
			Collection<Hero> availableTargets = getAvailableTargets(playerParty);
			
			if (availableAbilities.size() < 2) {
				ability = baseAttack;
				target = selectByStat(availableTargets,"health",false); //highest Health
				return new AiBattleReturnType(target, ability);
			}
			else {
				double random = Math.random();
				//System.out.println(random);
				if(random > 0.90) {
					ability = getDefensiveAbility(availableAbilities);
				}
				else if(random > 0.75) {
					ability = getOffensiveAbility(availableAbilities);
					target = selectByStat(availableTargets,"health",false); //Highest Health
				}
				else if(random > 0.55) {
					ability = getCrowdControlAbility(availableAbilities);
					target = selectByStat(availableTargets,"health",false); //highest Health
				}
				else if(random > 0.15) {
					ability = getOffensiveStatusAbility(availableAbilities);
					target = selectByStat(availableTargets,"defenseRating",false); //highest defenseRating
				}
			}
			if (ability == null) { 
				ability = baseAttack;
				target = selectByStat(availableTargets,"health",false); //Lowest Health 
			}
			return new AiBattleReturnType(target, ability);
		}
}