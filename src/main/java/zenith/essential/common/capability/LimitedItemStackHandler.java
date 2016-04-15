package zenith.essential.common.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import zenith.essential.common.EssentialLogger;

public class LimitedItemStackHandler extends ItemStackHandler {

	int maxStackSize;

    public LimitedItemStackHandler(int size, int maxStackSize)
    {
    	super(size);
        this.maxStackSize = maxStackSize;
    }

	@Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
    {
        if (stack == null || stack.stackSize == 0)
            return null;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks[slot];
        int limit = getStackLimit(slot, stack);

        if (existing != null)
        {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.stackSize;
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.stackSize > limit;

        if (!simulate)
        {
            if (existing == null)
            {
                ItemStack itemHelperStack = reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack;                this.stacks[slot] = reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack;
                this.stacks[slot] = itemHelperStack;
            }
            else
            {
                existing.stackSize += reachedLimit ? limit : stack.stackSize;
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.stackSize - limit) : null;
    }

	@Override
	protected int getStackLimit(int slot, ItemStack stack){
		return Math.min(maxStackSize, stack.getMaxStackSize());
	}
    

}
