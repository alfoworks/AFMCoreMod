package ru.allformine.afmcm;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.io.PrintStream;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import ru.allformine.afmcm.gui.cfngui;
import ru.allformine.afmcm.proxy.CommonProxy;
import ru.allformine.afmcm.proxy.cfnproxy;
import ru.allformine.afmcm.rpc.rpci;

@Mod(modid="afmcm")
public class main
{
  public static Configuration config;
  public static FMLEventChannel channel;
  @SidedProxy(clientSide="ru.allformine.afmcm.proxy.ClientProxy", serverSide="ru.allformine.afmcm.proxy.CommonProxy")
  public static CommonProxy proxy;
  
  @Mod.EventHandler
  public void init(FMLInitializationEvent event)
  {
    cfnproxy handler = new cfnproxy();
    (channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("FactionsShow")).register(handler);
    MinecraftForge.EVENT_BUS.register(handler);
  }
  
  @SideOnly(Side.CLIENT)
  @SubscribeEvent(priority=EventPriority.NORMAL)
  public void onRenderGui(RenderGameOverlayEvent.Pre event)
  {
    if ((event.type == RenderGameOverlayEvent.ElementType.TEXT) && vars.cfn.length() > 0) {
      new cfngui(vars.cfn, Minecraft.getMinecraft());
    }
  }
  
  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
    MinecraftForge.EVENT_BUS.register(new main());
    FMLCommonHandler.instance().bus().register(new main());

    Configuration config = new Configuration(new File("config", "AFMCoreMod.cfg"));
    config.load();
    
    vars.ServerName = config.getString("serverName", "main", vars.ServerName, "Secret stuff");
    
    config.save();
  }
  
  @Mod.EventHandler
  public void init(FMLPostInitializationEvent event)
  {
    vars.Nick = Minecraft.getMinecraft().getSession().getUsername();
    vars.initTime = System.currentTimeMillis() / 1000L;
    
    System.out.println("[AFMCM] DiscordRPC is starting with nickname \"" + vars.Nick + "\" and servername \"" + vars.ServerName + "\"");
    rpci.start();
  }
}
