package Heros;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import BattleCommands.Ability;
import GridGUI.BattleController;
import RPG_Exceptions.NotEnoughAbilityPointsException;
import Statuses.CrowdControlStatus;
import Statuses.StatGet;
import Statuses.StatSet;
import Statuses.Status;

/**
 * Hero abstract class, implements Comparable so the Heros can be sorted by speed when it comes time to initially create the battle system queue. 
 * @author kevin
 *
 */
public abstract class Hero implements Comparable<Hero>{
	private int health;
	private int abilityPoints;
	private int defenseRating;
	private int experience; 
	private int level;
	private int speed;
	private int position;
	private int maxHealth;
	private int maxAP;
	private int attackPower;
	private String name;
	private String image;
	private String controlledBy;
	private HashMap<String, Status> statuses = new HashMap<String, Status>();
	private HashMap<String, Ability> abilities = new HashMap<String,Ability>();
	private static HashMap<String, StatGet> getStats = new HashMap<String, StatGet>();
	private static HashMap<String, StatSet> setStats = new HashMap<String, StatSet>();
	static
	{
		Hero.getStats.put("health", new StatGet() {public int statGet(Hero hero) { return hero.getHealth();}});
		Hero.getStats.put("abilityPoints", new StatGet() {public int statGet(Hero hero) { return hero.getAbilityPoints();}});
		Hero.getStats.put("defenseRating", new StatGet() {public int statGet(Hero hero) { return hero.getDefenseRating();}});
		
		Hero.setStats.put("health", new StatSet() {public void statSet(Hero hero, int val) { hero.setHealth(val);}});
		Hero.setStats.put("abilityPoints", new StatSet() {public void statSet(Hero hero, int val) { hero.setAbilityPoints(val);}});
		Hero.setStats.put("defenseRating", new StatSet() {public void statSet(Hero hero, int val) { hero.setDefenseRating(val);}});
	}
	
	/**
	 * Constructor to create an instance of the Hero class. 
	 * @param name TODO
	 * @param image: image to be used by the view
	 * @param experience: experience, not yet implemented
	 * @param level: level, not yet implements
	 * @param health: initial hero health, also sets the hero's maximum health
	 * @param abilityPoints: initial hero ability points, also sets the hero's maximum ability points 
	 * @param defenseRating: hero's defense rating
	 * @param speed: hero's speed rating, used for sorting to build initial battle queue order
	 * @param controlledBy: controlled by field, has to be a final constant from either HumanPlayer or AI, determines where control is passed in the battle system
	 */
	public Hero(String image, int experience, int level, int health, int abilityPoints, int defenseRating, int speed, String controlledBy, String name)
	{
		this.image = image;
		this.experience = experience;
		this.level=level;
		this.health = health;
		this.maxHealth = health;
		this.abilityPoints = abilityPoints;
		this.maxAP = abilityPoints;
		this.defenseRating = defenseRating;
		this.speed = speed;
		this.controlledBy = controlledBy;
		this.name = name;
	}
	
	/**
	 * Method to confirm a Hero's ability belong to that Hero's subclass. Why abstract?
	 * Don't know the class type of the specific ability 
	 * i.e for soldier must be of type SoldierAbility
	 * @param ability
	 */
	public abstract void setAbilityCheckType(Ability ab);
	
	/**
	 * Get the hero's image
	 * @return a string of the image path
	 */
	public String getImage()
	{
		return this.image;
	}
	
	/**
	 * Get the controlled by string
	 * @return String controlledBy
	 */
	public String getControlledBy()
	{
		return this.controlledBy;
	}
	
	/**
	 * Compare to first checks the hero's speed, then checks by class name.
	 * @param Hero: hero to compare to
	 */
	public int compareTo(Hero other)
	{
		if(this.speed > other.speed)
		{
			return 1;
		}
		else if(this.speed < other.speed)
		{
			return -1;
		}
		else
		{
			// Compare by class name string 
			return(this.getClass().getName().compareTo(other.getClass().getName()));
		}
	}
	
