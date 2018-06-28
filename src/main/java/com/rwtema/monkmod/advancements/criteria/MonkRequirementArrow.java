package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.LinkedList;
import java.util.WeakHashMap;

public class MonkRequirementArrow extends MonkRequirementTick {
	private final WeakHashMap<EntityPlayerMP, Collection<EntityArrow>> arrows = new WeakHashMap<>();

	public MonkRequirementArrow(int numDodges) {
		super("arrow_dodge", numDodges);
	}

	@Override
	protected void doTick(EntityPlayerMP player, MonkData monkData) {
		Collection<EntityArrow> entityArrows = arrows.get(player);
		if (entityArrows == null) return;

		entityArrows.removeIf(
				arrow -> {
					if (arrow.onGround || arrow.isDead) {
						return true;
					}
					Entity shootingEntity = arrow.shootingEntity;
					if (shootingEntity == null) return true;
					Vec3d a = new Vec3d(player.posX - shootingEntity.posX, player.posY - shootingEntity.posY, player.posZ - shootingEntity.posZ).normalize();
					Vec3d b = new Vec3d(player.posX - arrow.posX, player.posY - arrow.posY, player.posZ - arrow.posZ).normalize();
					double v = a.dotProduct(b);

					if (v < -0.1) {
						monkData.increase(1, requirementLimit);

						return true;
					}
					return false;

				}
		);
		if (monkData.getProgress() >= requirementLimit) {
			arrows.remove(player);
			grantLevel(player);
		}
	}

	@SubscribeEvent
	public void onImpact(ProjectileImpactEvent.Arrow event) {
		if (event.getArrow().world.isRemote) return;

		RayTraceResult rayTraceResult = event.getRayTraceResult();
		Entity entityHit = rayTraceResult.entityHit;
		if (entityHit instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP) entityHit;
			MonkData monkData = MonkManager.get(playerMP);
			if (monkData.getLevel() == (this.levelToGrant - 1)) {
				monkData.resetProgress();
			}
		}
		arrows.values().forEach(entityArrows -> entityArrows.remove(event.getArrow()));
	}


	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		if (!entity.world.isRemote && entity instanceof EntityArrow) {
			EntityArrow arrow = (EntityArrow) entity;
			Entity shootingEntity = arrow.shootingEntity;
			if (shootingEntity instanceof EntitySkeleton) {
				EntityLivingBase attackTarget = ((EntitySkeleton) shootingEntity).getAttackTarget();
				if (attackTarget instanceof EntityPlayerMP) {
					EntityPlayerMP playerMP = (EntityPlayerMP) attackTarget;
					MonkData monkData = MonkManager.get(playerMP);
					if (monkData.getLevel() == (this.levelToGrant - 1)) {
						arrows.computeIfAbsent(playerMP, p -> new LinkedList<>()).add(arrow);
					}
				}
			}
		}
	}
}
