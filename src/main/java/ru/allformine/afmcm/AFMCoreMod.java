package ru.allformine.afmcm;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import ru.allformine.afmcm.gui.NotifyGui;
import ru.allformine.afmcm.proxy.CommonProxy;
import ru.allformine.afmcm.proxy.NotifierProxy;
import ru.allformine.afmcm.rpc.rpci;

import java.io.File;

@Mod(modid = "afmcm")
public class AFMCoreMod {
    private static FMLEventChannel channel;
    @SidedProxy(clientSide = "ru.allformine.afmcm.proxy.ClientProxy", serverSide = "ru.allformine.afmcm.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NotifierProxy handler = new NotifierProxy();
        (channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("FactionsShow")).register(handler);
        MinecraftForge.EVENT_BUS.register(handler);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderGui(RenderGameOverlayEvent.Pre event) {
        if ((event.type == RenderGameOverlayEvent.ElementType.TEXT) && References.notifyText.length() > 0 && References.notifyDrawing) {
            new NotifyGui(References.notifyText, Minecraft.getMinecraft(), event);
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new AFMCoreMod());
        FMLCommonHandler.instance().bus().register(new AFMCoreMod());

        Configuration config = new Configuration(new File("config", "AFMCoreMod.cfg"));
        config.load();

        References.rpcAppId = config.getString("rpcAppId", "discord", References.rpcAppId, "Secret stuff");
        References.serverName = config.getString("serverName", "discord", References.serverName, "Secret stuff");
        References.bigImageKey = config.getString("bigImageKey", "discord", References.bigImageKey, "Secret stuff");

        config.save();
    }

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event) {
        References.nickname = Minecraft.getMinecraft().getSession().getUsername();

        System.out.println("[AFMCM] Statring DiscordRPC..");
        rpci.initDiscord(System.currentTimeMillis() / 1000L);
        rpci.updateState("В меню", System.currentTimeMillis() / 1000L);
    }


    @SubscribeEvent
    public void onLoggedIn(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        System.out.println("Changing Discord RPC status (ClientConnectedToServerEvent)");

        rpci.updateState("На сервере", System.currentTimeMillis() / 1000L);
    }

    @SubscribeEvent
    public void onLoggedOut(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        System.out.println("Changing Discord RPC status (ClientDisconnectionFromServerEvent)");

        rpci.updateState("В меню", System.currentTimeMillis() / 1000L);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (References.notifyDrawing) {
            References.notifyTicks++;

            if (References.notifyTicks > 200) {
                References.notifyDrawing = false;
                References.notifyTicks = 0;
            }
        }
    }
}