	/**
	 * The hero must update it's own statuses, the status class only adheres to a single status.
	 * Thus the hero iterates through it's own statuses and calls the individual status's update status method
	 * to update the hero's state.
	 * @return boolean: if the hero is crowd controlled no further action can be made on it's turn 
	 */
	public boolean updateStatuses()
	{
		boolean crowdControlled = false;
		if(!this.statuses.isEmpty())
		{
			// Make deep copy to reference from to delete from in for each loop
			// Otherwise get error 
			// Not using Iterator because already returning one value from this.updateStatus
			// Don't wish to deal with an array of booleans as a return, so just make deep copy 
			HashMap<String,Status> tmpCopyForDeletion = new HashMap<String,Status>();
			for(Map.Entry<String, Status> entry : this.statuses.entrySet())
			{
				tmpCopyForDeletion.put(entry.getKey(), entry.getValue().clone());
			}
			
			// Operate on the characters status effects using the old keys generated 
			// When they were applied to the character 
			// While using the tmpCopy so the status effects can be deleted 
			for(Map.Entry<String, Status> entry : tmpCopyForDeletion.entrySet())
			{
				Status currentStatus = this.statuses.get(entry.getKey());
				boolean currentCrowd = currentStatus.updateStatus(this);
				if(currentCrowd)
				{
					crowdControlled = currentCrowd;
				}
			}
			
		}
		return crowdControlled; 
	}
	
	/**
	 * The hero must update it's own statuses, the status class only adheres to a single status.
	 * Thus the hero iterates through it's own statuses and calls the individual status's update status method
	 * to update the hero's state. This version will update an animation to the controller. 
	 * @param controller the BattleController that will update animations 
	 * @return boolean if the hero is crowd controlled no further action can be made on it's turn 
	 */
	public boolean updateStatuses(BattleController controller)
	{
		boolean crowdControlled = false;
		if(!this.statuses.isEmpty())
		{
			// Make deep copy to reference from to delete from in for each loop
			// Otherwise get error 
			// Not using Iterator because already returning one value from this.updateStatus
			// Don't wish to deal with an array of booleans as a return, so just make deep copy 
			HashMap<String,Status> tmpCopyForDeletion = new HashMap<String,Status>();
			for(Map.Entry<String, Status> entry : this.statuses.entrySet())
			{
				tmpCopyForDeletion.put(entry.getKey(), entry.getValue().clone());
			}
			
			// Operate on the characters status effects using the old keys generated 
			// When they were applied to the character 
			// While using the tmpCopy so the status effects can be deleted 
			for(Map.Entry<String, Status> entry : tmpCopyForDeletion.entrySet())
			{
				Status currentStatus = this.statuses.get(entry.getKey());
				boolean currentCrowd = currentStatus.updateStatus(this, controller);
				if(currentCrowd)
				{
					crowdControlled = currentCrowd;
				}
			}
			
		}
		return crowdControlled; 
	}
	
	/**
	 * Hero checks if it has enough ability points to perform an action.
	 * @param ability to determine if the hero has enough ability points to perform 
	 */
	public final void checkIfEnoughAP(Ability ability) throws NotEnoughAbilityPointsException
	{
		if(this.getAbilityPoints() < ability.getPointCost())
		{
			throw new NotEnoughAbilityPointsException();
		}
	}
	
	/**
	 * Hero checks if it has enough ability points to perform an action.
	 * @param ability to determine if the hero has enough ability points to perform
	 * @param controller the controller to signal on exception in GUI program  
	 */
	public final void checkIfEnoughAP(Ability ability, BattleController controller) throws NotEnoughAbilityPointsException
	{
		if(this.getAbilityPoints() < ability.getPointCost())
		{
			throw new NotEnoughAbilityPointsException(controller);
		}
	}
	
