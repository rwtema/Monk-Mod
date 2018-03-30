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
		register(1, ARMOR, STRENGTH, SPEED, MINING);
		register(2, ARMOR, HEALTH);
		register(3, STRENGTH, SPEED);
		register(4, ARMOR, MINING);
		register(5, SPEED);
		register(6, STRENGTH);
		register(7, HEALTH, MINING);
		register(8, ARMOR, SPEED);
		register(9, STRENGTH);
		register(12, STRENGTH, ARMOR_TOUGH, MINING);
		register(13, HEALTH, SPEED);
		register(15, STRENGTH);
		register(16, ARMOR, ARMOR_TOUGH);
		register(18, ARMOR_TOUGH, SPEED);
		register(20, HEALTH);

	}

}
