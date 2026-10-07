/*
 * Decompiled with CFR 0.152.
 */
package shit.module.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import shit.Client;
import shit.event.Event2;
import shit.event.EventHandler;
import shit.module.Category;
import shit.module.Module;
import shit.module.misc.IRC;
import shit.setting.BooleanSetting;
import shit.util.MC;
import shit.util.Util2;

@Environment(value=EnvType.CLIENT)
public class Friend
extends Module {
    public static Friend INSTANCE;
    private final BooleanSetting mCF = (BooleanSetting)this.m28(new BooleanSetting("MCF", true));
    private final BooleanSetting sendMessage = (BooleanSetting)this.m28(new BooleanSetting("SendMessage", false));
    private final BooleanSetting sound = (BooleanSetting)this.m28(new BooleanSetting("Sound", true));
    private String text1363 = null;
    private int count137 = 0;

    public Friend() {
        super("Friend", "Manages your client-side friends.", Category.MISC);
        this.setFlag3(true);
        INSTANCE = this;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean m446(Object object) {
        String string = (String)object;
        String string2 = IRC.getText7();
        Friend friend = INSTANCE;
        if (string2 != null) {
            if (friend == null) return false;
            friend = INSTANCE;
        }
        boolean bl = friend.isSet19();
        if (string2 != null) {
            if (!bl) {
                return false;
            }
            bl = Client.manager.m258(string);
        }
        if (string2 == null) return bl;
        if (bl) return true;
        bl = Friend.m913(string);
        if (string2 == null) return bl;
        if (!bl) return false;
        return true;
    }

    @Override
    public void onEnable() {
        this.text1363 = null;
        this.count137 = 0;
    }

    @Override
    public void m709() {
        this.text1363 = null;
        this.count137 = 0;
    }

    @EventHandler
    private void setEvent2Inner7(Event2.Event2Inner event2Inner) {
        EntityHitResult entityHitResult;
        Object object;
        if (Module.isSet37()) {
            return;
        }
        if (this.text1363 != null && this.count137 > 0) {
            --this.count137;
            if (this.count137 == 0) {
                this.setObj110(this.text1363);
                this.text1363 = null;
            }
        }
        if (((Boolean)this.mCF.getObj()).booleanValue() && MC.client3.options.pickItemKey.wasPressed() && MC.client3.currentScreen == null && (object = MC.client3.crosshairTarget) instanceof EntityHitResult && (object = (entityHitResult = (EntityHitResult)object).getEntity()) instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity)object;
            if (Friend.m913(object = playerEntity.getGameProfile().name())) {
                Util2.setObj10("\u00a7d\u273f \u00a7f\u8fd9\u662f\u732b\u732b\u7684\u7279\u522b\u597d\u53cb\u54e6~ \u4e0d\u80fd\u79fb\u9664\u7684\u55b5~ \u00a7d\u273f");
                if (((Boolean)this.sound.getObj()).booleanValue()) {
                    MC.client3.world.playSoundClient(MC.client3.player.getX(), MC.client3.player.getY(), MC.client3.player.getZ(), SoundEvents.ENTITY_CAT_AMBIENT, SoundCategory.PLAYERS, 1.0f, 1.5f, false);
                }
                return;
            }
            boolean bl = Client.manager.m258(object);
            if (bl) {
                Client.manager.m933(object);
                Util2.setObj10("\u00a77\u2716 \u00a7c\u5df2\u79fb\u9664\u597d\u53cb\u00a7f: " + (String)object + " \u00a77\u2716 \u545c\u545c...");
                if (((Boolean)this.sound.getObj()).booleanValue()) {
                    MC.client3.world.playSoundClient(MC.client3.player.getX(), MC.client3.player.getY(), MC.client3.player.getZ(), (SoundEvent)SoundEvents.ENTITY_ITEM_BREAK.value(), SoundCategory.PLAYERS, 1.0f, 0.8f, false);
                }
            } else {
                Client.manager.m151(object);
                Util2.setObj10("\u00a7d\u2661 \u00a7a\u5df2\u6dfb\u52a0\u597d\u53cb\u00a7f: " + (String)object + " \u00a7d\u2661 \uff90\u30e3~");
                if (((Boolean)this.sound.getObj()).booleanValue()) {
                    MC.client3.world.playSoundClient(MC.client3.player.getX(), MC.client3.player.getY(), MC.client3.player.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.2f, false);
                }
                if (((Boolean)this.sendMessage.getObj()).booleanValue()) {
                    this.text1363 = (String)object;
                    this.count137 = 10;
                }
            }
        }
    }

    private void setObj110(Object object) {
        String string = (String)object;
        String string2 = IRC.getText7();
        if (string2 != null) {
            if (MC.client3.player == null) {
                return;
            }
            try {
                Util2.setObj14("w " + string + " I Just Add you as friend on Alien!");
                Util2.setObj10("\u00a7d\u2709 \u00a7f\u5df2\u5411 \u00a7a" + string + " \u00a7f\u53d1\u9001\u4e86\u52a0\u597d\u53cb\u79c1\u4fe1~ \u00a7d\u55b5\u2661");
            }
            catch (Exception exception) {
                Util2.setObj10("\u00a7c\u81ea\u52a8\u53d1\u9001\u79c1\u4fe1\u5931\u8d25\u4e86\u55b5... (\u0e51\u2022\u0301 \u2083 \u2022\u0300\u0e51)");
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean m913(Object object) {
        String string = (String)object;
        String string2 = IRC.getText7();
        boolean bl = string.equalsIgnoreCase("kiss_O_o");
        if (string2 == null) return bl;
        if (bl) return true;
        bl = string.equalsIgnoreCase("ssy_");
        if (string2 == null) return bl;
        if (bl) return true;
        bl = string.equalsIgnoreCase("e_2");
        if (string2 == null) return bl;
        if (bl) return true;
        bl = string.equalsIgnoreCase("LoveAstolfa");
        if (string2 == null) return bl;
        if (!bl) return false;
        return true;
    }
}

