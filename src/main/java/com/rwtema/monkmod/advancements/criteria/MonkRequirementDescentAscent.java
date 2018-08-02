package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import javax.annotation.Nonnull;

public class MonkRequirementDescentAscent extends MonkRequirementDeath {
	public MonkRequirementDescentAscent() {
		super("void_fall");
	}

	@Override
	protected void onDeathAvoid(@Nonnull EntityPlayerMP player, @Nonnull MonkData monkData) {
		player.setHealth(20);
		int progress = monkData.getProgress();
		if (monkData.increase(1, 15)) {
			super.onDeathAvoid(player, monkData);
			player.changeDimension(0, (world, entity, yaw) -> entity.moveToBlockPosAndAngles(world.getTopSolidOrLiquidBlock(world.getSpawnPoint()), yaw, entity.rotationPitch));
		} else {
			player.sendMessage(new TextComponentTranslation("monk.final." + progress));
		}

	}

	@Override
	protected boolean isValidSourceOfDeath(@Nonnull LivingDeathEvent event) {
		return event.getSource() == DamageSource.OUT_OF_WORLD && event.getEntity().posY < -60;
	}
}
