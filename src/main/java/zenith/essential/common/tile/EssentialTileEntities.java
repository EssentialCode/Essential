package zenith.essential.common.tile;

import net.minecraftforge.fml.common.registry.GameRegistry;
import zenith.essential.common.lib.GeneralConstants;

public class EssentialTileEntities {
	public static void init() {
		GameRegistry.registerTileEntity(TileEntityCampfire.class, GeneralConstants.MOD_ID + "_campfire");
	}

}
