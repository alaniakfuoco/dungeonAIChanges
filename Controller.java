package Heros;

import java.util.Collection;

import BattleCommands.Ability;
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

	/**
	 * Constructor to create an instance of the Controller class.
	 * This constructor will specify values for a Monster to check when to use items.
	 * @param image: image to be used by the view
	 * @param experience: experience, not yet implemented
	 * @param level: level, not yet implements
	 * @param health: initial hero health, also sets the hero's maximum health
	 * @param abilityPoints: initial hero ability points, also sets the hero's maximum ability points 
	 * @param defenseRating: hero's defense rating
	 * @param speed: hero's speed rating, used for sorting to build initial battle queue order
	 * @param controlledBy: controlled by field, has to be a final constant from either HumanPlayer or AI, determines where control is passed in the battle system
	 */
	public Controller(String image, int experience, int level, int health, int abilityPoints, int defenseRating,
			int speed, String controlledBy) {
		super(image, experience, level, health, abilityPoints, defenseRating, speed, controlledBy);
		setHealRange(0.4);
		setHealChance(0.5);
		setRecoverPointsRange(0.6);
		setRecoverPointsChance(0.6);
		setCureChance(0.5);
	}

	/**
	 * Abstract method used by Monsters to determine actions during battle.  
	 * This method will be used as part of the AI functionality to return an ability to use and a target (if applicable).
	 * @param Collection<Hero> A list of the Hero instances in the player's party
	 * @return AiBattleReturnType holding both a target (if applicable) and an ability to use. 
	 */
	public AiBattleReturnType selectCommand(Collection<Hero> playerParty) {
			
			Hero target = null;
			Ability ability = null;
			
			Collection<Ability> availableAbilities = getAvailableAbilities();
			Collection<Hero> availableTargets = getAvailableTargets(playerParty);
			
			if (availableAbilities.size() < 2) {
				ability = this.getBaseAttack();
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
				ability = this.getBaseAttack();
				target = selectByStat(availableTargets,"health",true); //Lowest Health 
			}
			return new AiBattleReturnType(target, ability);
		}

}