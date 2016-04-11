package zenith.essential.common.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.core.Logger;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import zenith.essential.common.EssentialLogger;

public class RecipeHandler {
	
	private static EssentialLogger log = EssentialLogger.getLogger();

	public static void postInit() {

	}

	private static void updateToOreDictRecipes(Object targetIn, String oreName) {
		boolean isItem = targetIn instanceof Item;
		ItemStack target = isItem ? new ItemStack((Item) targetIn) : new ItemStack((Block) targetIn);
		int[] oreIds = OreDictionary.getOreIDs(target);
		if(oreIds != null && oreIds.length > 0){
			log.info("======================");
			log.info("In ore dictionary as:");
			for(int i = 0; i < oreIds.length; i ++){
				log.info(OreDictionary.getOreName(oreIds[i]));
			}
			log.info("======================");
		}
		log.info("Recipe Handler removing recipes containing " + target.getDisplayName());

		List<IRecipe> recipies = CraftingManager.getInstance().getRecipeList();
		Iterator<IRecipe> remover = recipies.iterator();
		List<IRecipe> matches = new ArrayList<IRecipe>();

		while (remover.hasNext()) {
			IRecipe recipe = remover.next();

			if(recipe instanceof ShapedRecipes){
				ShapedRecipes shaped = (ShapedRecipes) recipe;
				ItemStack items[] = shaped.recipeItems;
				if(inputsContainsTarget(new ArrayList<Object>(Arrays.asList(items)), target)){
					log.info("Found target in shaped recipe for : " + shaped.getRecipeOutput().getDisplayName());
					matches.add(recipe);
					remover.remove();
				}
			} else if (recipe instanceof ShapelessRecipes){
				ShapelessRecipes shapeless = (ShapelessRecipes) recipe;
				Object itemObjects[] = shapeless.recipeItems.toArray();
				ItemStack items[] = Arrays.asList(itemObjects).toArray(new ItemStack[itemObjects.length]);
				if(inputsContainsTarget(new ArrayList<Object>(Arrays.asList(itemObjects)), target)){
					matches.add(recipe);
					remover.remove();
				}
			} else if (recipe instanceof ShapedOreRecipe){
				ShapedOreRecipe shapedOre = (ShapedOreRecipe) recipe;
				Object itemObjects[] = shapedOre.getInput(); 
				if(inputsContainsTarget(new ArrayList<Object>(Arrays.asList(itemObjects)), target)){
					log.info("Found ");
				}
//				logRecipe(shapedOre, items);
			} else if (recipe instanceof ShapelessOreRecipe){
				ShapelessOreRecipe shapelessOre = (ShapelessOreRecipe) recipe;
				if(inputsContainsTarget(shapelessOre.getInput(), target)){
					log.info("Found target in shaped ore recipe for : " + 
						shapelessOre.getRecipeOutput().getDisplayName());
					matches.add(recipe);
					remover.remove();
				}
//				log.info("A different kind of recipe:" + recipe.getClass().toString());
			} else {
//				log.info("It is a different kind of recipe: " + recipe.getClass().toString());
			}

//			if ((itemstack != null && isItem && itemstack.getItem() == (Item) target) || (itemstack != null
//					&& isItem == false && itemstack.getItem() == Item.getItemFromBlock((Block) target))) {
//				remover.remove();
//			}

		}
		log.info("Found " + matches.size() + " recipes containing " + target.getDisplayName());
		replaceRecipes(matches);
	}
	
	public static void updateToOreDictRecipes(Item targetIn, String oreName) {
		updateToOreDictRecipes(targetIn, oreName);
	}
	
	public static void updateToOreDictRecipes(Block targetIn, String oreName) {
		updateToOreDictRecipes(targetIn, oreName);
	}
	
	private static void replaceRecipes(List<IRecipe> recipes){
		for(IRecipe recipe : recipes){
			log.info("Replacing recipe for " + recipe.getRecipeOutput().getDisplayName());
		}
	}
	
	private static boolean inputsContainsTarget(Iterable<Object> inputs, ItemStack target){
		for (Object o : inputs){
			if(o instanceof ItemStack ){
				if(((ItemStack) o).getUnlocalizedName().equalsIgnoreCase(target.getUnlocalizedName())){
					return true;
				}
			} else if (o instanceof String){
				List<ItemStack> theStacks = OreDictionary.getOres((String) o);
				if(inputsContainsTarget(new ArrayList<Object>(theStacks), target)){
					return true;
				}
			} else if (o instanceof Iterable){
/*				log.info("Iterating");
				for(Object obj : (Iterable) o){
					if(obj instanceof ItemStack){
						int[] ids = OreDictionary.getOreIDs((ItemStack) obj);
						if(ids != null && ids.length > 1){
							for(int i = 0; i < ids.length; i ++){
								log.info("ore dict name: " + OreDictionary.getOreName(ids[i]));
							}
						}
					}
				}
				log.info("-----------");
*/
				if(inputsContainsTarget((Iterable) o, target)){
					return true;
				}
			} else {
				if(o != null)
					log.info("o is not any of the above: " + o.getClass().getName());
			}
		}
		return false;
	}

	private static boolean recipeInputContains(ItemStack[] items, Object target, boolean isItem){
//		for(ItemStack itemStack : items){
//			if(itemStack != null && itemStack.getIsItemStackEqual(targetStack)){
//				return true;
//			}
//		}
		return false;
	}
	
	private static void logRecipe(IRecipe recipe, ItemStack[] recipeItems){
		log.info(recipe.getRecipeOutput().getDisplayName() + ":");
		log.info("---------------------");
		for(ItemStack itemStack : recipeItems){
			if(itemStack != null)
			log.info(itemStack.getDisplayName());
		}
	}
}
