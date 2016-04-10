package zenith.essential.common;

import java.util.List;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import zenith.essential.common.item.EssentialItems;

public class RecipeManager {
	
	public static void registerCraftingRecipes(){
		List<IRecipe> recipeList = CraftingManager.getInstance().getRecipeList();
		EssentialItems.registerRecipes(recipeList);
	}
}
