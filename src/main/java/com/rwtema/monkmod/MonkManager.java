package com.rwtema.monkmod;

import com.rwtema.monkmod.abilities.MonkAbility;
import com.rwtema.monkmod.abilities.MonkAbilityAttribute;
import com.rwtema.monkmod.data.MonkData;
import com.rwtema.monkmod.levels.MonkLevelManager;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Set;
import java.util.UUID;

public class MonkManager {
	@SubscribeEvent
	public static void registerCap(AttachCapabilitiesEvent<EntityPlayer> playerAttachCapabilitiesEvent) {
		playerAttachCapabilitiesEvent.addCapability(MonkData.LOCATION, new MonkData());
	}

	@SubscribeEvent
	public static void tick(TickEvent.PlayerTickEvent event){
		if(event.player.world.isRemote) return;
		MonkData monkData = get(event.player);

		Set<MonkLevelManager.Entry> abilities = MonkLevelManager.getAbilities(monkData.getLevel());
		for (MonkLevelManager.Entry entry : abilities) {
			entry.ability.tickServer(event.player, entry.level);
		}

		AbstractAttributeMap attributeMap = event.player.getAttributeMap();
		for (IAttributeInstance attributeInstance : attributeMap.getAllAttributes()) {
			for (UUID uuid : MonkAbilityAttribute.uuids) {
				attributeInstance.removeModifier(uuid);
			}
		}
		for (MonkLevelManager.Entry entry : abilities) {
			if(entry.ability instanceof MonkAbilityAttribute){
				MonkAbilityAttribute abilityAttribute = (MonkAbilityAttribute) entry.ability;
				IAttributeInstance attributeInstance = attributeMap.getAttributeInstance(((MonkAbilityAttribute) abilityAttribute).attribute);
				attributeInstance.applyModifier(new AttributeModifier(
						abilityAttribute.uuid,
						abilityAttribute.name,
						abilityAttribute.getAmount(entry.level),
						abilityAttribute.operation


				));
			}
		}
	}

	public static MonkData get(EntityPlayer player) {
		return player.getCapability(MonkData.MONKLEVELDATA, null);
	}

	public int getLevel(EntityPlayer player, MonkAbility ability) {
		return 0;
	}

	public boolean hasAbility(EntityPlayer player, MonkAbility ability) {
		return getLevel(player, ability) > 0;
	}
}
