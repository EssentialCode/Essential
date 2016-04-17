package zenith.essential.common.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraftforge.items.IItemHandlerModifiable;
import zenith.essential.common.EssentialLogger;

public class InputInventoryHandler extends InventoryHandlerBase<InputInventoryHandler> {
	
	public InputInventoryHandler(){
		super();
	}

	/**
	 * @param inventory
	 * @param slot
	 * @param hitMin
	 * @param hitMax
	 * @param renderOffset
	 */
	public InputInventoryHandler(IItemHandlerModifiable inventory, int slot, Vec3 hitMin, Vec3 hitMax, Vec3 renderOffset){
		super(inventory, slot, hitMin, hitMax, renderOffset);
	}
	
	public void printContents(){
		for(int i = 0; i < inventory.getSlots(); i++){
			ItemStack stack = inventory.getStackInSlot(i);
			ItemStack otherStack = getCurrentStack();
			String contents = stack == null || stack.stackSize == 0 ? "Empty " : stack.getDisplayName() + " ";
			contents += otherStack == null || otherStack.stackSize == 0 ? "(empty)" : otherStack.getDisplayName();

			if(stack != null && i == this.getSlot() && ItemStack.areItemStacksEqual(stack, getCurrentStack())) 
				contents += " - not equal to super";

			String msg = String.format("Slot %d: %s", i, contents);
			EssentialLogger.quickInfo(msg);
		}
	}
}
