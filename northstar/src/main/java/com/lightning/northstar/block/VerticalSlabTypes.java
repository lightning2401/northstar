package com.lightning.northstar.block;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;

public enum VerticalSlabTypes implements StringRepresentable {
	   NORTH("north"),
	   WEST("west"),
	   EAST("east"),
	   SOUTH("south"),
	   DOUBLE("double");

	   private final String name;

	   private VerticalSlabTypes(String pName) {
	      this.name = pName;
	   }
	   
	   public static Direction toDir(VerticalSlabTypes type) {
		   switch (type){
		   		case NORTH: return Direction.NORTH;
		   		case SOUTH: return Direction.SOUTH;
		   		case EAST: return Direction.EAST;
		   		case WEST: return Direction.WEST;
		   		case DOUBLE: return Direction.NORTH;
		   		default: return Direction.NORTH;
		   }
	   }
	   public static VerticalSlabTypes fromDir(Direction dir) {
		   if(dir == Direction.NORTH){return NORTH;}
		   if(dir == Direction.SOUTH){return SOUTH;}
		   if(dir == Direction.EAST){return EAST;}
		   if(dir == Direction.WEST){return WEST;}
		   return NORTH;
	   }

	   public String toString() {
	      return this.name;
	   }

	   public String getSerializedName() {
	      return this.name;
	   }
	}