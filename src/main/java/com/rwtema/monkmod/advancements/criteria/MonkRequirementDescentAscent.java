package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class MonkRequirementDescentAscent extends MonkRequirementDeath {
	public MonkRequirementDescentAscent() {
		super("void_fall");
	}

	@Override
	protected void onDeathAvoid(EntityPlayerMP player, MonkData monkData) {
		player.setHealth(20);
		int progress = monkData.getProgress();
		if (monkData.increase(1, 15)) {
			super.onDeathAvoid(player, monkData);

			MinecraftServer minecraftServer = player.world.getMinecraftServer();
			assert minecraftServer != null;
			player.connection.sendPacket(new SPacketChangeGameState(4, 0));
			player.connection.player = minecraftServer.getPlayerList().recreatePlayerEntity(player, player.world.provider.getDimension(), false);
		} else {
			player.sendMessage(new TextComponentTranslation("monk.final." + progress));
		}

	}

	@Override
	protected boolean isValidSourceOfDeath(LivingDeathEvent event) {
		return event.getSource() == DamageSource.OUT_OF_WORLD && event.getEntity().posY < -60;
	}
}
