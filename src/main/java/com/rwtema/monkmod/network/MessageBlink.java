package com.rwtema.monkmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageBlink extends MonkNetwork.MessageClientToServer {
	Vec3d target;

	public MessageBlink() {
	}

	public MessageBlink(Vec3d target) {

		this.target = target;
	}

	@Override
	protected void runServer(MessageContext ctx, EntityPlayerMP player) {

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		target = new Vec3d(buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat((float) target.x);
		buf.writeFloat((float) target.y);
		buf.writeFloat((float) target.z);
	}
}
