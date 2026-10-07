/*
 * Decompiled with CFR 0.152.
 */
package shit.module.chat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import shit.event.EventHandler;
import shit.event.PacketEvent;
import shit.module.Category;
import shit.module.Module;
import shit.setting.BooleanSetting;
import shit.setting.StringSetting;
import shit.util.Util2;

@Environment(value=EnvType.CLIENT)
public class AutoReply
extends Module {
    private final BooleanSetting customMessage = (BooleanSetting)this.m28(new BooleanSetting("CustomMessage", false));
    private final StringSetting customText = (StringSetting)this.m28(new StringSetting("CustomText", "unchanged"));
    private String x = "";
    private long time36 = 0L;

    public AutoReply() {
        super("AutoReply", "Automatically reply to direct messages.", Category.CHAT);
    }

    @Override
    public void m709() {
        this.x = "";
        this.time36 = 0L;
    }

    @EventHandler
    private void setPacketEventInner13(PacketEvent.PacketEventInner packetEventInner) {
        boolean bl;
        String string = null;
        Packet packet = packetEventInner.getPacket();
        if (packet instanceof GameMessageS2CPacket) {
            GameMessageS2CPacket gameMessageS2CPacket = (GameMessageS2CPacket)packet;
            string = gameMessageS2CPacket.content().getString();
        } else {
            packet = packetEventInner.getPacket();
            if (packet instanceof ChatMessageS2CPacket) {
                ChatMessageS2CPacket chatMessageS2CPacket = (ChatMessageS2CPacket)packet;
                String string2 = string = chatMessageS2CPacket.unsignedContent() != null ? chatMessageS2CPacket.unsignedContent().getString() : chatMessageS2CPacket.body().content();
            }
        }
        if (string == null) {
            return;
        }
        boolean bl2 = bl = string.contains(" whispers") || string.contains(" -> ") || string.contains("tells you");
        if (!bl) {
            return;
        }
        long l = System.currentTimeMillis();
        if (string.equals(this.x) && l - this.time36 < 500L) {
            return;
        }
        this.x = string;
        this.time36 = l;
        String string3 = (Boolean)this.customMessage.getObj() != false ? (String)this.customText.getObj() : "I just automatically replied, thanks to Alien's AutoReply module!";
        Util2.setObj14("r " + string3);
    }
}

