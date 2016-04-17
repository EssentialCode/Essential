package zenith.essential.common.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class EssentialBlocks {

	public static BlockCampfire campfire;
	public static BlockAltar altar;
	public static BlockAltarAttuned altarAttuned;
	public static BlockTable table;
	public static BlockTableShort tableShort;

	private static List<BlockBase> blockList = new ArrayList<BlockBase>();

	public static void init(){
		campfire = (BlockCampfire) registerBlock(BlockCampfire.class);
		altar = (BlockAltar) registerBlock(BlockAltar.class);
		altarAttuned = (BlockAltarAttuned) registerBlock(BlockAltarAttuned.class);
		table = (BlockTable) registerBlock(BlockTable.class);
		tableShort = (BlockTableShort) registerBlock(BlockTableShort.class);

	}

	private static Block registerBlock(Class<? extends BlockBase> clazz){
		BlockBase instance = null;
		try {
			instance = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		blockList.add(instance);
		return instance;
	}

	@SideOnly(Side.CLIENT)
	public static void initModels(){
		for(BlockBase block : blockList){
			block.initModel();
		}
	}
	
	public static void registerRecipes(List<IRecipe> recipes){
		for(BlockBase block : blockList){
			List<IRecipe> newRecipes = block.getRecipes();
			recipes.addAll(newRecipes);
		}
	}
}
