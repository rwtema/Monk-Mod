package com.rwtema.monkmod.config;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.rwtema.monkmod.MonkMod;
import com.rwtema.monkmod.abilities.MonkAbility;
import com.rwtema.monkmod.advancements.MonkRequirement;
import com.rwtema.monkmod.factory.Factory;
import com.rwtema.monkmod.levels.MonkLevelManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MonkConfiguration {


	public static final String MONK_LEVEL_DATA = "MonkLevelData";
	public static final String MONK_WEAR_ITEMS = "MonkWear";
	public static final HashBiMap<Integer, String> texMap = HashBiMap.create();
	public static LevelData[] data;
	public static Set<ResourceLocation> whitelist;
	public static Set<ResourceLocation> blacklist;

	@SuppressWarnings("unchecked")
	public static void genDefaultConfig() {
		ConfigCategory category = MonkMod.config.getCategory(MONK_LEVEL_DATA);
		category.clear();
		ImmutableList.copyOf(category.getChildren()).forEach(category::removeChild);

		MonkMod.config.get(MONK_LEVEL_DATA, "Max Level", 20, "Number of Levels").getInt();

		String comment = "Specify requirements to unlock each level and abilities to gain. See below for available abilities and requirements as well as needed parameters." +
				"\n\n\n" +
				"Abilities:\n\n" +
				Factory.abilityFactories.values().stream().sorted(Comparator.comparing(f -> f.name)).map(Object::toString).collect(Collectors.joining("\n")) +
				"\n\n\n" +
				"Requirements:\n\n" +
				Factory.requirementFactories.values().stream().sorted(Comparator.comparing(f -> f.name)).map(Object::toString).collect(Collectors.joining("\n"));
		category.setComment(comment);
		MonkMod.config.save();

		register(0, "meditate", FactoryEntry.requirement("break_wood").setInt("number", 1));
		register(1, "tree",
				FactoryEntry.requirement("break_wood").setInt("number", 10),
				FactoryEntry.ability("mining").setInt("harvest_level", 0).setInt("speed_increase", 0).setStringList("harvest_tools", "", "shovel", "axe"),
				FactoryEntry.ability("strength").setInt("damage", 1));

		register(2, "sunrise",
				FactoryEntry.requirement("mediate_sunrise"),
				FactoryEntry.ability("hunger").setFloat("hunger_chance", 0.5F),
				FactoryEntry.ability("armor").setInt("armor", 1));
		register(3, "squid",
				FactoryEntry.requirement("drown"),
				FactoryEntry.ability("water_breathing_partial"),
				FactoryEntry.ability("armor").setInt("armor", 2));

		register(4, "silverfish",
				FactoryEntry.requirement("break_block").setString("block", "minecraft:stone").setInt("number", 5), FactoryEntry.ability("mining").setInt("harvest_level", 1).setFloat("speed_increase", 2F));

		register(5, "love",
				FactoryEntry.requirement("pet").setInt("number", 20),
				FactoryEntry.ability("tame_animals"),
				FactoryEntry.ability("armor").setInt("armor", 4));
		register(6, "feather",
				FactoryEntry.requirement("sprint").setInt("distance", 200),
				FactoryEntry.ability("swift").setFloat("increase", 0.4F),
				FactoryEntry.ability("jump").setFloat("jump", 1.2F),
				FactoryEntry.ability("strength").setInt("damage", 2));
		register(7, "wolf",
				FactoryEntry.requirement("kill_undead").setInt("kills", 10),
				FactoryEntry.ability("strength").setInt("damage", 4),
				FactoryEntry.ability("armor").setInt("armor", 8));

		register(8, "skeleton",
				FactoryEntry.requirement("arrow_dodge").setInt("dodges", 5),
				FactoryEntry.ability("catch_arrows"),
				FactoryEntry.ability("swift").setFloat("increase", 0.8F));

		register(9, "iron_golem",
				FactoryEntry.requirement("break_block").setString("block", "minecraft:iron_block").setInt("number", 4), FactoryEntry.ability("mining").setInt("harvest_level", 2).setFloat("speed_increase", 5F));

		register(10, "water",
				FactoryEntry.requirement("meditate_water_moon").setInt("stare_time", 20 * 10),
				FactoryEntry.ability("water_walking"));

		register(11, "enderman",
				FactoryEntry.requirement("meditate_endermen"), FactoryEntry.ability("blink"));

		register(12, "blaze",
				FactoryEntry.requirement("walk_fire").setInt("distance", 10),
				FactoryEntry.ability("fire_resistance").setFloat("multiplier", 0.5F), FactoryEntry.ability("lava_protection"));

		register(13, "ghast",
				FactoryEntry.requirement("break_block").setString("block", "minecraft:obsidian").setInt("number", 1),
				FactoryEntry.ability("mining").setInt("harvest_level", 3).setFloat("speed_increase", 9F),
				FactoryEntry.ability("armor").setInt("armor", 14)
		);

		register(14, "creeper",
				FactoryEntry.requirement("kiss_creeper"),
				FactoryEntry.ability("kiss_creeper"),
				FactoryEntry.ability("explosion_resistance").setFloat("multiplier", 0.5F));

		register(15, "evil_eye",
				FactoryEntry.requirement("wither_stare").setInt("stare_time", 200),
				FactoryEntry.ability("withering_stare"));

		register(16, "ocelot",
				FactoryEntry.requirement("fall").setInt("distance", 40),
				FactoryEntry.ability("feather_fall").setFloat("multiplier", 0.2F),
				FactoryEntry.ability("armor").setInt("armor", 18));

		register(17, "bed",
				FactoryEntry.requirement("bedrock_sleep"),
				FactoryEntry.ability("mining").setInt("harvest_level", 5).setFloat("speed_increase", 12F),
				FactoryEntry.ability("strength").setInt("damage", 8),
				FactoryEntry.ability("blindness"));

		register(18, "blind_eye",
				FactoryEntry.requirement("kill_blind").setInt("kills", 5),
				ImmutableList.of(FactoryEntry.ability("strength").setInt("damage", 16)), "blindness");

		register(19, "wither",
				FactoryEntry.requirement("kill_entity_type").setInt("kills", 1).setString("entity_type", "minecraft:wither"),
				FactoryEntry.ability("potion_immunity"),
				FactoryEntry.ability("armor").setInt("armor", 20));

		register(20, "ascend", FactoryEntry.requirement("void_fall"), FactoryEntry.ability("fly"));

		MonkMod.config.save();
	}

	@SafeVarargs
	public static void register(int level, String texture, @Nonnull FactoryEntry<MonkRequirement> requirementFactoryEntry, @Nonnull FactoryEntry<MonkAbility>... toAdd) {
		register(level, texture, requirementFactoryEntry, ImmutableList.copyOf(toAdd));
	}

	public static void register(int level, String texture, FactoryEntry<MonkRequirement> requirementFactoryEntry, List<FactoryEntry<MonkAbility>> toAdd, @Nonnull String... toRemove) {
		String baseCat = getCatName(level);
		MonkMod.config.getString("texture", baseCat, MonkMod.MODID + ":icon/" + texture, "Texture");
		requirementFactoryEntry.writeToConfig(baseCat + ".requirement");
		for (FactoryEntry<MonkAbility> monkAbilityFactoryEntry : toAdd) {
			monkAbilityFactoryEntry.writeToConfig(baseCat + ".abilities_to_add");
		}
		if (toRemove.length != 0) {
			MonkMod.config.getStringList("abilities_to_remove", baseCat, toRemove, "");
		}
	}

	@Nonnull
	protected static String getCatName(int level) {
		return MONK_LEVEL_DATA + ".level_" + level;
	}

	public static void load() {
		whitelist = Stream.of(MonkMod.config.get(MONK_WEAR_ITEMS, "whitelist", new String[]{"minecraft:pumpkin", "minecraft:skull"},
				"Add an item registry name to this section to allow monks to wear/wield it without penalty", false, -1,
				Pattern.compile("^[^:]+:[^:]+$")).getStringList())
				.map(ResourceLocation::new).
						collect(ImmutableSet.toImmutableSet());

		blacklist = Stream.of(MonkMod.config.get(MONK_WEAR_ITEMS, "whitelist", new String[]{},
				"Add an item registry name to this section to prevent monks being able to wear/wield it without penalty", false, -1,
				Pattern.compile("^[^:]+:[^:]+$")).getStringList())
				.map(ResourceLocation::new).
						collect(ImmutableSet.toImmutableSet());

		Property config = MonkMod.config.get("Config", "Config Version", 0, "Config Version (set to 0 to reset configuration to default)");
		boolean config_reset = MonkMod.config.getBoolean("Auto-Reset on Version Update", "Config", true, "If the config version changes, regenerate the config");
		if (config_reset && config.getInt() != MonkMod.config_version) {
			config.set(MonkMod.config_version);
			genDefaultConfig();
		}

		MonkMod.MAX_LEVEL = MonkMod.config.get(MONK_LEVEL_DATA, "Max Level", 20, "Number of Levels").getInt();

		data = new LevelData[MonkMod.MAX_LEVEL + 1];
		for (int i = 0; i <= MonkMod.MAX_LEVEL; i++) {
			LevelData levelData = new LevelData();
			try {

				ConfigCategory reqCat = MonkMod.config.getCategory(getCatName(i) + ".requirement");
				reqCat.setComment("Requirement to unlock level");
				if (reqCat.getChildren().size() != 1) {
					throw new IllegalArgumentException("Level " + i + " does not have 1 requirement");
				} else {
					ConfigCategory next = reqCat.getChildren().iterator().next();
					levelData.requirement = load(Factory.requirementFactories, next);
				}

				ConfigCategory abilityCat = MonkMod.config.getCategory(getCatName(i) + ".abilities_to_add");
				abilityCat.setComment("Abilities to gain in this level");
				for (ConfigCategory configCategory : abilityCat.getChildren()) {
					levelData.toAdd.add(load(Factory.abilityFactories, configCategory));
				}

				String[] abilities_to_removes = MonkMod.config.getStringList("abilities_to_remove", getCatName(i), new String[]{}, "Abilities to remove in this level");
				Collections.addAll(levelData.toRemove, abilities_to_removes);

				levelData.texture = MonkMod.config.getString("texture", getCatName(i), MonkMod.MODID + ":icon/meditate", "Texture");
			} catch (Exception err) {
				throw new RuntimeException("Error loading Level " + i, err);
			}
			data[i] = levelData;
		}

		HashMap<String, MonkAbility> abilityHashMap = new HashMap<>();
		for (int level = 0; level < data.length; level++) {
			LevelData datum = data[level];
			MonkLevelManager.registerRequirement(level, datum.requirement);


			for (String s : datum.toRemove) {
				abilityHashMap.remove(s);
			}
			for (MonkAbility ability : datum.toAdd) {
				abilityHashMap.put(ability.name, ability);
			}

			for (MonkAbility ability : abilityHashMap.values()) {
				MonkLevelManager.register(level, ability);
			}
		}

	}


	public static <T> T load(Map<String, Factory<T>> factoryMap, ConfigCategory category) {
		String name = category.getName();
		Factory<T> tFactory = factoryMap.get(name);
		if (tFactory == null) throw new IllegalArgumentException("Cannot find " + name);
		Factory.Parameters parameters = new Factory.Parameters() {
			@Override
			public float getFloat(String key) {
				checkKey(key);
				return (float) category.get(key).getDouble();
			}

			private void checkKey(String key) {
				if (!category.containsKey(key))
					throw new IllegalArgumentException("key " + key + " in name is not defined");
			}

			@Override
			public float getFloat(String key, float _default) {
				return category.containsKey(key) ? (float) category.get(key).getDouble() : _default;
			}

			@Override
			public int getInt(String key) {
				checkKey(key);
				return category.get(key).getInt();
			}

			@Override
			public int getInt(String key, int _default) {
				return category.containsKey(key) ? category.get(key).getInt() : _default;
			}

			@Override
			public String getString(String key) {
				checkKey(key);
				return category.get(key).getString();
			}

			@Override
			public String getString(String key, String _default) {
				return category.containsKey(key) ? category.get(key).getString() : _default;
			}

			@Override
			public String[] getStringList(String key) {
				checkKey(key);
				return category.get(key).getStringList();
			}

			@Override
			public String[] getStringList(String key, String[] _defaults) {
				return category.containsKey(key) ? category.get(key).getStringList() : _defaults;
			}
		};

		return tFactory.function.apply(parameters);
	}


	public static class LevelData {
		@Nonnull
		public List<MonkAbility> toAdd = new ArrayList<>();
		@Nonnull
		public List<String> toRemove = new ArrayList<>();
		@Nullable
		public MonkRequirement requirement = null;
		@Nullable
		public String texture = null;
	}


}
