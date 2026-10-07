/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.Client;
import shit.event.PacketEvent;
import shit.event.PlayerEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={ClientConnection.class})
public class ConnectionMixin {
    @Inject(method={"sendImmediately(Lnet/minecraft/network/packet/Packet;Lio/netty/channel/ChannelFutureListener;Z)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$sendPacket(Packet packet, ChannelFutureListener channelFutureListener, boolean bl, CallbackInfo callbackInfo) {
        if (((PacketEvent.PacketEventInner2)Client.eventBus.m287(new PacketEvent.PacketEventInner2(packet))).isSet85()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$channelRead(ChannelHandlerContext channelHandlerContext, Packet packet, CallbackInfo callbackInfo) {
        EntityStatusS2CPacket entityStatusS2CPacket;
        if (((PacketEvent.PacketEventInner)Client.eventBus.m287(new PacketEvent.PacketEventInner(packet))).isSet85()) {
            callbackInfo.cancel();
            return;
        }
        if (packet instanceof EntityStatusS2CPacket && (entityStatusS2CPacket = (EntityStatusS2CPacket)packet).getStatus() == 35) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            minecraftClient.execute(() -> {
                if (minecraftClient.world == null) {
                    return;
                }
                Entity entity = entityStatusS2CPacket.getEntity((World)minecraftClient.world);
                if (entity instanceof PlayerEntity) {
                    PlayerEntity playerEntity = (PlayerEntity)entity;
                    Client.eventBus.m287(new PlayerEvent(playerEntity));
                }
            });
        }
    }
}

