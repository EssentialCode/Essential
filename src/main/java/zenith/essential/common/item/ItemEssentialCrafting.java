package zenith.essential.common.item;

import java.util.List;

import org.apache.logging.log4j.core.Logger;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import zenith.essential.common.EssentialLogger;

public class ItemEssentialCrafting extends ItemBase {
	public static String name = "itemCrafting";
	
	private static EssentialLogger log = EssentialLogger.getLogger();

	public static int GRASS_FIBER = 0;

	public static enum CraftingItemType implements IStringSerializable {
        GRASS_FIBER("fiberGrass");

        private final String name;

        CraftingItemType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

	public static final PropertyEnum<CraftingItemType> CRAFTING_ITEM_TYPE = PropertyEnum.create("craftingItemType", CraftingItemType.class);

	public static final String[] TYPES = {
			"fiberGrass"
	};

	public ItemEssentialCrafting(){
		super(name);
		setHasSubtypes(true);
		setMaxDamage(0);
		OreDictionary.registerOre("string", new ItemStack(this, 1, 0));
	}
	
	public static ItemBase instantiate(){
		return new ItemEssentialCrafting();
	}

	@Override
	@SideOnly(Side.CLIENT)
    public void initModel() {
		log.info("Registry name: " + getRegistryName());
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName()+"FiberGrass"));
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
