package ru.allformine.afmcm;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.lwjgl.input.Keyboard;
import ru.allformine.afmcm.gui.NotifyGui;
import ru.allformine.afmcm.gui.TerritoryShowGui;
import ru.allformine.afmcm.music.MusicThread;
import ru.allformine.afmcm.proxy.*;
import ru.allformine.afmcm.rpc.rpci;

import java.io.File;

@Mod(modid = "afmcm")
public class AFMCoreMod {
    private static FMLEventChannel channel;
    @SidedProxy(clientSide = "ru.allformine.afmcm.proxy.ClientProxy", serverSide = "ru.allformine.afmcm.proxy.CommonProxy")
    public static CommonProxy proxy;
    static Configuration config;
    public static KeyBinding SWITCH_MUSIC= new KeyBinding("Включить/выключить фоновую музыку", Keyboard.KEY_M, "Фоновая музыка");

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NotifierProxy notifyHandler = new NotifierProxy();
        (channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("Notify")).register(notifyHandler);
        MinecraftForge.EVENT_BUS.register(notifyHandler);
        ScreenshotProxy screenshotHandler = new ScreenshotProxy();
        (channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("C234Fb")).register(screenshotHandler);
        MinecraftForge.EVENT_BUS.register(screenshotHandler);
        AmbientProxy ambientProxy = new AmbientProxy();
        (channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("ambient")).register(ambientProxy);
        MinecraftForge.EVENT_BUS.register(ambientProxy);
        TerritoryShowProxy tsProxy = new TerritoryShowProxy();
        (channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("territoryshow")).register(tsProxy);
        MinecraftForge.EVENT_BUS.register(tsProxy);

        ClientRegistry.registerKeyBinding(SWITCH_MUSIC);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderGui(RenderGameOverlayEvent.Pre event) {
        if ((event.type == RenderGameOverlayEvent.ElementType.TEXT)) {
            if(References.notifyText.size() > 0 && References.notifyDrawing) {
                new NotifyGui(References.notifyText, Minecraft.getMinecraft(), event);
            }

            if(References.territoryText.length() > 0) {
                new TerritoryShowGui(References.territoryText, Minecraft.getMinecraft(), event);
            }
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new AFMCoreMod());
        FMLCommonHandler.instance().bus().register(new AFMCoreMod());

        config = new Configuration(new File("config", "AFMCoreMod.cfg"));
        config.load();

        References.rpcAppId = config.getString("rpcAppId", "discord", References.rpcAppId, "Secret stuff");
        References.serverName = config.getString("serverName", "discord", References.serverName, "Secret stuff");
        References.bigImageKey = config.getString("bigImageKey", "discord", References.bigImageKey, "Secret stuff");
        References.musicEnabled = config.getBoolean("musicEnabled", "music", References.musicEnabled, "Hui");


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

        if (References.activePlayer != null) {
            References.activePlayer.close();
            References.activePlayer = null;
            References.activeMusicUrl = "";
        }

        if (References.territoryText.length() > 0) {
            References.territoryText = "";
        }
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

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onKeyInputEvent(InputEvent.KeyInputEvent event) {
        if(SWITCH_MUSIC.isPressed()) {
            References.musicEnabled = !References.musicEnabled;
            config.save();

            if(References.musicEnabled) {
                if(References.activeMusicUrl != null) {
                    new MusicThread(References.activeMusicUrl).run();
                }
            } else {
                if (References.activePlayer != null) {
                    References.activePlayer.close();
                    References.activePlayer = null;
                    References.activeMusicUrl = "";
                }
            }
        }
    }
}
