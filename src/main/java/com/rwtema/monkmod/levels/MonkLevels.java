package com.rwtema.monkmod.levels;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.rwtema.monkmod.abilities.MonkAbility;
import com.rwtema.monkmod.advancements.MonkRequirement;
import com.rwtema.monkmod.advancements.criteria.MonkRequirementBreakBlockBareHanded;
import com.rwtema.monkmod.advancements.criteria.MonkRequirementOcean;
import com.rwtema.monkmod.advancements.criteria.MonkRequirementSunriseSunset;
import com.rwtema.monkmod.advancements.criteria.MonkRequirementWalk;
import net.minecraft.init.Blocks;

import java.util.HashMap;

import static com.rwtema.monkmod.abilities.Abilities.*;

public class MonkLevels {
	public static final HashMap<Integer, MonkRequirement> levelUpRequirements = new HashMap<>();
	public HashMultimap<Integer, MonkAbility> abilityHashMultimap = HashMultimap.create();

	public static void init() {
		MonkLevels monkLevels = new MonkLevels();
		monkLevels.loadAbilities();
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
						} catch (Exception err) {
							throw new RuntimeException("Unable to register: " + level + " " + monkAbility.name + " " + ability_level, err);
						}
					}
				}
		);
	}


	public void register(int level, MonkAbility... monkAbilities) {
		abilityHashMultimap.putAll(level, ImmutableList.copyOf(monkAbilities));
	}

	public void loadAbilities() {

		register(new MonkRequirementBreakBlockBareHanded(0, (world, pos, state) -> state.getBlock().isWood(world, pos), 1));

		// Sapling
		// Punch 50 tree logs with bare hands.
		register(1, MINING);
		register(new MonkRequirementBreakBlockBareHanded(1, (world, pos, state) -> state.getBlock().isWood(world, pos), 10));

		// Chicken
		// Meditate while watching a sunset or sunrise.
		register(2, HUNGER);
		register(2, ARMOR, HEALTH);
		register(new MonkRequirementSunriseSunset(2, 10 * 20));

		// Squid
		// Swim to the bottom of an ocean.
		register(3, WATER_BREATHING);
		register(3, ARMOR, SPEED);
		register(new MonkRequirementOcean(3, 20));

		// Silverfish
		// Break 10 stone blocks with your bare hands
		register(new MonkRequirementBreakBlockBareHanded(4, (world, pos, state) -> state.getBlock() == Blocks.STONE, 5));
		register(4, MINING);
		register(4, ARMOR, STRENGTH);

		// Ocelot
		// From sunrise to sunset, run a mile in one day.
		register(new MonkRequirementWalk(5, 100));
		register(5, SPEED);
		register(5, HEALTH);

		// Wolf
		// Kill 10 hostile mobs with your bare fists
		register(6, STRENGTH);
		register(6, ARMOR);

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

		// Creeper
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

	private void register(MonkRequirement requirement) {
		levelUpRequirements.put(requirement.levelToGrant, requirement);
	}
}
