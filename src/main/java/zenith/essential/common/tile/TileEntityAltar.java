package zenith.essential.common.tile;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zenith.essential.common.block.BlockAltarAttuned;

public class TileEntityAltar extends TileEntityBase {
	
	
	
	
	
	@SideOnly(Side.CLIENT)
	public ResourceLocation getEmblem(){
		return worldObj.getBlockState(pos).getValue(BlockAltarAttuned.ESSENCE_TYPE).getEmblem();
	}
}