	/**
	 * Hero checks if it is under a crowd control status
	 * @return boolean value true if the Hero is under a crowd control status 
	 */
	public boolean checkIfCrowdControlled() {
		Collection<Status> status = this.getStatuses().values();
		for(Status currentStatus : status) {
			if (currentStatus instanceof CrowdControlStatus) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Generic method which returns the current hero's ability HashMap but allows them to be seen as BattleCommands as input to other methods if necessary.
	 * @return Current hero's ability HashMap but allows them to be seen as BattleCommands.
	 */
	public HashMap<String,Ability> getAbilities()
	{
		return this.abilities;
	}
	
	/**
	 * Returns an Array of names of the abilities in the heros ability set. Used to update the text for the current hero's ability buttons in the view.
	 * @return Array of names of the abilities in the heros ability set
	 */
	public String[] getAbilitiesNames()
	{
		String[] names = new String[4];
		int nameCount = 0;
		for(String abName : this.abilities.keySet())
		{
			names[nameCount] = abName;
			nameCount ++;
		}
		return names;
	}
	
	/**
	 * Set the X position of this hero on the battle view grid bag layout. Y position is a constant. 
	 * @param position of hero on battle view grid bag layout. 
	 */
	public void setPosition(int position)
	{
		this.position = position;
	}
	
	/**
	 * Get the X position of this hero on the battle view grid bag layout. Y position is a constant.
	 * @return X position of hero on battle view grid layout.
	 */
	public int getPosition()
	{
		return this.position;
	}
	
	/**
	 * Return the heros max health.
	 * @return Heros max health
	 */
	public int getMaxHealth() {
		return maxHealth;
	}

	/**
	 * Return the heros max ability points
	 * @return The heros max ability points
	 */
	public int getMaxAP() {
		return maxAP;
	}
	
	/**
	 * Return the statuses HashMap for this character
	 * @return Statuses HashMap for this character
	 */
	public final HashMap<String, Status> getStatuses()
	{
		return this.statuses;
	}
		
	/**
	 * Gets the stat by using the HashMap filled with anomyous functions that call a hero's getter function based on the string entered.
	 * @param String: stat, hero field you wish to get
	 * @return int of whatever hero field you specified
	 */
	public final int getStat(String stat)
	{
		return Hero.getStats.get(stat).statGet(this);
	}
	
	/**
	 * Sets the stat by using the HashMap filled with anomyous functions that call a hero's getter function based on the string entered.
	 * @param String: stat, hero field you wish to set
	 * @param Int val: value you wish to set to
	 */
	public final void setStat(String stat, int val)
	{
		Hero.setStats.get(stat).statSet(this, val);
	}
	
	/**
	 * Set the hero health
	 * @param health
	 */
	public final void setHealth(int health)
	{
		this.health = health;
	}
	
	/**
	 * Get the hero health
	 * @return current hero health
	 */
	public final int getHealth(){
		return this.health;
	}

	/**
	 * Get hero ability points
	 * @return The hero's current ability points
	 */
	public final int getAbilityPoints() {
		return abilityPoints;
	}

	/**
	 * Set hero's ability points
	 * @param abilityPoints
	 */
	public final void setAbilityPoints(int abilityPoints) {
		this.abilityPoints = abilityPoints;
	}

	/**
	 * Get the hero's defense rating
	 * @return hero's current defenset rating
	 */
	public final int getDefenseRating() {
		return defenseRating;
	}

	/**
	 * Set the heros defense rating
	 * @param defenseRating
	 */
	public final void setDefenseRating(int defenseRating) {
		this.defenseRating = defenseRating;
	}

	/**
	 * Get the hero's current experience
	 * @return The hero's current experience
	 */
	public final int getExperience() {
		return experience;
	}

	/**
	 * Set the hero's current experience
	 * @param experience
	 */
	public final void setExperience(int experience) {
		this.experience = experience;
	}

	/**
	 * Get the hero's current level
	 * @return Hero's current level
	 */
	public final int getLevel() {
		return level;
	}

	/**
	 * Set the hero's level
	 * @param level
	 */
	public final void setLevel(int level) {
		this.level = level;
	}
	
	public String getToolTip()
	{
		String toolTip = "<html>" + this.getName() + " has:<br>"
				+ " Attack power: " + this.getAttackPower() + " <br>"
				+ " Defense rating: " + this.getDefenseRating() + " </html>";
		return toolTip;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public final void setAttackPower(int attackPower)
	{
		this.attackPower = attackPower;
	}
	
	public int getAttackPower()
	{
		return this.attackPower;
	}
	
	public final void setName(String name)
	{
		this.name = name;
	}
}