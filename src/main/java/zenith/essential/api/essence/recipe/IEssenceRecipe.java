package zenith.essential.api.essence.recipe;

import java.util.List;

import net.minecraft.item.ItemStack;
import zenith.essential.api.essence.EnumEssenceType;

public interface IEssenceRecipe {
	
	int getTier();
	float getCostMultiplier();
	int getCost();

	boolean matches(ItemStack input, EnumEssenceType essenceType);
	boolean outputMatches(ItemStack output);
	List<ItemStack> getInput();
	ItemStack getResult();
	EnumEssenceType getEssenceType();
}
