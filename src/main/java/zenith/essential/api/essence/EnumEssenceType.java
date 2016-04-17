package zenith.essential.api.essence;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import zenith.essential.common.lib.GeneralConstants;

public enum EnumEssenceType implements IStringSerializable{

	WOOD("wood", MapColor.greenColor, "emblem_wood.png"),
	FIRE("fire", MapColor.redColor, "emblem_fire.png"),
	EARTH("earth", MapColor.dirtColor, "emblem_earth.png"),
	METAL("metal", MapColor.silverColor, "emblem_metal.png"),
	WATER("water", MapColor.waterColor, "emblem_water.png");
	
	private final MapColor color;
	private final String name;
	private final ResourceLocation emblem;
	private final static EnumEssenceType[] LOOKUP = new EnumEssenceType[values().length];
	
	EnumEssenceType(String name, MapColor color, String emblemTextureName){
		this.name = name;
		this.color = color;
		this.emblem = new ResourceLocation(GeneralConstants.MOD_ID, "textures/blocks/" + emblemTextureName);
	}

	public MapColor getColor(){
		return this.color;
	}
	
	public int getColorValue(){
		return this.color.colorValue;
	}
	
	public int getMetadata(){
		return this.ordinal();
	}
	
	public ResourceLocation getEmblem(){
		return this.emblem;
	}

	@Override
	public String getName() {
		return name;
	}

	static {
		for(EnumEssenceType ess : values()){
			LOOKUP[ess.ordinal()] = ess;
		}
	}

	public static EnumEssenceType byMetadata(int meta) {
		return LOOKUP[meta];
	}

}
