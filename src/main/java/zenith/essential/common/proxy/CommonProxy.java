package zenith.essential.common.proxy;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zenith.essential.api.essence.recipe.EssenceRecipeManager;
import zenith.essential.common.RecipeManager;
import zenith.essential.common.block.EssentialBlocks;
import zenith.essential.common.event.ForgeEventHandlers;
import zenith.essential.common.item.EssentialItems;
import zenith.essential.common.tile.EssentialTileEntities;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event){
		EssenceRecipeManager.preInit();

		EssentialItems.init();
		EssentialBlocks.init();
	}

	public void init(FMLInitializationEvent event){
		
		RecipeManager.registerCraftingRecipes();
		EssentialTileEntities.init();
		EssenceRecipeManager.registerRecipes();
		MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
	}

	public void postInit(FMLPostInitializationEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void essenceFX(World world, double x, double y, double z, float r, float g, float b, float size, int m) {
		essenceFX(world, x, y, z, r, g, b, 0, 0, 0, size, m);
	}

	public void essenceFX(World world, double x, double y, double z, float r, float g, float b, float gravity, float size, int m) {
		essenceFX(world, x, y, z, r, g, b, 0, -gravity, 0, size, m);
	}

	public void essenceFX(World world, double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m) {
		// Proxy override
	}

}
