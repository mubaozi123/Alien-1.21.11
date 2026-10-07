/*
 * Decompiled with CFR 0.152.
 */
package shit.module.chat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import shit.module.Category;
import shit.module.Module;
import shit.module.chat.ChatTimestamp;
import shit.setting.BooleanSetting;
import shit.setting.EnumSetting;
import shit.setting.StringSetting;

@Environment(value=EnvType.CLIENT)
public class ChatSuffix
extends Module {
    public static ChatSuffix INSTANCE;
    private final EnumSetting message = (EnumSetting)this.m28(new EnumSetting("Message", MessageMode.NAME));
    private final EnumSetting separator = (EnumSetting)this.m28(new EnumSetting("Separator", SeparatorMode.NONE));
    private final BooleanSetting commands = (BooleanSetting)this.m28(new BooleanSetting("Commands", false));
    private final StringSetting customText = (StringSetting)this.m28(new StringSetting("CustomText", "Alien"));

    public ChatSuffix() {
        super("ChatSuffix", "Add a custom suffix to your chat messages.", Category.CHAT);
        INSTANCE = this;
    }

    public String m778(Object object) {
        String string;
        String string2;
        int[] nArray;
        block6: {
            String string3;
            block5: {
                boolean bl;
                block4: {
                    string3 = (String)object;
                    nArray = ChatTimestamp.getIntArray2();
                    bl = (Boolean)this.commands.getObj();
                    if (nArray == null) break block4;
                    if (bl) break block5;
                    string2 = string3;
                    string = "/";
                    if (nArray == null) break block6;
                    bl = string2.startsWith(string);
                }
                if (bl) {
                    return string3;
                }
            }
            string2 = string3;
            string = this.getText6();
        }
        String string4 = string2 + string;
        Object object2 = string4;
        if (nArray != null) {
            object2 = ((String)object2).length() > 256 ? string4.substring(0, 256) : string4;
        }
        return (String)object2;
    }

    /*
     * Unable to fully structure code
     */
    private String getText6() {
        String base = this.message.getObj() == MessageMode.NAME ? "Alien" : (String)this.customText.getObj();
        SeparatorMode separatorMode = (SeparatorMode)this.separator.getObj();
        if (separatorMode == SeparatorMode.SEPARATOR) {
            return " | " + base;
        }
        if (separatorMode == SeparatorMode.CLASSIC) {
            return " \u00ab " + base + " \u00bb";
        }
        return " " + base;
    }

    @Environment(value=EnvType.CLIENT)
    public static enum SeparatorMode {
      NONE, SEPARATOR, CLASSIC;

      private SeparatorMode() {}



        private static SeparatorMode[] getSeparatorModeArray() {
            return new SeparatorMode[]{NONE, SEPARATOR, CLASSIC};
        }
    
   }

    @Environment(value=EnvType.CLIENT)
    public static enum MessageMode {
      NAME, CUSTOM;

      private MessageMode() {}



        private static MessageMode[] getMessageModeArray() {
            return new MessageMode[]{NAME, CUSTOM};
        }
    
   }
}

