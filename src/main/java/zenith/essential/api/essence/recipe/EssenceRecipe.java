package zenith.essential.api.essence.recipe;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import zenith.essential.api.essence.EnumEssenceAltarTier;
import zenith.essential.api.essence.EnumEssenceType;
import zenith.essential.common.EssentialLogger;

public class EssenceRecipe implements IEssenceRecipe{

	int tier;
	float costMultiplier;
	EnumEssenceType essenceType;

	ItemStack result;
	List<ItemStack> inputList = Lists.<ItemStack>newArrayList();
	

	public EssenceRecipe(ItemStack result, String input, int tier, float costMultiplier, EnumEssenceType essenceType){
		this(result, OreDictionary.getOres(input), tier, costMultiplier, essenceType);
	}
	public EssenceRecipe(ItemStack result, ItemStack input, int tier, float essenceRequired, EnumEssenceType essenceType){
		this(result, Lists.<ItemStack>newArrayList(input.copy()), tier, essenceRequired, essenceType);
	}
	public EssenceRecipe(ItemStack result, List<ItemStack> input, int tier, float essenceRequired, EnumEssenceType essenceType){
		if(input.isEmpty()){
			EssentialLogger.quickError("No input given for essence recipe resulting in " + result.getDisplayName() + ". Recipe not added.");
			return;
		}
		this.result = result;
		this.tier = tier;
		this.costMultiplier = essenceRequired;
		this.essenceType = essenceType;
		for(ItemStack stack : input){
			if(stack.stackSize > 1){
				stack.stackSize = 1;
			}
		}
		inputList.addAll(input);
	}

	@Override
	public int getTier() {
		return tier;
	}

	@Override
	public float getCostMultiplier() {
		return costMultiplier;
	}
	
	@Override
	public int getCost(){
		return (int) Math.ceil(EnumEssenceAltarTier.getTier(tier).getBaseCost() * costMultiplier);
	}
	
	@Override
	public boolean outputMatches(ItemStack testInput){
		if(testInput != null){
			return ItemHandlerHelper.canItemStacksStack(result, testInput);
		}
		return false;
	}

	@Override
	public boolean matches(ItemStack testInput, EnumEssenceType essenceType){
		if(testInput != null && essenceType == this.essenceType){
			for(ItemStack theInput : inputList){
				if(stacksAreCompatible(theInput, testInput))
					return true;
			}
			
		}
		return false;
	}
	
	private boolean stacksAreCompatible(ItemStack a, ItemStack b){
		return ItemStack.areItemsEqual(a, b) &&
					a.getMetadata() == b.getMetadata() &&
					ItemStack.areItemStackTagsEqual(a, b);
	}

	@Override
	public List<ItemStack> getInput(){
		return inputList;
	}

	@Override
	public ItemStack getResult() {
		return result;
	}

	@Override
	public EnumEssenceType getEssenceType() {
		return essenceType;
	}

}
