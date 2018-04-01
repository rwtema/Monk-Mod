package com.rwtema.monkmod.abilities;

import com.google.common.collect.ImmutableSet;
import com.rwtema.monkmod.MonkManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

public class Abilities {
	public static final MonkAbilityHunger HUNGER = new MonkAbilityHunger("hunger");

	public static final MonkAbilityWater WATER_BREATHING = new MonkAbilityWater("water", 2);

	public static final MonkAbilityAttribute STRENGTH = new MonkAbilityAttribute("punch", SharedMonsterAttributes.ATTACK_DAMAGE, new double[]{
			1, 2, 4, 8, 16, 32
	}, 0) {

		@Override
		public boolean canApply(EntityPlayer player) {
			return isUnarmed(player);
		}
	};

	public static final MonkAbilityAttribute HEALTH = new MonkAbilityAttribute("health", SharedMonsterAttributes.MAX_HEALTH, new double[]{1.1, 1.2, 1.5, 2}, 1) {

		@Override
		public boolean canApply(EntityPlayer player) {
			return true;
		}
	};

	public static final MonkAbilityAttribute SPEED = new MonkAbilityAttribute("swift", SharedMonsterAttributes.MOVEMENT_SPEED, new double[]{0.1, 0.2, 0.4, 0.6, 0.8, 1}, 1) {
		@Override
		public boolean canApply(EntityPlayer player) {
			return isUnarmored(player);
		}

		@Override
		public double getAmount(int level, EntityPlayer player) {
			double amount = super.getAmount(level, player);
			return player.isSprinting() ? amount : amount / 4;
		}
	};

	public static final MonkAbilityAttribute ARMOR = new MonkAbilityAttribute("hardskin", SharedMonsterAttributes.ARMOR, new double[]{
			4, 7, 11, 15, 20
	}, 0) {
		@Override
		public boolean canApply(EntityPlayer player) {
			return isUnarmored(player);
		}
	};

	public static final MonkAbilityAttribute ARMOR_TOUGH = new MonkAbilityAttribute("strong_skin", SharedMonsterAttributes.ARMOR, new double[]{
			2, 4, 8
	}, 0) {
		@Override
		public boolean canApply(EntityPlayer player) {
			return isUnarmored(player);
		}
	};

	public static final MonkCatchArrows ARROW_CATCH = new MonkCatchArrows("arrow_catch");

	public static final MonkProtection FEATHER_FALLING = new MonkProtection("feather_fall", 2) {
		@Override
		public float getAbsorbtion(DamageSource source, EntityPlayer player, int abilityLevel) {
			switch (abilityLevel) {
				case 0: return 0.5F;
				case 1: return  0.2F;
				default:
				case 2: return 0;
			}
		}

		@Override
		public boolean canHandle(EntityPlayer player, DamageSource source) {
			return source == DamageSource.FALL;
		}
	};


	public static final MonkAbility MINING;

	static {
		float[] speeds = {2, 4, 6, 8, 12, 16, 20};
		Set<String> validBlocks = ImmutableSet.of("", "pickaxe", "shovel", "axe");
		MINING = new MonkAbility("mine_speed", speeds.length) {
			@SubscribeEvent
			public void onMine(PlayerEvent.BreakSpeed event) {
				int abilityLevel = MonkManager.getAbilityLevel(event.getEntityPlayer(), this);
				if (abilityLevel == -1) return;

				float speedMultiplier = speeds[abilityLevel];

				float oldSpeed = Math.min(event.getOriginalSpeed(), 1);

				event.setNewSpeed(Math.max(event.getNewSpeed(), speedMultiplier * oldSpeed));
			}

			@SubscribeEvent
			public void onHarvest(PlayerEvent.HarvestCheck event) {
				int abilityLevel = MonkManager.getAbilityLevel(event.getEntityPlayer(), this);
				if (abilityLevel == -1) return;

				IBlockState targetBlock = event.getTargetBlock();
				String harvestTool = targetBlock.getBlock().getHarvestTool(targetBlock);
				if (harvestTool != null && !validBlocks.contains(harvestTool))
					return;

				if (targetBlock.getBlock().getHarvestLevel(targetBlock) <= abilityLevel) {
					event.setCanHarvest(true);
				}
			}
		};
	}
}
