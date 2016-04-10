package zenith.essential.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import zenith.essential.common.lib.GeneralConstants;

public class EssentialCreativeTab extends CreativeTabs {

	public static EssentialCreativeTab INSTANCE = new EssentialCreativeTab();
	
	public EssentialCreativeTab(){
		super(GeneralConstants.MOD_ID);
	}

	@Override
	public Item getTabIconItem() {
		return Items.cake;
	}

}
