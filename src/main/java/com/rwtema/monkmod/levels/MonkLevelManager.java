package com.rwtema.monkmod.levels;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.rwtema.monkmod.MonkMod;
import com.rwtema.monkmod.abilities.MonkAbility;
import com.rwtema.monkmod.advancements.MonkRequirement;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Set;

public class MonkLevelManager {

	private static final HashMultimap<Integer, MonkAbility> abilities = HashMultimap.create();
	private static HashMap<Integer, Set<MonkAbility>> cachedAbilities = null;

	public static void register(int level, MonkAbility ability) {
		cachedAbilities = null;
		abilities.put(level, ability);
	}

	public static final TIntObjectHashMap<MonkRequirement> requirements = new TIntObjectHashMap<>();

	public static void registerRequirement(int level, MonkRequirement requirement){
		requirements.put(level, requirement);
		requirement.levelToGrant = level;
	}

	public static Set<MonkAbility> getAbilities(int level) {
		HashMap<Integer, Set<MonkAbility>> cachedAbilities = MonkLevelManager.cachedAbilities;
		if (cachedAbilities == null) {
			cachedAbilities = new HashMap<>();

			HashMap<MonkAbility, Integer> levels = new HashMap<>();
			for (int i = 0; i <= MonkMod.MAX_LEVEL; i++) {
				cachedAbilities.put(i, ImmutableSet.copyOf(abilities.get(i)));
			}
		}
		level = MathHelper.clamp(level, 0, MonkMod.MAX_LEVEL);
		return cachedAbilities.get(level);
	}

}
