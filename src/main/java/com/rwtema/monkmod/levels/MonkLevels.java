package com.rwtema.monkmod.levels;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.rwtema.monkmod.abilities.Abilities;
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
						for (int i = 0; i <= level; i++) {
							if (monkLevels.abilityHashMultimap.get(i).contains(monkAbility)) {
								ability_level++;
							}
						}

						MonkLevelManager.register(level, monkAbility, ability_level);
					}
				}
		);
	}


	public void register(int level, MonkAbility... monkAbilities) {
		abilityHashMultimap.putAll(level, ImmutableList.copyOf(monkAbilities));
	}

	public void loadData() {
		register(1, Abilities.ARMOR, Abilities.STRENGTH, Abilities.SPEED);
		register(2, Abilities.ARMOR, Abilities.HEALTH);
		register(3, Abilities.STRENGTH, Abilities.SPEED);
		register(4, Abilities.ARMOR);
		register(5, Abilities.SPEED);
		register(6, Abilities.STRENGTH);
		register(7, Abilities.HEALTH);
		register(8, Abilities.ARMOR, Abilities.SPEED);
		register(9, Abilities.STRENGTH);
		register(12, Abilities.STRENGTH, Abilities.ARMOR_TOUGH);
		register(13, Abilities.HEALTH, Abilities.SPEED);
		register(15, Abilities.STRENGTH);
		register(16, Abilities.ARMOR, Abilities.ARMOR_TOUGH);
		register(18, Abilities.ARMOR_TOUGH, Abilities.SPEED);
		register(20, Abilities.HEALTH);

	}

}
