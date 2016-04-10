package zenith.essential.common.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class EssentialItems {
	
	public static ItemBase crafting;
	public static ItemFireBow firebow;

	private static List<ItemBase> itemList = new ArrayList<ItemBase>();

	public static void init(){
		crafting = (ItemEssentialCrafting) registerItem(ItemEssentialCrafting.class);
		firebow = (ItemFireBow) registerItem(ItemFireBow.class);
	}
	
	private static Item registerItem(Class<? extends ItemBase> clazz){
		ItemBase instance = null;
		try {
			instance = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		itemList.add(instance);
		return instance;
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels(){
		for(ItemBase item : itemList){
			item.initModel();
		}
	}
}
