package zenith.essential.api.essence;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import zenith.essential.common.lib.GeneralConstants;

public enum EnumEssenceType implements IStringSerializable{

	WOOD("wood", MapColor.greenColor, "emblem_wood.png", "textures/blocks/wool_colored_green.png"),
	FIRE("fire", MapColor.redColor, "emblem_fire.png", "textures/blocks/wool_colored_red.png"),
	EARTH("earth", MapColor.dirtColor, "emblem_earth.png", "textures/blocks/wool_colored_brown.png"),
	METAL("metal", MapColor.silverColor, "emblem_metal.png", "textures/blocks/wool_colored_silver.png"),
	WATER("water", MapColor.waterColor, "emblem_water.png", "textures/blocks/wool_colored_blue.png");
	
	private final MapColor color;
	private final String name;
	private final ResourceLocation emblem;
	private final ResourceLocation runner;
	private final static EnumEssenceType[] LOOKUP = new EnumEssenceType[values().length];
	
	EnumEssenceType(String name, MapColor color, String emblemTextureName, String runnerTexture){
		this.name = name;
		this.color = color;
		this.runner = new ResourceLocation(runnerTexture);
		this.emblem = new ResourceLocation(GeneralConstants.MOD_ID, "textures/items/" + emblemTextureName);
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
	
	public ResourceLocation getRunnerTexture(){
		return this.runner;
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
