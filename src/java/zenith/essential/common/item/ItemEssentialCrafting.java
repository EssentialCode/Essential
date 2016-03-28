package zenith.essential.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zenith.essential.common.EssentialCreativeTab;

public class ItemEssentialCrafting extends ItemBase {
	public static String name = "itemCrafting";

	public static int GRASS_FIBER = 0;

	public static final String[] TYPES = {
			"fiberGrass"
	};

	public ItemEssentialCrafting(){
		super(name);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
	@Override
    public String getUnlocalizedName(ItemStack stack)
    {
        int i = stack.getMetadata();
        return super.getUnlocalizedName() + "." + TYPES[i];
    }


    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems)
    {
        for (int i = 0; i < TYPES.length; ++i)
        {
            subItems.add(new ItemStack(item, 1, i));
        }
    }

}
