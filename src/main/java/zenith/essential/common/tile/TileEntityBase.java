package zenith.essential.common.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import zenith.essential.common.EssentialLogger;
import zenith.essential.common.block.BlockBase;
import zenith.essential.common.capability.IInventoryHandler;

public class TileEntityBase extends TileEntity {

	protected List<IInventoryHandler> handlers = new ArrayList<IInventoryHandler>();

	public BlockBase getBlock(){
		return (BlockBase) getBlockType();
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

    @SuppressWarnings("rawtypes")
	@Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

	public static double calculateHitX(Vec3 hitVec, EnumFacing k, EnumFacing front) {
        return calculateHitX(hitVec.xCoord, hitVec.yCoord, hitVec.zCoord, k, front);
    }

    public static double calculateHitX(double sx, double sy, double sz, EnumFacing k, EnumFacing front) {
        switch (k) {
            case DOWN:
            case UP:
                switch (front) {
                    case DOWN:
                        return sx;
                    case UP:
                        return sx;
                    case NORTH:
                        return 1-sx;
                    case SOUTH:
                        return sx;
                    case WEST:
                        return sz;
                    case EAST:
                        return 1-sz;
                    default:
                        break;
                }
                return sx;
            case NORTH: return 1-sx;
            case SOUTH: return sx;
            case WEST: return sz;
            case EAST: return 1-sz;
            default: return 0.0f;
        }
    }

    public static double calculateHitY(Vec3 hitVec, EnumFacing k, EnumFacing front) {
        return calculateHitY(hitVec.xCoord, hitVec.yCoord, hitVec.zCoord, k, front);
    }

    public static double calculateHitY(double sx, double sy, double sz, EnumFacing k, EnumFacing front) {
        switch (k) {
            case DOWN:
            case UP:
                switch (front) {
                    case DOWN:
                        return sz;
                    case UP:
                        return sz;
                    case NORTH:
                        return 1-sz;
                    case SOUTH:
                        return sz;
                    case WEST:
                        return 1-sx;
                    case EAST:
                        return sx;
                    default:
                        break;
                }
                return sz;
            case NORTH: return sy;
            case SOUTH: return sy;
            case WEST: return sy;
            case EAST: return sy;
            default: return 0.0f;
        }
    }

	public List<IInventoryHandler> getInventoryHandlers() {
		return handlers;
	}
	
	protected void addInterfaceHandle(IInventoryHandler handler){
		handlers.add(handler);
	}
	
	public void markForUpdate(){
		this.markDirty();
		worldObj.markBlockForUpdate(pos);
	}
}
