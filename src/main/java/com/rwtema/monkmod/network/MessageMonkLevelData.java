package com.rwtema.monkmod.network;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.data.MonkData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

public class MessageMonkLevelData extends MonkNetwork.MessageServerToClient {
	private NBTTagCompound data;

	public MessageMonkLevelData() {

	}

	private MessageMonkLevelData(NBTTagCompound data) {
		this.data = data;
	}

	public MessageMonkLevelData(MonkData monkData) {
		this(monkData.serializeNBT());
	}


	@Override
	protected void runClient(MessageContext ctx, EntityPlayer player) {
		if (data != null) {
			MonkData monkData = MonkManager.get(player);
			monkData.deserializeNBT(data);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			data = (new PacketBuffer(buf)).readCompoundTag();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		(new PacketBuffer(buf)).writeCompoundTag(data);
	}
}
