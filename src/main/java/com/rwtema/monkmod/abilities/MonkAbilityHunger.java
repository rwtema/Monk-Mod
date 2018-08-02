package com.rwtema.monkmod.abilities;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.Locale;

public class MonkAbilityHunger extends MonkAbility {
	private static final Field prevFoodLevel = ReflectionHelper.findField(FoodStats.class, "prevFoodLevel", "field_75124_e");
	private final float chance;

	public MonkAbilityHunger(float chance) {
		super("hunger");
		this.chance = 1 - chance;
	}

	@Override
	public void tickServer(@Nonnull EntityPlayerMP player) {
		super.tickServer(player);
		FoodStats foodStats = player.getFoodStats();

		Integer prevLevel;
		try {
			prevLevel = (Integer) prevFoodLevel.get(foodStats);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (foodStats.getFoodLevel() != prevLevel) {
			if (player.world.rand.nextFloat() < chance) {
				foodStats.setFoodLevel(prevLevel);
			}
		}
	}

	@Nonnull
	@Override
	protected String[] args() {
		return new String[]{NumberFormat.getPercentInstance(Locale.UK).format(1 - chance)};
	}
}
