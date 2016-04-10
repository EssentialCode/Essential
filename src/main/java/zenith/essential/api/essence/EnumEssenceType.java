package zenith.essential.api.essence;

import net.minecraft.block.material.MapColor;

public enum EnumEssenceType {

	WOOD(MapColor.greenColor),
	FIRE(MapColor.redColor),
	EARTH(MapColor.dirtColor),
	METAL(MapColor.silverColor),
	WATER(MapColor.waterColor);
	
	private final MapColor color;
	
	EnumEssenceType(MapColor color){
		this.color = color;
	}

	public MapColor getColor(){
		return this.color;
	}
	
	public int getColorValue(){
		return this.color.colorValue;
	}

}
