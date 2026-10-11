/*
 * Decompiled with CFR 0.152.
 */
package shit.module.misc;

import java.util.HashSet;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import shit.event.Event2;
import shit.event.EventHandler;
import shit.module.Category;
import shit.module.Module;
import shit.setting.BooleanSetting;
import shit.setting.StringSetting;
import shit.util.MC;
import shit.util.Util2;

@Environment(value=EnvType.CLIENT)
public class AutoEZ
extends Module {
    private final BooleanSetting self = (BooleanSetting)this.m28(new BooleanSetting("Self", false));
    private final StringSetting message = (StringSetting)this.m28(new StringSetting("Message", "gg %s"));
    private final Set set3 = new HashSet();

    public AutoEZ() {
        super("AutoEZ", "Sends a message when nearby players die.", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        this.set3.clear();
    }

    @Override
    public void m709() {
        this.set3.clear();
    }

    @EventHandler
    private void setEvent2Inner22(Event2.Event2Inner2 event2Inner2) {
        if (Module.isSet37()) {
            return;
        }
        for (PlayerEntity playerEntity : MC.client3.world.getPlayers()) {
            if (playerEntity == MC.client3.player && !((Boolean)this.self.getObj()).booleanValue()) continue;
            if (playerEntity.isAlive()) {
                this.set3.add(playerEntity.getUuid());
                continue;
            }
            if (!this.set3.remove(playerEntity.getUuid())) continue;
            Util2.setObj62(String.format((String)this.message.getObj(), playerEntity.getName().getString()));
        }
    }
}

