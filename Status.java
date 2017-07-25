package Statuses;

import java.awt.Image;
import java.util.Collection;
import java.util.Date;

import GridGUI.BattleController;
import Heros.Hero;
/**
 * Status abstract class.
 * Why abstract? 
 * Don't know what type of status is applied yet, does it tick per turn or is it a one 
 * time application?
 * @author Kevin
 *
 */
public abstract class Status implements Cloneable{
	private String name;
	private String statAffected;
	private int effectStrength;
	private int duration;
	private final int defaultDuration;
	private long time;
	private Image animationImage;
	public static final int STATUS_IMAGE_WIDTH = 150;
	public static final int STATUS_IMAGE_HEIGHT = 150;
	public static final int DEFEND_STATUS_HEIGHT = 250;
	/**
	 * Status argument constructor.
	 * @param name: status name
	 * @param statAffected: stat affected
	 * @param effectStrength: effect strength
	 * @param duration: duration of status
	 * @param defaultDuration: default duration of status
	 */
	public Status(String name, String statAffected, int effectStrength, int duration, int defaultDuration, Image animationImage)
	{
		this.name = name;
		this.statAffected = statAffected;
		this.effectStrength = effectStrength;
		this.duration = duration;
		this.defaultDuration = defaultDuration;
		this.animationImage = animationImage;
		this.time = new Date().getTime();
		
	}
	
	/**
	 * Status copy constructor. Used when updating and adding status effects heros to create deep copies in clone so references aren't shared in common.
	 * If references were shared in common when the duration of the applied status is changed the duration of the 
	 * status effect applied by a hero's ability would also be changed.
	 * @param other: Status to copy
	 */
	public Status(Status other)
	{
		this(other.name, other.statAffected, other.effectStrength, other.duration, other.defaultDuration, other.animationImage);
		// New time stamp is created in the original constructor
		// This way we can stack status effects 
	}
	
	/**
	 * Add a status to the targeted hero. Clones a deep copy with a new time stamp so we can stack statuses. 
	 * @param Status: effect, status to add to this character 
	 */
	public Status addStatus(Hero hero)
	{
		Status statusToApply = this.clone();
		hero.getStatuses().put(statusToApply.getKey(), statusToApply);
		return statusToApply;
	}
	
	/**
	 * Remove status from character
	 * @param hero: hero to remove status from
	 */
	public void removeStatus(Hero hero)
	{
		hero.getStatuses().remove(this.getKey());
	}
	
	/**
	 * Get status by class Status.
	 * @param hero: hero to get status from
	 * @return Status: the status to get
	 */
	public Status getStatus(Hero hero)
	{
		return hero.getStatuses().get(this.getKey());
	}
	
	/**
	 * Get a status from the heros HashMap by name, used by StatusItem as no new Status is created.
	 * @param hero: hero to get status from
	 * @param name: name of status to get
	 * @return status to get
	 */
	public static Status getStatus(Hero hero, String name)
	{
		//return hero.getStatuses().get(name);
		Status effect = null;
		Collection<Status> statusEffects = hero.getStatuses().values();
		for (Status s :statusEffects) {
			if (s.getName() == name) { effect = s; }
		}
		return effect;
	}
	
	/**
	 * Remove status by the String name of the status, used by StatusItem as no new Status is created.
	 * @param hero: hero to get status from
	 * @param name: name of status to be removed
	 */
	public static void removeStatus(Hero hero, String name)
	{
		//Status effect = hero.getStatuses().get(name);
		Status effect = getStatus(hero,name);
		effect.removeStatus(hero);
	}
	
	/**
	 * Abstract definition to be defined by subclasses depending on what status effect they apply.
	 * @param hero: hero to apply status effect to
	 */
	public abstract void applyStatusEffect(Hero hero);
	
	/**
	 * Abstract method to update the current status. We don't yet know what all parameters of the applied status effect, thus 
	 * this should be defined in the subclass.
	 * @param hero : hero to apply status affect to
	 * @return boolean: crowd controlled or not, if crowd controlled must skip that heros turn.
	 */
	public abstract boolean updateStatus(Hero hero);
	
	/**
	 * Abstract method to update the current status. We don't yet know what all parameters of the applied status effect, thus 
	 * this should be defined in the subclass.
	 * @param hero hero to apply status affect to
	 * @param controller the controller is needed to signal the status animation
	 * @return boolean crowd controlled or not, if crowd controlled must skip that heros turn.
	 */
	public abstract boolean updateStatus(Hero hero, BattleController controller);
	
	/**
	 * Clone method, since this is an abstract class we can't create instances of it. 
	 * This is used in updating a hero's statuses to create a copy of the proper run type
	 * so we can still apply polymorphism. 
	 * 
	 * Why not a static factory method? 
	 * Polymorphism doesn't apply to static methods.
	 */
	public abstract Status clone();

	/**
	 * Get effect strength
	 * @return this statuses effect strength
	 */
	public int getEffectStrength() {
		return effectStrength;
	}

	/**
	 * Get duration
	 * @return this statuses duration 
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Set remaining duration of status effect
	 * @param duration remaining
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * Get default duration of status effect to see if first application.
	 * @return the default duration of this status effect
	 */
	public int getDefaultDuration()
	{
		return this.defaultDuration;
	}
	
	/**
	 * Returns the stat affected by this status effect
	 * @return A string of the statistic name i.e health
	 */
	public String getStatAffected()
	{
		return this.statAffected;
	}
	
	/**
	 * Get a string representation of this status effect. Used to public info on the view.
	 * @return A string representation of this status effect. 
	 */
	public String toString()
	{
		return this.name + " Ticks: " + this.duration;
	}
	
	/**
	 * Get the key of this status effect to access it from a hero's statuses Map.
	 * @return The Map key of this status effect. 
	 */
	public String getKey()
	{
		return this.name + "_" + String.format("%d",this.time);
	}
	
	/**
	 * Get this statuses name.
	 * @return Name of status
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Get the animation image associated with this class.
	 * @return The animation image associated with this class.
	 */
	public Image getAnimationImage()
	{
		return this.animationImage;
	}
}
