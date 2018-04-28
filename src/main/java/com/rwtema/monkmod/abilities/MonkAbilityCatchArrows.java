package com.rwtema.monkmod.abilities;

import com.rwtema.monkmod.MonkManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Method;

public class MonkAbilityCatchArrows extends MonkAbility {
	private Method getArrowStackMethod = ReflectionHelper.findMethod(EntityArrow.class, "getArrowStack", "func_184550_j");

	public MonkAbilityCatchArrows() {
		super("catch_arrows");
	}

	@SubscribeEvent
	public void onImpact(ProjectileImpactEvent.Arrow event) {
		EntityArrow arrow = event.getArrow();
		if (event.getArrow().world.isRemote) return;

		RayTraceResult rayTraceResult = event.getRayTraceResult();
		Entity entityHit = rayTraceResult.entityHit;
		if (entityHit instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) entityHit;
			if (!MonkManager.getAbilityLevel(player, this)) return;
			if (!isUnarmed(player)) {
				return;
			}

			Vec3d vec3d = new Vec3d(event.getArrow().posX, arrow.posY, arrow.posZ);

			Vec3d vec3d1 = player.getLook(1.0F);
			Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(player.posX, player.posY, player.posZ)).normalize();
			vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

			if (vec3d2.dotProduct(vec3d1) > 0.2D) {
				return;
			}

			if (arrow.pickupStatus == EntityArrow.PickupStatus.ALLOWED) {
				ItemStack dropStack;
				try {
					dropStack = (ItemStack) getArrowStackMethod.invoke(arrow);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				if (!player.inventory.addItemStackToInventory(dropStack)) {
					arrow.entityDropItem(dropStack, 0.1F);
				}
			}
			arrow.setDead();
			event.setCanceled(true);
		}


	}
}
