package Heros;

import java.util.Collection;

import BattleCommands.Ability;
import BattleCommands.CrowdControlAbility;
import BattleCommands.DefensiveAbility;
import BattleCommands.OffensiveAbility;
import PartyContainers.AiBattleReturnType;


/**
 * Defender Monster type. 
 *  Defines the method selectCommand for this specific Monster type.
 *  Will use abilities as such:
 *  25% chance to use an OffensiveAbility
 *  	Target defined by lowest health
 *  25% chance to use a CrowdControlAbility
 *  	Target defined by highest health
 *  30% chance to use a DefensiveAbility
 *  20% chance to use baseAttack
 *  	Target defined by lowest health
 * @author Andrew
 */
public abstract class Defender extends Monster {

	
	public Defender(String image, int experience, int level, int health, int abilityPoints, int defenseRating,
			int speed, String controlledBy) {
		super(image, experience, level, health, abilityPoints, defenseRating, speed, controlledBy);
		setHealRange(0.5);
		setHealChance(0.75);
		setRecoverPointsRange(0.3);
		setRecoverPointsChance(0.3);
		setCureChance(0.5);
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
				if(random > 0.75) {
					ability = getOffensiveAbility(availableAbilities);
					if (ability == null) {
						ability = getOffensiveStatusAbility(availableAbilities);
					}
					target = selectByStat(availableTargets,"health",true); //Lowest Health
				}
				else if(random > 0.50) {
					ability = getCrowdControlAbility(availableAbilities);
					target = selectByStat(availableTargets,"abilityPoints",false); //Highest abilityPoints
				}
				else if(random > 0.20) {
					ability = getDefensiveAbility(availableAbilities);
				}
			}
			if (ability == null) { 
				ability = baseAttack;
				target = selectByStat(availableTargets,"health",true); //Lowest Health 
			}
			return new AiBattleReturnType(target, ability);
		}

}