package com.rwtema.monkmod.abilities;

import com.google.common.collect.ImmutableSet;
import com.rwtema.monkmod.MonkManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

public class Abilities {
	public static final MonkAbilityHunger HUNGER = new MonkAbilityHunger("hunger");

	public static final MonkAbilityWater WATER_BREATHING = new MonkAbilityWater("water", 2);
	public static final MonkAbilityWitheringStare WITHERING_STARE = new MonkAbilityWitheringStare("withering_stare");

	public static final MonkAbilityWalkOnWater WALK_ON_WATER = new MonkAbilityWalkOnWater("water_walking");

	public static final MonkAbilityAttribute STRENGTH = new MonkAbilityAttribute("punch", SharedMonsterAttributes.ATTACK_DAMAGE, new double[]{
			1, 2, 4, 8, 16, 32
	}, 0) {

		@Override
		public boolean canApply(EntityPlayer player) {
			return isUnarmed(player);
		}
	};

	public static final MonkAbilityAttribute HEALTH = new MonkAbilityAttribute("health", SharedMonsterAttributes.MAX_HEALTH, new double[]{0.2, 0.4, 0.6, 0.8, 1}, 1) {

		@Override
		public boolean canApply(EntityPlayer player) {
			return true;
		}
	};

	public static final MonkAbilityAttribute SPEED = new MonkAbilityAttribute("swift", SharedMonsterAttributes.MOVEMENT_SPEED,
			new double[]{0.1, 0.25, 0.5, 1}, 1) {
		@Override
		public boolean canApply(EntityPlayer player) {
			return isUnarmored(player);
		}

		@Override
		public double getAmount(int level, EntityPlayer player) {
			double amount = super.getAmount(level, player);
			return player.isSprinting() ? amount * 2 : amount;
		}

		@SubscribeEvent
		public void overrideFOV(FOVUpdateEvent event) {
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			if (player == null) return;
			int abilityLevel = MonkManager.getAbilityLevel(player, this);
			if (abilityLevel == -1) return;

			IAttributeInstance iattributeinstance = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
			double oldMult = multiplier(player, iattributeinstance);
			AttributeModifier modifier = iattributeinstance.getModifier(uuid);
			if (modifier == null) return;
			iattributeinstance.removeModifier(modifier);
			double newMult = multiplier(player, iattributeinstance);
			iattributeinstance.applyModifier(modifier);
			event.setNewfov((float) ((event.getFov() / oldMult) * newMult));
		}

		private double multiplier(EntityPlayerSP player, IAttributeInstance iattributeinstance) {
			return (iattributeinstance.getAttributeValue() / (double) player.capabilities.getWalkSpeed() + 1.0D) / 2.0D;
		}
	};

	public static final MonkAbilityBlink BLINK = new MonkAbilityBlink("blink");

	public static final MonkAbilityAttribute ARMOR = new MonkAbilityAttribute("hardskin", SharedMonsterAttributes.ARMOR, new double[]{
			2, 4, 7, 11, 14, 15, 20
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

	public static final MonkAbilityTameAnimals PET = new MonkAbilityTameAnimals("tame_animal");

	public static final MonkAbilityCreeperKiss KISS = new MonkAbilityCreeperKiss("creeper_kiss");

	public static final MonkProtection EXPLOSION = new MonkProtection("explosion") {
		@Override
		public float getAbsorbtion(DamageSource source, EntityPlayer player, int abilityLevel) {
			return 0.3F;
		}

		@Override
		public boolean canHandle(EntityPlayer player, DamageSource source) {
			return source.isExplosion();
		}
	};

	public static final MonkCatchArrows ARROW_CATCH = new MonkCatchArrows("arrow_catch");

	public static final MonkProtection FIRE = new MonkProtectionFire();

	public static final MonkProtection FEATHER_FALLING = new MonkProtection("feather_fall", 2) {
		@Override
		public float getAbsorbtion(DamageSource source, EntityPlayer player, int abilityLevel) {
			switch (abilityLevel) {
				case 0:
					return 0.2F;
				default:
				case 1:
					return 0;
			}
		}

		@Override
		public boolean canHandle(EntityPlayer player, DamageSource source) {
			return source == DamageSource.FALL;
		}
	};

	public static final MonkAbility JUMP = new MonkAbilityJump("jump");

	public static final MonkAbility BLIND = new MonkAbilityBlindness("blindness", 2);

	public static final MonkAbility FLY = new MonkAbilityFly("fly");

	public static final MonkAbility POTION_IMMUNITY = new MonkAbilityHeal("potionImmunity");


	public static final MonkAbility MINING;

	static {
		float[] speeds = {2, 4, 7, 12, 32};
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
				if (harvestTool != null && (abilityLevel == 0 && "pickaxe".equals(harvestTool) || !validBlocks.contains(harvestTool)))
					return;

				if (targetBlock.getBlock().getHarvestLevel(targetBlock) <= abilityLevel) {
					event.setCanHarvest(true);
				}
			}
		};
	}

}
