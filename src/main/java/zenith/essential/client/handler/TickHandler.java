package zenith.essential.client.handler;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class TickHandler {
	private int rotationTicks = 0;
	public static final int MAX_ROTATION_TICKS = 360;

	@SubscribeEvent
	public void clientTick(ClientTickEvent event){
		if(event.phase == Phase.END){
			rotationTicks++;
			rotationTicks++;
			rotationTicks++;
			rotationTicks = rotationTicks % MAX_ROTATION_TICKS;
		}
	}
	
	public int getRotationTicks(){
		return rotationTicks;
	}
	
	
}
