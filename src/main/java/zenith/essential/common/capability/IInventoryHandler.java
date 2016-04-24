package zenith.essential.common.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zenith.essential.common.tile.TileEntityBase;

public interface IInventoryHandler {

	int getSlot();
	float getMinX();
    float getMaxX();
    float getMinY();
    float getMaxY();
    EnumFacing getSide();
    float getScale();
    @SideOnly(Side.CLIENT)
    int getRotationOffset();

    Vec3 getRenderOffset();

    ItemStack getCurrentStack();

    boolean acceptAsInput(ItemStack stack);

    // Insert a stack and return the number of items that could not be inserted
    int insertInput(TileEntityBase te, ItemStack stack);

    // True if this handle is meant for output.
    boolean isOutput();

    // True if this is a crafting handle
    boolean isCrafting();

    // Extract output, if amount is -1 all will be extracted. Otherwise the specific amount.
    ItemStack extractOutput(TileEntityBase te, EntityPlayer player, int amount);
	boolean showText();
}
