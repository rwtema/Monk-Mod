package com.rwtema.monkmod.abilities;


import com.rwtema.monkmod.MonkManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MonkAbilityStepUp extends MonkAbility {

	private final EnumMap<Side, WeakHashMap<EntityPlayer, Boolean>> map;

	public MonkAbilityStepUp() {
		super("step_up");
		Collector<Side, ?, Map<Side, WeakHashMap<EntityPlayer, Boolean>>> sideMapCollector = Collectors.toMap(s -> s, t -> new WeakHashMap<>());
		Map<Side, WeakHashMap<EntityPlayer, Boolean>> map = Stream.of(Side.values()).collect(sideMapCollector);
		this.map = new EnumMap<>(map);
	}


	@SubscribeEvent
	public void run(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			Side side = event.getEntity().world.isRemote ? Side.CLIENT : Side.SERVER;
			WeakHashMap<EntityPlayer, Boolean> submap = map.get(side);

			boolean shouldStepup = isUnarmored(player) && MonkManager.getAbilityLevel(player, this);
			if (submap.containsKey(player)) {
				if(shouldStepup){
					player.stepHeight = !player.isSneaking() ? 1.5F : 0.6000001F;
				}else{
					player.stepHeight = side == Side.SERVER ? 1.0F : 0.6F;
					submap.remove(player);
				}
			} else if (shouldStepup) {
				player.stepHeight = 1.25F;
				submap.put(player, true);
			}
		}
	}
}
