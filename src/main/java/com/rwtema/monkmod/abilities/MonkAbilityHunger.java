package com.rwtema.monkmod.abilities;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

public class MonkAbilityHunger extends MonkAbility {
	private static final Field prevFoodLevel = ReflectionHelper.findField(FoodStats.class, "prevFoodLevel", "field_75124_e");

	public MonkAbilityHunger(String name) {
		super(name, 2);
	}

	@Override
	public void tickServer(EntityPlayerMP player, int level) {
		super.tickServer(player, level);
		FoodStats foodStats = player.getFoodStats();
		if (level == 1) {
			foodStats.addStats(1, 0.5F);
		} else {
			Integer prevLevel;
			try {
				prevLevel = (Integer) prevFoodLevel.get(foodStats);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (foodStats.getFoodLevel() != prevLevel) {
				if (player.world.rand.nextBoolean()) {
					foodStats.setFoodLevel(prevLevel);
				}
			}
		}
	}
}
