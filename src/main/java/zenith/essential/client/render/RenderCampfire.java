package zenith.essential.client.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zenith.essential.common.block.BlockBase;
import zenith.essential.common.block.EssentialBlocks;
import zenith.essential.common.tile.TileEntityCampfire;

@SideOnly(Side.CLIENT)
public class RenderCampfire extends EssentialTileEntityRenderer<TileEntityCampfire> {

	public RenderCampfire() {
		super(EssentialBlocks.campfire);
	}

	@Override
    protected void renderCustom(TileEntityCampfire tileEntity) {
    }

}
