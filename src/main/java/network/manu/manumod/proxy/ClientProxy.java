package network.manu.manumod.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;
import network.manu.manumod.eventhandlers.ClientBusEventHandler;

public class ClientProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
    }

    @Override
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new ClientBusEventHandler());
        MinecraftForge.EVENT_BUS.register(new ClientBusEventHandler());
    }

    @Override

    public void postInit(FMLPostInitializationEvent event) {
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
    }
}
