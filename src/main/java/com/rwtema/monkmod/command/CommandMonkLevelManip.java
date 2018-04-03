package com.rwtema.monkmod.command;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.MonkMod;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandMonkLevelManip extends  CommandTreeBase  {

	@Override
	public String getName() {
		return "monk";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "monk";
	}

	 {

		addSubcommand(new CommandBase() {
			@Override
			public String getName() {
				return "setlevel";
			}

			@Override
			public String getUsage(ICommandSender sender) {
				return "setlevel";
			}

			@Override
			public int getRequiredPermissionLevel() {
				return 2;
			}

			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

				EntityPlayer entityplayer = args.length > 1 ? getPlayer(server, sender, args[1]) : getCommandSenderAsPlayer(sender);

				MonkData monkData = MonkManager.get(entityplayer);

				int amount = parseInt(args[0]);
				monkData.setLevel(amount);
				MonkMod.TRIGGER.trigger((EntityPlayerMP) entityplayer, amount);
			}

			public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
			{
				return args.length == 2 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
			}
		});
	}

}
