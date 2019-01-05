package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

public class MonkRequirementWaterMeditation extends MonkRequirementTick {
	public MonkRequirementWaterMeditation(int stareTime) {
		super("meditate_water_moon", stareTime);
	}

	@Override
	protected void doTick(@Nonnull EntityPlayerMP player, @Nonnull MonkData monkData) {
		Entity ridingEntity = player.getRidingEntity();
		if (ridingEntity == null || !(ridingEntity.getClass().getName().equals("net.minecraft.entity.item.EntityBoat"))) {
			monkData.resetProgress();
			return;
		}

		double celestialAngle = player.world.getCelestialAngle(0) * Math.PI * 2;
		double sunDist = Math.sin(celestialAngle);
		double sunHeight = Math.cos(celestialAngle);
		if (sunHeight < -0.2) {
			Vec3d vec3d1 = player.getLook(1.0F);
			Vec3d sunDir = new Vec3d(-sunDist, sunHeight, 0);

			if (sunDir.dotProduct(vec3d1) < -0.99) {
				if (monkData.increase(1, requirementLimit)) {
					grantLevel(player);
				}
			} else {
				monkData.resetProgress();
			}
		} else {
			monkData.resetProgress();
		}
	}
}
