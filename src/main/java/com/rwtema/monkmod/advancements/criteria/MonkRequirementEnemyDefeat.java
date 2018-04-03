package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.abilities.MonkAbility;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.WeakHashMap;

public class MonkRequirementEnemyDefeat<T extends EntityLivingBase> extends MonkRequirementTick {
	private final WeakHashMap<T, WeakHashMap<EntityPlayerMP, Entry>> entryTracker = new WeakHashMap<>();
	private final Class<T> clazz;

	public MonkRequirementEnemyDefeat(int level, Class<T> clazz) {
		super(level);
		this.clazz = clazz;
	}

	@Override
	protected void doTick(EntityPlayerMP player, MonkData monkData) {
		if (!entryTracker.isEmpty()) {
			if (!MonkAbility.isUnarmored(player)) {
				disqualifyPlayer(player);
			}
		}
	}

	private void disqualifyPlayer(EntityPlayerMP player) {
		if (entryTracker.values().removeIf(
				m -> {
					Entry remove = m.remove(player);
					if (remove != null) {
						remove.disqualified = true;
					}
					return m.isEmpty();
				}
		)) {
			player.sendStatusMessage(new TextComponentTranslation("monk.disqualify"), true);
		}
	}

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		if (event.getEntity().world.isRemote || entryTracker.isEmpty() || !clazz.isInstance(event.getEntity())) return;
		EntityLivingBase entityLiving = event.getEntityLiving();

		WeakHashMap<EntityPlayerMP, Entry> map = entryTracker.get(clazz.cast(entityLiving));
		if (map == null) return;
		for (Map.Entry<EntityPlayerMP, Entry> playerMPEntryEntry : map.entrySet()) {
			EntityPlayerMP player = playerMPEntryEntry.getKey();
			MonkData monkData = MonkManager.get(player);
			if (monkData.getLevel() == (this.levelToGrant - 1)) {
				if (!playerMPEntryEntry.getValue().disqualified && playerMPEntryEntry.getValue().damage > (event.getEntityLiving().getMaxHealth() / 4)) {
					grantLevel(player);
				}
			}
		}

	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDamage(LivingDamageEvent event) {
		if (!event.getEntity().world.isRemote && clazz.isInstance(event.getEntity())) {
			DamageSource source = event.getSource();
			Entity trueSource = source.getTrueSource();
			if (trueSource instanceof EntityPlayerMP) {
				EntityPlayerMP player = (EntityPlayerMP) trueSource;
				MonkData monkData = MonkManager.get(player);
				if (monkData.getLevel() == (this.levelToGrant - 1)) {
					if (source.isProjectile()) {
						disqualifyPlayer(player);
					} else {
						Entry entry = entryTracker
								.computeIfAbsent(clazz.cast(event.getEntity()), t -> new WeakHashMap<>())
								.computeIfAbsent(player, p -> new Entry());
						entry.damage += event.getAmount();
					}
				}
			}
		}

	}


	private class Entry {
		boolean disqualified;
		float damage;
	}
}
