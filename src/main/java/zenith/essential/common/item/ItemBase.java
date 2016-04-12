package zenith.essential.common.item;

import java.util.List;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zenith.essential.common.EssentialCreativeTab;
import zenith.essential.common.lib.GeneralConstants;

public class ItemBase extends Item {
	
	public ItemBase(String name){
		setupAndRegister(name);
		setCreativeTab(EssentialCreativeTab.INSTANCE);
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
//        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
	
	private Item setupAndRegister(String name){
		super.setUnlocalizedName(name);
		setRegistryName(name);
		GameRegistry.registerItem(this, name);
		return this;
	}

	@Override
	public String getUnlocalizedName(){
		return super.getUnlocalizedName();
	}

	public List<IRecipe> getRecipes(){
		return null;
	}
}
