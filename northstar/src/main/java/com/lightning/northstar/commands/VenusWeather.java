package com.lightning.northstar.commands;

import com.lightning.northstar.world.dimension.NorthstarDimensions;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class VenusWeather {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
			dispatcher.register(Commands.literal("venusweather").requires((p_139171_) -> {
	         return p_139171_.hasPermission(2);
	      }).then(Commands.literal("clear").executes((p_139190_) -> {
	         return setClear(p_139190_.getSource(), 6000);
	      }).then(Commands.argument("duration", IntegerArgumentType.integer(0, 1000000)).executes((p_139188_) -> {
	         return setClear(p_139188_.getSource(), IntegerArgumentType.getInteger(p_139188_, "duration") * 20);
	      }))).then(Commands.literal("rain").executes((p_139186_) -> {
	         return setRain(p_139186_.getSource(), 6000);
	      }).then(Commands.argument("duration", IntegerArgumentType.integer(0, 1000000)).executes((p_139181_) -> {
	         return setRain(p_139181_.getSource(), IntegerArgumentType.getInteger(p_139181_, "duration") * 20);
	      }))).then(Commands.literal("thunder").executes((p_139176_) -> {
	         return setThunder(p_139176_.getSource(), 6000);
	      }).then(Commands.argument("duration", IntegerArgumentType.integer(0, 1000000)).executes((p_139169_) -> {
	         return setThunder(p_139169_.getSource(), IntegerArgumentType.getInteger(p_139169_, "duration") * 20);
	      }))));
	   }
	
	
	
	private static int setClear(CommandSourceStack pSource, int pTime) {
		pSource.getLevel().getServer().getLevel(NorthstarDimensions.VENUS_DIM_KEY).setWeatherParameters(0, pTime, false, false);
	    pSource.sendSuccess(() -> Component.translatable("commands.weather.set.clear"), true);
	    return pTime;
	}

	private static int setRain(CommandSourceStack pSource, int pTime) {
		pSource.getLevel().getServer().getLevel(NorthstarDimensions.VENUS_DIM_KEY).setWeatherParameters(0, pTime, true, false);
	    pSource.sendSuccess(() -> Component.translatable("commands.weather.set.rain"), true);
	    return pTime;
	}

	private static int setThunder(CommandSourceStack pSource, int pTime) {
		pSource.getLevel().getServer().getLevel(NorthstarDimensions.VENUS_DIM_KEY).setWeatherParameters(0, pTime, true, true);
	    pSource.sendSuccess(() -> Component.translatable("commands.weather.set.thunder"), true);
	    return pTime;
	}
}
