package PartyContainers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;
import BattleCommands.AbilityItem;
import BattleCommands.BattleCommand;
import BattleCommands.HealthItem;
import BattleCommands.Item;
import BattleCommands.OffensiveAbility;
import BattleCommands.StatusItem;
import BattleMVC.BattleController;
import Heros.Hero;
import Heros.Monster;
import Heros.SkeletonArcher;
import Heros.SkeletonBoss;
import Heros.SkeletonSpearMan;
import Heros.SkeletonWarrior;
import RPG_Exceptions.BattleModelException;
import RPG_Exceptions.MaximumStatException;
import RPG_Exceptions.NotAfflictedWithStatusException;
import Statuses.Status;

/**
 * Artificial Intelligence class, will scan the human opponent and make a decision as which move to make.
 * @author Kevin, Andrew 
 *
 */

public class AI extends Player{
	// Public constant which defines who to give control to in the battle system
	public static final String CONTROLLER = "AI";
	private BattleController controller;
	
	/**
	 * Constructor which creates the default party.
	 */
	public AI()
	{
		super();
		makeDefaultParty();
	}
	
	/**
	 * Creates the default AI party uses Heros specific to the AI.
	 */
	public void makeDefaultParty()
	{
		ArrayList<Hero> partyArray = new ArrayList<Hero>();
		partyArray.add(new SkeletonBoss(AI.CONTROLLER));
		partyArray.add(new SkeletonSpearMan(AI.CONTROLLER));
		partyArray.add(new SkeletonArcher(AI.CONTROLLER));
		partyArray.add(new SkeletonWarrior(AI.CONTROLLER));
		for(Hero hero : partyArray)
		{
			super.getParty().put(hero.getClass().getName(), hero);
		}
	}
	
	/**
	 * This method is used to determine a Monster's actions in battle.
	 * The method uses various values from the Monster to when and how often it will use certain items.
	 * The priority of items goes as follows: health item, status cure item, ability point item.
	 * If an item is not used the method will call the Monsters selectCommand method which through polymorphism is
	 * different based on the type of Monster acting.
	 * @param Monster The current acting character
	 * @param Player The enemy's player to access their party
	 * @return AiBattleReturnType The object which holds both the ability to be used and target.
	 * @throws A child of BattleModelException if the defensive ability or item used cannot increase the statistic that it affects, if you pick a 
	 * target that isn't alive, or if the character doesn't have enough ability points. 
	 */
	public AiBattleReturnType scan(Monster monster, Player player) throws BattleModelException
	{
		
		AiBattleReturnType result;
		TreeMap<String, Hero> party = player.getParty();
		Collection<Hero> playerParty = party.values();
		
		// Checks to see if Monster will use a Health item
		double currentHealth = (double) monster.getHealth();
		double maxHealth = (double) monster.getMaxHealth();
		if ((currentHealth / maxHealth) <= monster.getHealRange()) {
			double random = Math.random();
			if (random < monster.getHealChance()) {
				Item item = pickHealingItem();
				if(item != null)		//If a healing item exists, use it
				{
					useItem(item, monster);
					result = new AiBattleReturnType(null,item);
					return result;		
				}
			}
		}
		
		// Checks to see if Monster will use a Status item
		Collection<Status> status = monster.getStatuses().values();

		if (status.size() != 0) {
			double random = Math.random();
			if (random < monster.getCureChance()) {
				Item item = setStatusCure(status);
				if(item != null)	//If a curing item exists, use it
				{
					useItem(item, monster);
					result = new AiBattleReturnType(null,item);
					return result;
				}
			}
		}
		
		// Checks to see if Monster will use an AbilityPoints item
		double currentAbilityPoints = (double) monster.getAbilityPoints();
		double maxAbilityPoints = (double) monster.getMaxAP();
		if ((currentAbilityPoints / maxAbilityPoints) <= monster.getRecoverPointsRange()) {
			double random = Math.random();
			if (random < monster.getHealChance()) {
				Item item = pickAbilityPointItem();
				if(item != null)	//If an ability point item exists, use it
				{
					useItem(item, monster);
					result = new AiBattleReturnType(null,item);
					return result;
				}
			}
		}
		

		// Attack if Monster did not heal/recover
		//selectCommand will select ability and target based on the Monster's sub-type
		result = monster.selectCommand(playerParty);
		
		System.out.println("AI using ability: " + result.getCmd().toString());
		
		BattleCommand ability = result.getCmd();
		Hero target = result.getTarget();
		if(controller != null)
		{
			if (ability instanceof OffensiveAbility) {
				OffensiveAbility useAbility = (OffensiveAbility) ability;
				useAbility.useBattleCommand(monster, target, controller);
			} else {
				ability.useBattleCommand(monster, controller);
			}
		}
		else
		{
			try {
				ability.useBattleCommand(monster, target);
			} catch (NotAfflictedWithStatusException e) {
				e.printStackTrace();
			}
		}
		
		return result;
		
	}
	
