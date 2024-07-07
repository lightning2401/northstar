package com.lightning.northstar.block.tech;

import com.lightning.northstar.Northstar;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;

public class SpriteShifts {
	
	public static final CTSpriteShiftEntry
			JET_ENGINE = getCT(AllCTTypes.RECTANGLE, "jet_engine"),
			JET_ENGINE_TOP = getCT(AllCTTypes.RECTANGLE, "jet_engine_top");
	
	
	private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
		return CTSpriteShifter.getCT(type, Northstar.asResource("block/" + blockTextureName),
				Northstar.asResource("block/" + connectedTextureName + "_connected"));
	}

	private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
		return getCT(type, blockTextureName, blockTextureName);
	}

}
