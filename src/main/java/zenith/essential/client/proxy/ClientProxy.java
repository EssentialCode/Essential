package zenith.essential.client.proxy;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zenith.essential.common.item.EssentialItems;
import zenith.essential.common.proxy.CommonProxy;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event){
		super.preInit(event);
		EssentialItems.initModels();
	}

}
