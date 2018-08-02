package com.rwtema.monkmod.factory;

import com.google.common.collect.Sets;
import com.rwtema.monkmod.abilities.*;
import com.rwtema.monkmod.advancements.MonkRequirement;
import com.rwtema.monkmod.advancements.criteria.*;
import net.minecraft.block.Block;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Factory<T> {
	@Nonnull
	public static final Map<String, Factory<MonkAbility>> abilityFactories = new HashMap<>();
	@Nonnull
	public static final Map<String, Factory<MonkRequirement>> requirementFactories = new HashMap<>();
	public static boolean shouldRegister = true;
	public final String name;
	public final Function<Parameters, ? extends T> function;
	public final List<Parameter> parameterList;

	public Factory(String name, Function<Parameters, ? extends T> function, List<Parameter> parameterList) {
		this.name = name;
		this.function = function;
		this.parameterList = parameterList;
	}

	public static void init() {
		Factory.registerAbility(parameters -> new MonkAbilityArmor(parameters.getFloat("armor")));
		Factory.registerAbility(parameters -> new MonkAbilityArmorToughness(parameters.getFloat("armor_toughness")));
		Factory.registerAbility(parameters -> new MonkAbilityBlindness());
		Factory.registerAbility(parameters -> new MonkAbilityBlink());
		Factory.registerAbility(parameters -> new MonkAbilityCatchArrows());
		Factory.registerAbility(parameters -> new MonkAbilityCreeperKiss());
		Factory.registerAbility(parameters -> new MonkAbilityExplosionProof(parameters.getFloat("multiplier")));
		Factory.registerAbility(parameters -> new MonkAbilityFeatherFalling(parameters.getFloat("multiplier")));
		Factory.registerAbility(parameters -> new MonkAbilityFly());
		Factory.registerAbility(parameters -> new MonkAbilityHealPotionEffects());
		Factory.registerAbility(parameters -> new MonkAbilityHealth(parameters.getFloat("health_increase")));
		Factory.registerAbility(parameters -> new MonkAbilityHunger(parameters.getFloat("hunger_chance")));
		Factory.registerAbility(parameters -> new MonkAbilityJump(parameters.getFloat("jump")));
		Factory.registerAbility(parameters -> new MonkAbilityMining(parameters.getInt("harvest_level"), parameters.getFloat("speed_increase"), Sets.newHashSet(parameters.getStringList("harvest_tools", new String[]{"", "pickaxe", "shovel", "axe"}))));
		Factory.registerAbility(parameters -> new MonkAbilityProtectionFire(parameters.getFloat("multiplier")));
		Factory.registerAbility(parameters -> new MonkAbilitiyProtectionLava());
		Factory.registerAbility(parameters -> new MonkAbilitySpeed(parameters.getFloat("increase")));
		Factory.registerAbility(parameters -> new MonkAbilityStrength(parameters.getFloat("damage")));
		Factory.registerAbility(parameters -> new MonkAbilityTameAnimals());
		Factory.registerAbility(parameters -> new MonkAbilityWalkOnWater());
		Factory.registerAbility(parameters -> new MonkAbilityWater());
		Factory.registerAbility(parameters -> new MonkAbilityFullWaterBreathing());
		Factory.registerAbility(parameters -> new MonkAbilityWitheringStare());

		Factory.registerRequirement(parameters -> {
			ResourceLocation resourceLocation = new ResourceLocation(parameters.getString("block"));
			return new MonkRequirementBreakBlockBareHanded("break_block", (world, pos, state) -> resourceLocation.equals(state.getBlock().getRegistryName()), parameters.getInt("number")) {
				@Nonnull
				@Override
				protected Object[] args() {
					Block block = Block.REGISTRY.getObject(resourceLocation);
					return new Object[]{requirementLimit, new ItemStack(block).getTextComponent()};
				}
			};
		});
		Factory.registerRequirement(parameters -> new MonkRequirementBreakBlockBareHanded("break_wood", (world, pos, state) -> state.getBlock().isWood(world, pos), parameters.getInt("number")));
		Factory.registerRequirement(parameters -> new MonkRequirementOcean(parameters.getInt("swim_time", 280)));
		Factory.registerRequirement(parameters -> new MonkRequirementSunriseSunset(10 * 20));
		Factory.registerRequirement(parameters -> new MonkRequirementPet(parameters.getInt("number")));

		Factory.registerRequirement(parameters -> new MonkRequirementWalk(parameters.getInt("distance"), "sprint") {
			@Override
			public boolean satisfiesRequirements(@Nonnull EntityPlayerMP player) {
				return player.isSprinting() && super.satisfiesRequirements(player);
			}
		});

		Factory.registerRequirement(parameters -> new MonkRequirementKill("kill_undead", parameters.getInt("kills")) {
			@Override
			protected boolean isValidEntity(@Nonnull LivingDeathEvent event) {
				return event.getEntityLiving().isEntityUndead();
			}
		});
		Factory.registerRequirement(parameters -> new MonkRequirementWalk(parameters.getInt("distance"), "walk_fire") {
			@Override
			protected void onGrant(@Nonnull EntityPlayerMP player) {
				player.extinguish();
				player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 20 * 10, 2));
			}

			@Override
			public boolean satisfiesRequirements(@Nonnull EntityPlayerMP player) {
				return ((player.isImmuneToFire() || player.isBurning()) && player.world.isFlammableWithin(player.getEntityBoundingBox()))
						&& super.satisfiesRequirements(player);
			}
		});


		Factory.registerRequirement(parameters -> new MonkRequirementFall(parameters.getInt("distance")));
		Factory.registerRequirement(parameters -> new MonkRequirementKissCreeper());
		Factory.registerRequirement(parameters -> new MonkRequirementArrow(parameters.getInt("dodges")));
		Factory.registerRequirement(parameters -> new MonkRequirementWaterMeditation(parameters.getInt("stare_time")));
		Factory.registerRequirement(parameters -> new MonkRequirementStare(parameters.getInt("stare_time")));
		Factory.registerRequirement(parameters -> new MonkRequirementMeditateEndermen(parameters.getInt("time", 30 * 20)));
		Factory.registerRequirement(parameters -> new MonkRequirementBedrockSleep());
		Factory.registerRequirement(parameters -> new MonkRequirementKill("kill_hostile", parameters.getInt("kills")) {
			@Override
			protected boolean isValidEntity(@Nonnull LivingDeathEvent event) {
				return event.getEntity() instanceof IMob;
			}
		});
		Factory.registerRequirement(parameters -> new MonkRequirementKill("kill_blind", parameters.getInt("kills")) {
			@Override
			protected boolean isValidEntity(@Nonnull LivingDeathEvent event) {
				return event.getEntity() instanceof IMob;
			}

			@Override
			protected void onGrant(@Nonnull EntityPlayerMP player) {
				super.onGrant(player);
				player.removePotionEffect(MobEffects.BLINDNESS);
			}
		});
		Factory.registerRequirement(parameters -> new MonkRequirementEnemyDefeat("kill_entity_type", parameters.getInt("kills"), new ResourceLocation(parameters.getString("entity_type"))));


		Factory.registerRequirement(parameters -> new MonkRequirementDescentAscent());

	}

	public static void registerAbility(@Nonnull Function<Parameters, MonkAbility> function) {
		register(abilityFactories, function);
	}

	public static void registerRequirement(@Nonnull Function<Parameters, MonkRequirement> function) {
		register(requirementFactories, function);
	}

	public static <T extends IFactoryMade> void register(Map<String, Factory<T>> factoryMap, Function<Parameters, T> function) {
		ParameterLoader parameterLoader = new ParameterLoader();
		shouldRegister = false;
		T apply = function.apply(parameterLoader);
		shouldRegister = true;
		MinecraftForge.EVENT_BUS.unregister(apply);
		String key = apply.getKey();
		factoryMap.put(key, new Factory<>(key, function, parameterLoader.parameterList));
	}

	@Nonnull
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		if (!parameterList.isEmpty()) {
			builder.append(" <");
			builder.append(parameterList.stream().map(parameter -> {
				StringBuilder b = new StringBuilder();
				switch (parameter.type) {
					case INTEGER:
						b.append("I:");
						break;
					case FLOAT:
						b.append("D:");
						break;
					case STRING:
						b.append("S:");
						break;
					case STRINGLIST:
						b.append("S[]:");
						break;
				}
				b.append(parameter.name);
				if (parameter._default != null) {
					b.append("=\"");
					b.append(parameter._default);
					b.append("\"");
				}
				return b;
			}).collect(Collectors.joining(" ")));
			builder.append(">");
		}
		return builder.toString();
	}

	public enum Type {
		INTEGER,
		FLOAT,
		STRING,
		STRINGLIST
	}

	public static abstract class Parameters {
		public abstract float getFloat(String key);

		public abstract float getFloat(String key, float _default);

		public abstract int getInt(String key);

		public abstract int getInt(String key, int _default);

		public abstract String getString(String key);

		public abstract String getString(String key, String _default);

		public String[] getStringList(String key) {
			String string = getString(key);
			return string.split(",");
		}

		public String[] getStringList(String key, String[] _defaults) {
			return getString(key, Stream.of(_defaults).collect(Collectors.joining(","))).split(",");
		}
	}

	private static class ParameterLoader extends Parameters {
		@Nonnull
		final
		List<Parameter> parameterList = new ArrayList<>();

		@Override
		public float getFloat(String key) {
			parameterList.add(new Parameter(key, Type.FLOAT, null));
			return 0;
		}

		@Override
		public float getFloat(String key, float _default) {
			parameterList.add(new Parameter(key, Type.FLOAT, Float.toString(_default)));
			return 0;
		}

		@Override
		public int getInt(String key) {
			parameterList.add(new Parameter(key, Type.INTEGER, null));
			return 0;
		}

		@Override
		public int getInt(String key, int _default) {
			parameterList.add(new Parameter(key, Type.INTEGER, Integer.toString(_default)));
			return 0;
		}

		@Nonnull
		@Override
		public String getString(String key) {
			parameterList.add(new Parameter(key, Type.STRING, null));
			return "";
		}

		@Nonnull
		@Override
		public String getString(String key, String _default) {
			parameterList.add(new Parameter(key, Type.STRING, _default));
			return "";
		}

		@Nonnull
		@Override
		public String[] getStringList(String key) {
			parameterList.add(new Parameter(key, Type.STRINGLIST, null));
			return new String[0];
		}

		@Nonnull
		@Override
		public String[] getStringList(String key, String[] _defaults) {
			parameterList.add(new Parameter(key, Type.STRINGLIST, Stream.of(_defaults).collect(Collectors.joining(","))));
			return new String[0];
		}
	}

	public static class Parameter {
		public final String name;
		public final Type type;
		@Nullable
		public final String _default;

		public Parameter(String name, Type type, @Nullable String aDefault) {
			this.name = name;
			this.type = type;
			_default = aDefault;
		}
	}
}

