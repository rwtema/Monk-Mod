package com.rwtema.monkmod.network;

import com.rwtema.monkmod.render.HUDProgress;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nonnull;

public class MessageProgress extends MonkNetwork.MessageServerToClient {
	private int progress;
	private int max;

	public MessageProgress() {
	}

	public MessageProgress(int progress, int max) {

		this.progress = progress;
		this.max = max;
	}

	@Override
	protected void runClient(MessageContext ctx, EntityPlayer player) {
		HUDProgress.INSTANCE.handle(progress, max);
	}

	@Override
	public void fromBytes(@Nonnull ByteBuf buf) {
		progress = buf.readInt();
		max = buf.readInt();
	}

	@Override
	public void toBytes(@Nonnull ByteBuf buf) {
		buf.writeInt(progress);
		buf.writeInt(max);
	}
}
