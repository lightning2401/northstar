package com.lightning.northstar.block.tech;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jozufozu.flywheel.core.PartialModel;
import com.lightning.northstar.Northstar;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class NorthstarPartialModels {
	
	public static final PartialModel
	IRON_SPACE_SUIT_HELMET = armor("iron_space_suit_helmet"),
	BROKEN_IRON_SPACE_SUIT_HELMET = armor("broken_iron_space_suit_helmet"),
	MARTIAN_STEEL_SPACE_SUIT_HELMET = armor("martian_steel_space_suit_helmet"),
	CIRCUIT_ENGRAVER_HEAD = block("circuit_engraver/head"),
	CIRCUIT_ENGRAVER_LASER = block("circuit_engraver/laser"),
	HALF_SHAFT = block("half_shaft"),
	OXYGEN_CONCENTATOR_FAN = block("oxygen_concentrator/fan"),
	WARM_SPINNY = block("temperature_regulator/warm_spinny"),
	COLD_SPINNY = block("temperature_regulator/cold_spinny"),
	OXYGEN_FAN = block("oxygen_generator/fan"),
	SMALL_PISTON = block("combustion_engine/small_piston"),
	PISTON1 = block("combustion_engine/pistons/piston1"),
	PISTON2 = block("combustion_engine/pistons/piston2"),
	PISTON3 = block("combustion_engine/pistons/piston3"),
	PISTON4 = block("combustion_engine/pistons/piston4"),
	PISTON5 = block("combustion_engine/pistons/piston5"),
	PISTON6 = block("combustion_engine/pistons/piston6"),
	CPU1 = block("computer_rack/tier1/cpu1"),
	CPU2 = block("computer_rack/tier1/cpu2"),
	CPU3 = block("computer_rack/tier1/cpu3"),
	CPU4 = block("computer_rack/tier1/cpu4"),
	CPU5 = block("computer_rack/tier1/cpu5"),
	CPU6 = block("computer_rack/tier1/cpu6"),
	CONTROL_LEVER = block("rocket_controls/stick"),
	CONTROL_LEVER_BLOCK = block("rocket_controls/stick_2");

	
	public static final Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials, Map<Direction, PartialModel>> PIPE_ATTACHMENTS =
			new EnumMap<>(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.class);

		public static final Map<Direction, PartialModel> METAL_GIRDER_BRACKETS = new EnumMap<>(Direction.class);
		public static final Map<DyeColor, PartialModel> TOOLBOX_LIDS = new EnumMap<>(DyeColor.class);
		public static final Map<ResourceLocation, Couple<PartialModel>> FOLDING_DOORS = new HashMap<>();
		public static final List<PartialModel> CONTRAPTION_CONTROLS_INDICATOR = new ArrayList<>();

		static {
			for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials type : FluidTransportBehaviour.AttachmentTypes.ComponentPartials.values()) {
				Map<Direction, PartialModel> map = new HashMap<>();
				for (Direction d : Iterate.directions) {
					String asId = Lang.asId(type.name());
					map.put(d, block("fluid_pipe/" + asId + "/" + Lang.asId(d.getSerializedName())));
				}
			}
		}


		private static PartialModel block(String path) {
			return new PartialModel(Northstar.asResource("block/" + path));
		}
		
		private static PartialModel armor(String path) {
			return new PartialModel(Northstar.asResource("entities/armor/" + path));
		}

		public static void init() {
			// init static fields
		}
}
