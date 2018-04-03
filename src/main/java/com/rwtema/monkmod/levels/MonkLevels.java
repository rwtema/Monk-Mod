package com.rwtema.monkmod.levels;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.rwtema.monkmod.abilities.MonkAbility;
import com.rwtema.monkmod.advancements.MonkRequirement;
import com.rwtema.monkmod.advancements.criteria.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

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

		registerRequirement(new MonkRequirementBreakBlockBareHanded(0, (world, pos, state) -> state.getBlock().isWood(world, pos), 1));

		// Sapling
		// Punch 50 tree logs with bare hands.
		register(1, MINING, STRENGTH);
		registerRequirement(new MonkRequirementBreakBlockBareHanded(1, (world, pos, state) -> state.getBlock().isWood(world, pos), 10));

		// Chicken
		// Meditate while watching a sunset or sunrise.
		register(2, HUNGER);
		register(2, ARMOR);
		registerRequirement(new MonkRequirementSunriseSunset(2, 10 * 20));

		// Squid
		// Swim to the bottom of an ocean.
		register(3, WATER_BREATHING);
		register(3, ARMOR);
		registerRequirement(new MonkRequirementOcean(3, 20));

		// Silverfish
		// Break 5 stone blocks with your bare hands
		registerRequirement(new MonkRequirementBreakBlockBareHanded(4, (world, pos, state) -> state.getBlock() == Blocks.STONE, 5));
		register(4, MINING);
		register(4, ARMOR, STRENGTH);


		// Animal Friend
		// Pet one of each vanilla animal (pet animals by right clicking on them with an empty hand)
		registerRequirement(new MonkRequirementPet(5));
		register(5, PET);

		// Ocelot
		// From sunrise to sunset, run a mile in one day.
		registerRequirement(new MonkRequirementWalk(6, 100) {
			@Override
			public boolean satisfiesRequirements(EntityPlayerMP player) {
				return player.isSprinting() && super.satisfiesRequirements(player);
			}
		});
		register(6, SPEED, JUMP);
//		register(5);

		// Wolf
		// Kill 10 hostile mobs with your bare fists
		registerRequirement(new MonkRequirementKill(7, 5) {

			@Override
			protected boolean isValidEntity(LivingDeathEvent event) {
				return event.getEntityLiving().isEntityUndead();
			}
		});
		register(7, STRENGTH);
		register(7, ARMOR);

		// Golem
		// Break an iron storage block with your bare fists
		registerRequirement(new MonkRequirementBreakBlockBareHanded(8, (world, pos, state) -> state.getBlock() == Blocks.IRON_BLOCK, 1));
		register(8, MINING, STRENGTH);

		// Blaze
		// Walk through fire.
		registerRequirement(new MonkRequirementWalk(9, 10) {
			@Override
			protected void onGrant(EntityPlayerMP player) {
				player.extinguish();
				player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 20 * 10, 2));
			}

			@Override
			public boolean satisfiesRequirements(EntityPlayerMP player) {
				return ((player.isImmuneToFire() || player.isBurning()) && player.world.isFlammableWithin(player.getEntityBoundingBox()))
						&& super.satisfiesRequirements(player);
			}
		});
		register(9, FIRE);
		register(9, ARMOR, SPEED);


		// Ghast
		// Break obsidian with bare hands
		registerRequirement(new MonkRequirementBreakBlockBareHanded(10, (world, pos, state) -> state.getBlock() == Blocks.OBSIDIAN, 1));
		register(10, MINING);
		register(10, ARMOR, STRENGTH);


		// Leap of Faith
		registerRequirement(new MonkRequirementDeath(11) {
			@Override
			protected boolean isValidSourceOfDeath(LivingDeathEvent event) {
				if (event.getSource() != DamageSource.FALL) return false;
				EntityLivingBase entityLiving = event.getEntityLiving();
				int j6 = MathHelper.floor(entityLiving.posX);
				int i1 = MathHelper.floor(entityLiving.posY - 0.20000000298023224D);
				int k6 = MathHelper.floor(entityLiving.posZ);
				BlockPos blockpos = new BlockPos(j6, i1, k6);
				IBlockState iblockstate = entityLiving.world.getBlockState(blockpos);

				return iblockstate.getBlock() == Blocks.HAY_BLOCK;
			}
		});
		register(11, SPEED, HEALTH, FEATHER_FALLING);

		// Creeper
		// Kiss a creeper
		registerRequirement(new MonkRequirementKissCreeper(12));
		register(12, EXPLOSION, KISS, HEALTH);

		// Dodge Arrows
		registerRequirement(new MonkRequirementArrow(13, 5));
		register(13, ARROW_CATCH, HEALTH);

		// Ocean
		registerRequirement(new MonkRequirementWaterMeditation(14, 10 * 20));
		register(14, WALK_ON_WATER);

		// Wither Skeleton
		registerRequirement(new MonkRequirementStare(15));
		register(15, WITHERING_STARE, HEALTH);


		// Enderman
		registerRequirement(new MonkRequirementMeditateEndermen(16, 20 * 60 * 20));
		register(16, BLINK);

		registerRequirement(new MonkRequirementBedrockSleep(17));
		register(17,BLIND,  MINING, STRENGTH, ARMOR, HEALTH);

		// Kill entities while blind
		registerRequirement(new MonkRequirementKill(18, 5) {
			@Override
			protected boolean isValidEntity(LivingDeathEvent event) {
				return event.getEntity() instanceof IMob;
			}

			@Override
			protected void onGrant(EntityPlayerMP player) {
				super.onGrant(player);
				player.removePotionEffect(MobEffects.BLINDNESS);
			}
		});
		register(18, BLIND, HUNGER);

		registerRequirement(new MonkRequirementEnemyDefeat<>(19, EntityWither.class));
		register(19, POTION_IMMUNITY);

		registerRequirement(new MonkRequirementDescentAscent(20) );
		register(20, FLY);

	}

	private void registerRequirement(MonkRequirement requirement) {

		levelUpRequirements.merge(requirement.levelToGrant, requirement, (monkRequirement, monkRequirement2) -> {
			throw new RuntimeException("Duplicate levels");
		});
	}
}
