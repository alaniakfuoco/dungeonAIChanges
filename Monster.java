package Heros;

import java.util.Collection;

import PartyContainers.AiBattleReturnType;

/**
 * Monster abstract class.  Includes a unique abstract method to be used by Monsters for individual battle behaviour.
 * @author Andrew
 *
 */

public abstract class Monster extends Hero {

	
	/**
	 * Constructor to create an instance of the Monster class.  This is no different from the Hero constructor.
	 * @param image: image to be used by the view
	 * @param experience: experience, not yet implemented
	 * @param level: level, not yet implements
	 * @param health: initial hero health, also sets the hero's maximum health
	 * @param abilityPoints: initial hero ability points, also sets the hero's maximum ability points 
	 * @param defenseRating: hero's defense rating
	 * @param speed: hero's speed rating, used for sorting to build initial battle queue order
	 * @param controlledBy: controlled by field, has to be a final constant from either HumanPlayer or AI, determines where control is passed in the battle system
	 */
	public Monster(String image, int experience, int level, int health, int abilityPoints, int defenseRating, int speed, String controlledBy) {
		super(image, experience, level, health, abilityPoints, defenseRating, speed, controlledBy);
	}
	
	/**
	 * Abstract method used by Monsters to determine actions during battle.  
	 * This method will be used as part of the AI functionality.
	 * @param playerParty 
	 * @return an AiBattleReturnType. 
	 */
	public abstract AiBattleReturnType selectCommand(Collection<Hero> playerParty);
	

}
