package com.lightning.northstar.contraptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionType;

public class NorthstarContraptionTypes {
	public static final Map<String, NorthstarContraptionTypes> ENTRIES = new HashMap<>();
	public static final ContraptionType
		ROCKET = ContraptionType.register("rocket", RocketContraption::new);
	
	Supplier<? extends Contraption> factory;
	String id;

	public static Contraption fromType(String type) {
		for (Entry<String, NorthstarContraptionTypes> allContraptionTypes : ENTRIES.entrySet())
			if (type.equals(allContraptionTypes.getKey()))
				return allContraptionTypes.getValue().factory.get();
		return null;
	}

}
