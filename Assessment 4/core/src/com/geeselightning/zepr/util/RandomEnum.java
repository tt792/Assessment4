package com.geeselightning.zepr.util;

import java.util.Random;

/**
 * Utility class for generating random members of enum classes.
 * Intended primarily for use with PowerUp.Type and Zombie.Type.
 * @author Xzytl
 * 
 * @param <E> the enum to return random results for
 */
public class RandomEnum<E extends Enum<?>> {
	
	/* Create random number generator */
	public static final Random rand = new Random();
	/* Store enum values */
	private final E[] values;
	
	public RandomEnum(Class<E> clazz) {
		values = clazz.getEnumConstants();
	}
	
	/**
	 * Returns a random member of the specified enum.
	 * @return
	 */
	public E getRandom() {
		return values[rand.nextInt(values.length)];
	}

}
