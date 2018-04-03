package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.server.FMLServerHandler;

public class MonkRequirementDescentAscent extends MonkRequirementDeath {
	public MonkRequirementDescentAscent(int level) {
		super(level);
	}

	@Override
	protected void onDeathAvoid(EntityPlayerMP player, MonkData monkData) {
		player.setHealth(10);
		int progress = monkData.getProgress();
		player.sendMessage(new TextComponentTranslation("monk.final." + progress));
		if (monkData.increase(1, 15)) {
			super.onDeathAvoid(player, monkData);

			MinecraftServer minecraftServer = player.world.getMinecraftServer();
			assert minecraftServer != null;
			minecraftServer.getPlayerList().recreatePlayerEntity(player, player.world.provider.getDimension(), true);
		}

	}

	@Override
	protected boolean isValidSourceOfDeath(LivingDeathEvent event) {
		return event.getSource() == DamageSource.OUT_OF_WORLD && event.getEntity().posY < -60;
	}
}
