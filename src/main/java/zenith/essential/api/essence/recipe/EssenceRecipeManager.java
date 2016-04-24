package zenith.essential.api.essence.recipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import zenith.essential.api.essence.EnumEssenceType;
import zenith.essential.common.EssentialLogger;

public class EssenceRecipeManager {

	private Map<EnumEssenceType, List<IEssenceRecipe>> recipeMap = Maps.newEnumMap(EnumEssenceType.class);
	private List<IEssenceRecipe> allRecipes = Lists.<IEssenceRecipe>newArrayList();
	private static final EssenceRecipeManager instance = new EssenceRecipeManager();
	
	private EssenceRecipeManager(){
	}

	public static void preInit(){
		for(EnumEssenceType essenceType : EnumEssenceType.values()){
			EssentialLogger.quickInfo("Initializing recipe list for " + essenceType.getName());
			instance.recipeMap.put(essenceType, Lists.<IEssenceRecipe>newArrayList());
			EssentialLogger.quickInfo("Initialized? " + (instance.recipeMap.get(essenceType) != null));
		}
	}
	
	public static void registerRecipes(){
		addRecipe(new EssenceRecipe(
				new ItemStack(Blocks.sapling), "stickWood", 0, 1f, EnumEssenceType.WOOD));
	}
	
	public static EssenceRecipeManager getInstance(){
		return instance;
	}

	public static void addRecipe(IEssenceRecipe recipe){
		EnumEssenceType essenceType = recipe.getEssenceType();
		if(essenceType != null){
			EssentialLogger.quickDebug("essenceType found ? " + (essenceType != null));
			List<IEssenceRecipe> theList = instance.recipeMap.get(essenceType);
			theList.add(recipe);
			instance.allRecipes.add(recipe);
				
		}

	}
	
	public static void removeRecipe(IEssenceRecipe recipe){
		EnumEssenceType essenceType = recipe.getEssenceType();
		instance.recipeMap.get(essenceType).remove(recipe);
		instance.allRecipes.remove(recipe);
	}
	
	public static IEssenceRecipe findMatch(ItemStack input, EnumEssenceType essenceType){
		for(IEssenceRecipe recipe : instance.recipeMap.get(essenceType)){
			if(recipe.matches(input, essenceType)){
				return recipe;
			}
		}
		return null;
	}
	
	public List<IEssenceRecipe> getRecipeList(EnumEssenceType essenceType){
		return recipeMap.get(essenceType);
	}

	public List<IEssenceRecipe> getRecipeList(){
		return allRecipes;
	}
	
}
