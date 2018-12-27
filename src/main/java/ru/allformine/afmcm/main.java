package ru.allformine.afmcm;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import ru.allformine.afmcm.gui.cfngui;
import ru.allformine.afmcm.proxy.CommonProxy;
import ru.allformine.afmcm.proxy.cfnproxy;
import ru.allformine.afmcm.rpc.rpci;

@Mod(modid = "afmcm")
public class main {
    private static FMLEventChannel channel;
    @SidedProxy(clientSide = "ru.allformine.afmcm.proxy.ClientProxy", serverSide = "ru.allformine.afmcm.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        cfnproxy handler = new cfnproxy();
        (channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("FactionsShow")).register(handler);
        MinecraftForge.EVENT_BUS.register(handler);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderGui(RenderGameOverlayEvent.Pre event) {
        if ((event.type == RenderGameOverlayEvent.ElementType.TEXT) && vars.cfn.length() > 0) {
            new cfngui(vars.cfn, Minecraft.getMinecraft());
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new main());
        FMLCommonHandler.instance().bus().register(new main());

        Configuration config = new Configuration(new File("config", "AFMCoreMod.cfg"));
        config.load();

        vars.rpcAppId = config.getString("rpcAppId", "discord", vars.rpcAppId, "Secret stuff");
        vars.serverName = config.getString("serverName", "discord", vars.serverName, "Secret stuff");
        vars.bigImageKey = config.getString("bigImageKey", "discord", vars.bigImageKey, "Secret stuff");

        config.save();
    }

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event) {
        vars.nickname = Minecraft.getMinecraft().getSession().getUsername();

        System.out.println("[AFMCM] Statring DiscordRPC..");
        rpci.initDiscord(System.currentTimeMillis() / 1000L);
        rpci.updateState("В меню", System.currentTimeMillis() / 1000L);
    }

    @SubscribeEvent
    public void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        System.out.println("Changing Discord RPC status (PlayerLoggedIn)");

        if(MinecraftServer.getServer().isDedicatedServer()) {
            rpci.updateState("На сервере", System.currentTimeMillis() / 1000L);
        } else {
            rpci.updateState("В одиночной игре", System.currentTimeMillis() / 1000L);
        }
    }

    @SubscribeEvent
    public void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        System.out.println("Changing Discord RPC status (PlayerLoggedOut)");

        rpci.updateState("В меню", System.currentTimeMillis() / 1000L);
    }
}
