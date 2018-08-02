package com.rwtema.monkmod.network;


import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

public class MonkNetwork {
	public static SimpleNetworkWrapper net;

	public static void init() {
		net = new SimpleNetworkWrapper("MonkNetwork");
		IMessageHandler<MessageBase, IMessage> genericHandler = (message, ctx) -> {
			message.onReceived(ctx);
			return null;
		};

		registerClientToServerPacket(genericHandler, MessageBlink.class, 0);
		registerServerToClientPacket(genericHandler, MessageMonkLevelData.class, 1);
		registerServerToClientPacket(genericHandler, MessageProgress.class, 2);
	}

	private static <T extends MessageServerToClient> void registerServerToClientPacket(@Nonnull IMessageHandler<MessageBase, IMessage> genericHandler, Class<T> messageType, int discriminator) {
		net.registerMessage(genericHandler, messageType, discriminator, Side.CLIENT);
	}

	private static <T extends MessageClientToServer> void registerClientToServerPacket(@Nonnull IMessageHandler<MessageBase, IMessage> genericHandler, Class<T> messageType, int discriminator) {
		net.registerMessage(genericHandler, messageType, discriminator, Side.SERVER);
	}

	public abstract static class MessageBase implements IMessage {

		public void onReceived(MessageContext ctx) {

		}
	}

	public abstract static class MessageClientToServer extends MessageBase {

		@Override
		public void onReceived(@Nonnull MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> runServer(ctx, player));
		}

		protected abstract void runServer(MessageContext ctx, EntityPlayerMP player);

		public void writeNBT(NBTTagCompound tag, @Nonnull ByteBuf buf) {
			new PacketBuffer(buf).writeCompoundTag(tag);
		}

		@Nullable
		public NBTTagCompound readNBT(@Nonnull ByteBuf bf) {
			try {
				return new PacketBuffer(bf).readCompoundTag();
			} catch (IOException e) {
				throw new DecoderException(e);
			}
		}
	}

	public abstract static class MessageServerToClient extends MessageBase {
		@Override
		@SideOnly(Side.CLIENT)
		public void onReceived(MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> runClient(ctx, Minecraft.getMinecraft().player));
		}

		@SideOnly(Side.CLIENT)
		protected abstract void runClient(MessageContext ctx, EntityPlayer player);
	}
}