	/**
	 * A simple method which calls useBattleCommand on the specified Item instance.
	 * If controller present animates.
	 * @param Item item to use
	 * @param Monster acting monster to use it on
	 * @throws BattleModelException if item won't change hero's stats.
	 */
	private void useItem(Item item, Monster monster) throws BattleModelException {
		System.out.println("AI using item: " + item.toString());
		Hero target = null;
		if(controller != null)
		{
			item.useBattleCommand(monster, controller);
		}
		else
		{
			item.useBattleCommand(monster, target);
		}
	}
	

	/**
	 * Function used to start the AI turn, checks if the AI is crowd controlled, if it is will tick all current statuses on the acting hero and finish the AI's turn.
	 * If not will proceed to scan the player party to choose the best course of action.
	 * @param hero: current acting hero
	 * @param human: enemy party to scan
	 * AiBattleReturnType: What is this? A class specifically made so that we can return two types of values by setting them as fields in this class.
	 * This class stores the target and AI ability used to be published to the view.
	 * @throws BattleModelException 
	 * @throws NotAfflictedWithStatusException 
	 */
	public AiBattleReturnType aiTurn(Hero monster, Player human) throws BattleModelException {
		
		AiBattleReturnType target = new AiBattleReturnType(null,null);
		System.out.println("Enemy Turn!");
		boolean AIControlled;
		if(controller != null)
        {
            AIControlled = monster.updateStatuses(controller);
        }
        else
        {
            AIControlled = monster.updateStatuses();
        }
		
		if(!AIControlled && monster.getHealth() > 0) {		//If Monster is under a crowd control effect, or it's health is 0, do not act.
			try	{ target = this.scan((Monster)monster, human); }
    		catch(MaximumStatException e) { e.printStackTrace(); }
		}
		
		return target;
	}
	
	/**
	 * Picks strongest healing item.
	 * @return Strongest healing item.
	 */
	private Item pickHealingItem()
	{
		Item bestItem = null;
		TreeMap<String, Item> inv = this.getInventory();
		int largestHeal = 0;
		if(!inv.isEmpty())
		{
			// Loops through entire inventory
			for(Item item : inv.values())
			{
				// Make sure to check only for healing items 
				if (item instanceof HealthItem)
				{
					int currentHeal = item.getEffectStrength();
					if(currentHeal > largestHeal)
					{
						bestItem = item;
					}
				}
			}
		}
		return bestItem;
	}
	
	/**
	 * Pick strongest ability point recovery item.
	 * @return Strongest ability point recovery item.
	 */
	private Item pickAbilityPointItem() {
		Item bestItem = null;
		TreeMap<String, Item> inv = this.getInventory();
		int largestHeal = 0;
		if(!inv.isEmpty())
		{
			// Loops through entire inventory
			for(Item item : inv.values())
			{
				// Make sure to check only for ability point items 
				if (item instanceof AbilityItem)
				{
					int currentHeal = item.getEffectStrength();
					if(currentHeal > largestHeal)
					{
						bestItem = item;
					}
				}
			}
		}
		return bestItem;
	}

	/**
	 * Picks a random status effect which the current actor has an item for.
	 * @param status A collection of all the status the current actor has.
	 * @return An item which can cure a status the current actor has.
	 */
	private Item setStatusCure(Collection<Status> status) {
		ArrayList<Item> matches = new ArrayList<>();
		Collection<Item> inventory = this.getInventory().values();
		for(Status currentStatus : status) {	// Loops through each status
			for(Item currentItem : inventory) {	// For each status, check to see if an item exists that can cure it
				if (currentItem instanceof StatusItem && currentStatus.getName() == ((StatusItem) currentItem).getStatusAffected()) {
					matches.add(currentItem);	// If an item exists add it to the list.
				}
			}
		}
		if (matches.size() == 0) { return null; }
		return Monster.pickRandom(matches);		// Picks a random match
	}
	
	/**
	 * Set the AI's controller.
	 * @param controller
	 */
	public void setController(BattleController controller)
	{
		this.controller = controller;
	}
}
