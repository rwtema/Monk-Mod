package com.rwtema.monkmod.levels;

import com.google.common.collect.HashMultimap;
import com.rwtema.monkmod.abilities.MonkAbility;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MonkLevelManager {
	public final static int MAX_LEVEL = 20;

	private final HashMultimap<Integer, Entry> abilities = HashMultimap.create();
	private HashMultimap<Integer, Entry> cachedAbilities = null;

	public void register(int level, MonkAbility ability, int abilitiy_level) {
		Validate.isTrue(abilitiy_level < ability.maxlevel);
		cachedAbilities = null;
		abilities.put(level, new Entry(ability, abilitiy_level));
	}

	public Set<Entry> getAbilities(int level) {
		HashMultimap<Integer, Entry> cachedAbilities = this.cachedAbilities;
		if (cachedAbilities == null) {
			cachedAbilities = HashMultimap.create();
			HashMultimap<Integer, Entry> finalCachedAbilities = cachedAbilities;
			HashMap<MonkAbility, Integer> levels = new HashMap<>();
			for (int i = 0; i < MAX_LEVEL; i++) {
				for (Entry entry : abilities.get(i)) {
					levels.merge(entry.ability, entry.level, Math::max);
				}
				levels.forEach((ability, integer) -> {
					finalCachedAbilities.put(level, new Entry(ability, integer));
				});
			}
		}
		return cachedAbilities.get(level);
	}

	public static class Entry {
		final MonkAbility ability;
		final int level;

		public Entry(MonkAbility ability, int level) {
			this.ability = ability;
			this.level = level;
		}
	}
}
