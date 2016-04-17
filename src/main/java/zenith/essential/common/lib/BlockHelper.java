package zenith.essential.common.lib;

import static net.minecraft.util.EnumFacing.*;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import zenith.essential.common.block.BlockBase;

public class BlockHelper {

	public static EnumFacing getOrientation(IBlockState state) {
        return ((BlockBase)state.getBlock()).getFrontDirection(state);
    }

	// Given the metavalue of a block, reorient the world direction to the internal block direction
    // so that the front side will be SOUTH.
    public static EnumFacing worldToBlockSpace(EnumFacing side, IBlockState state) {
        return worldToBlockSpace(side, getOrientation(state));
    }

    public static EnumFacing worldToBlockSpace(EnumFacing worldSide, EnumFacing blockDirection) {
        switch (blockDirection) {
            case DOWN:
                switch (worldSide) {
                    case DOWN: return SOUTH;
                    case UP: return NORTH;
                    case NORTH: return UP;
                    case SOUTH: return DOWN;
                    case WEST: return EAST;
                    case EAST: return WEST;
                    default: return worldSide;
                }
            case UP:
                switch (worldSide) {
                    case DOWN: return NORTH;
                    case UP: return SOUTH;
                    case NORTH: return UP;
                    case SOUTH: return DOWN;
                    case WEST: return WEST;
                    case EAST: return EAST;
                    default: return worldSide;
                }
            case NORTH:
                if (worldSide == DOWN || worldSide == UP) {
                    return worldSide;
                }
                return worldSide.getOpposite();
            case SOUTH:
                return worldSide;
            case WEST:
                if (worldSide == DOWN || worldSide == UP) {
                    return worldSide;
                } else if (worldSide == WEST) {
                    return SOUTH;
                } else if (worldSide == NORTH) {
                    return WEST;
                } else if (worldSide == EAST) {
                    return NORTH;
                } else {
                    return EAST;
                }
            case EAST:
                if (worldSide == DOWN || worldSide == UP) {
                    return worldSide;
                } else if (worldSide == WEST) {
                    return NORTH;
                } else if (worldSide == NORTH) {
                    return EAST;
                } else if (worldSide == EAST) {
                    return SOUTH;
                } else {
                    return WEST;
                }
            default:
                return worldSide;
        }
    }
}
