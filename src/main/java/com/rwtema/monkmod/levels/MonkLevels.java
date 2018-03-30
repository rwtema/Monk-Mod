package com.rwtema.monkmod.levels;

import com.rwtema.monkmod.abilities.Abilities;
import com.rwtema.monkmod.abilities.MonkAbility;

public class MonkLevels {
	static {
		MonkLevelManager.register(1, Abilities.ARMOR, 0);
		MonkLevelManager.register(2, Abilities.ARMOR, 1);
		MonkLevelManager.register(4, Abilities.ARMOR, 2);
		MonkLevelManager.register(8, Abilities.ARMOR, 3);
		MonkLevelManager.register(16, Abilities.ARMOR, 4);

		MonkLevelManager.register(12, Abilities.ARMOR_TOUGH, 0);
		MonkLevelManager.register(16, Abilities.ARMOR_TOUGH, 0);
		MonkLevelManager.register(18, Abilities.ARMOR_TOUGH, 2);

		MonkLevelManager.register(1, Abilities.STRENGTH, 0);
		MonkLevelManager.register(3, Abilities.STRENGTH, 1);
		MonkLevelManager.register(6, Abilities.STRENGTH, 2);
		MonkLevelManager.register(9, Abilities.STRENGTH, 3);
		MonkLevelManager.register(12, Abilities.STRENGTH, 4);
		MonkLevelManager.register(15, Abilities.STRENGTH, 5);

	}

}
