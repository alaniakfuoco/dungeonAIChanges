package BattleCommands;

import GridGUI.BattleController;
import Heros.Hero;
import RPG_Exceptions.MaximumStatException;
import RPG_Exceptions.NotAfflictedWithStatusException;

public abstract class BattleCommand {
	
	/**
	 * Print a string representation of this battle command.
	 * @return A string representation of this battle command.
	 */
	public abstract String toString();
	
	/**
	 * Use the battle command in battle. 
	 */
	public abstract void useBattleCommand(Hero hero, Hero other) throws MaximumStatException, NotAfflictedWithStatusException;
	
	public abstract void useBattleCommand(Hero hero, BattleController controller) throws MaximumStatException;
	
}
