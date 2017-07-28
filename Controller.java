package Heros;

import java.util.Collection;

import BattleCommands.Ability;
import BattleCommands.CrowdControlAbility;
import BattleCommands.DefensiveAbility;
import BattleCommands.OffensiveAbility;
import PartyContainers.AiBattleReturnType;


/**
 * Controller Monster type. 
 *  Defines the method selectCommand for this specific Monster type.
 *  Will use abilities as such:
 *  15% chance to use an OffensiveAbility that is not a CrowdControlAbility
 *  	Target defined by lowest health
 *  25% chance to use a DefensiveAbility
 *  45% chance to use a CrowdControlAbility
 *  	Target defined by highest abilityPoints
 *  15% chance to use baseAttack
 *  	Target defined by lowest health
 * @author Andrew
 */
public abstract class Controller extends Monster {

	
	public Controller(String image, int experience, int level, int health, int abilityPoints, int defenseRating,
			int speed, String controlledBy) {
		super(image, experience, level, health, abilityPoints, defenseRating, speed, controlledBy);
		setHealRange(0.4);
		setHealChance(0.5);
		setRecoverPointsRange(0.6);
		setRecoverPointsChance(0.6);
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
				if(random > 0.85) {
					ability = getOffensiveAbility(availableAbilities);
					if (ability == null) {
						ability = getOffensiveStatusAbility(availableAbilities);
					}
					target = selectByStat(availableTargets,"health",true); //Lowest Health
				}
				else if(random > 0.60) {
					ability = getDefensiveAbility(availableAbilities);
				}
				else if(random > 0.15) {
					ability = getCrowdControlAbility(availableAbilities);
					target = selectByStat(availableTargets,"abilityPoints",false); //Highest abilityPoints
				}
			}
			if (ability == null) { 
				ability = baseAttack;
				target = selectByStat(availableTargets,"health",true); //Lowest Health 
			}
			return new AiBattleReturnType(target, ability);
		}

}