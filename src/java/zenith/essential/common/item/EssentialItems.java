package zenith.essential.common.item;

public final class EssentialItems {
	
	public static ItemBase crafting;

	public static void init(){
		crafting = new ItemEssentialCrafting();
	}
}
