package zenith.essential.common.item;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zenith.essential.common.EssentialCreativeTab;
import zenith.essential.common.lib.GeneralConstants;

public class ItemBase extends Item {
	
	public ItemBase(String name){
		setUnlocalizedName(name);
		setCreativeTab(EssentialCreativeTab.INSTANCE);
	}
	
	@Override
	public Item setUnlocalizedName(String name){
		super.setUnlocalizedName(name);
		setRegistryName(name);
		GameRegistry.registerItem(this, name);
		return this;
	}

	@Override
	public String getUnlocalizedName(){
		return super.getUnlocalizedName() + "." + GeneralConstants.MOD_ID;
	}

}
