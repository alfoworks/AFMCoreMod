package ru.allformine.afmcm.proxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.netty.buffer.ByteBuf;

public class cfnproxy
{
  
  @SubscribeEvent
  public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event)
  {
    ByteBuf buf = event.packet.payload();
    ByteBufUtils.readUTF8String(buf);
    ru.allformine.afmcm.vars.cfn = ByteBufUtils.readUTF8String(buf);
  }
}
