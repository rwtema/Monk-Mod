package com.rwtema.monkmod.levels;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import static com.rwtema.monkmod.abilities.Abilities.*;
import com.rwtema.monkmod.abilities.MonkAbility;

public class MonkLevels {
	public HashMultimap<Integer, MonkAbility> abilityHashMultimap = HashMultimap.create();

	public static void init() {
		MonkLevels monkLevels = new MonkLevels();
		monkLevels.loadData();
		monkLevels.abilityHashMultimap.keySet().stream().sorted().forEach(
				level -> {
					for (MonkAbility monkAbility : monkLevels.abilityHashMultimap.get(level)) {
						int ability_level = 0;
						for (int i = 0; i < level; i++) {
							if (monkLevels.abilityHashMultimap.get(i).contains(monkAbility)) {
								ability_level++;
							}
						}

						try {
							MonkLevelManager.register(level, monkAbility, ability_level);
						}catch (Exception err){
							throw new RuntimeException("Unable to register: " + level + " " + monkAbility.name + " " + ability_level, err);
						}
					}
				}
		);
	}


	public void register(int level, MonkAbility... monkAbilities) {
		abilityHashMultimap.putAll(level, ImmutableList.copyOf(monkAbilities));
	}

	public void loadData() {
		// Sapling
		// Punch 40 tree logs with bare hands.
		register(1, ARMOR, STRENGTH, SPEED, MINING);

		// Chicken
		// Meditate while watching a sunset or beneath a waterfall.
		register(2, ARMOR, HEALTH);

		// Squid
		// Swim to the bottom of an ocean.
		register(3, STRENGTH, SPEED);

		// Silverfish
		// Break 10 stone blocks with your bare hands
		register(4, ARMOR, MINING);

		// Ocelot
		// Run a mile in one day.
		register(5, SPEED);

		// Wolf
		// Kill 10 hostile mobs with your bare fists
		register(6, STRENGTH);

		// Golem
		// Break an iron storage block with your bare fists
		register(7, HEALTH, MINING);

		// Blaze
		// Walk through fire.
		register(8, ARMOR, SPEED);

		// Ghast
		// Break obsidian with bare hands
		register(9, MINING);

		// Master
		register(10, STRENGTH);


		register(11);

		register(12, STRENGTH, ARMOR_TOUGH, MINING);
		register(13, HEALTH, SPEED);
		register(14);
		register(15, STRENGTH);
		register(16, ARMOR, ARMOR_TOUGH);
		register(17);
		register(18, ARMOR_TOUGH, SPEED);
		register(20, HEALTH, MINING);

	}

}
