package com.lightning.northstar.contraptions;

import com.simibubi.create.content.contraptions.ContraptionType;

public class NorthstarContraptionTypes {
	public static final ContraptionType
		ROCKET = ContraptionType.register("northstar:rocket", RocketContraption::new);
    public static void register() {}
}
