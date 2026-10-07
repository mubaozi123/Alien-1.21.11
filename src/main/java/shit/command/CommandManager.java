/*
 * Decompiled with CFR 0.152.
 */
package shit.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import shit.command.BindCommand;
import shit.command.Command;
import shit.command.ConfigCommand;
import shit.command.FriendCommand;
import shit.command.HelpCommand;
import shit.command.IrcCommand;
import shit.command.IrccrashCommand;
import shit.command.IrckickCommand;
import shit.command.KitCommand;
import shit.command.LoadCommand;
import shit.command.PrefixCommand;
import shit.command.SaveCommand;
import shit.command.ToggleCommand;
import shit.module.client.ClientSetting;
import shit.util.MC;

@Environment(value=EnvType.CLIENT)
public class CommandManager
implements MC {
    private final Map<String, Command> map4 = new LinkedHashMap<>();
    private String text2195 = ".";

    public void m351() {
        this.setObj51(new HelpCommand());
        this.setObj51(new ToggleCommand());
        this.setObj51(new BindCommand());
        this.setObj51(new FriendCommand());
        this.setObj51(new PrefixCommand());
        this.setObj51(new ConfigCommand());
        this.setObj51(new SaveCommand());
        this.setObj51(new LoadCommand());
        this.setObj51(new KitCommand());
        this.setObj51(new IrcCommand());
        this.setObj51(new IrckickCommand());
        this.setObj51(new IrccrashCommand());
    }

    public boolean m571(Object object) {
        String string = (String)object;
        Object var4_3 = null;
        if (!string.startsWith(this.text2195)) {
            return false;
        }
        String string2 = string.substring(this.text2195.length()).trim();
        if (string2.isEmpty()) {
            return true;
        }
        String[] stringArray = string2.split("\\s+");
        Command command = (Command)this.map4.get(stringArray[0].toLowerCase(Locale.ROOT));
        if (command == null) {
            CommandManager.setObj21("Unknown command. Try " + this.text2195 + "help.");
            return true;
        }
        String[] stringArray2 = new String[stringArray.length - 1];
        System.arraycopy(stringArray, 1, stringArray2, 0, stringArray2.length);
        try {
            command.run(stringArray2);
        } catch (RuntimeException e) {
            CommandManager.setObj21("Command '" + stringArray[0] + "' is not available in this build.");
        }
        return true;
    }

    public void setObj51(Object object) {
        Command command = (Command)object;
        this.map4.put(command.getText48().toLowerCase(Locale.ROOT), command);
    }

    public Collection<Command> getCollection() {
        return this.map4.values();
    }

    public String getText10() {
        return this.text2195;
    }

    public void setObj8(Object object) {
        block0: {
            String string = (String)object;
            Object var4_3 = null;
            if (string == null || string.isBlank()) break block0;
            this.text2195 = string;
        }
    }

    public static void setObj21(Object object) {
        String string = (String)object;
        Object var3_2 = null;
        if (MC.client3.inGameHud == null) {
            return;
        }
        int n = ClientSetting.INSTANCE != null ? (Integer)ClientSetting.INSTANCE.prefixColor.getObj() : -11141121;
        int n2 = n & 0xFFFFFF;
        Style style = Style.EMPTY.withColor(TextColor.fromRgb((int)n2));
        MutableText mutableText = Text.literal((String)"[Alien] ").fillStyle(style);
        MutableText mutableText2 = Text.literal((String)string);
        MC.client3.inGameHud.getChatHud().addMessage((Text)mutableText.copy().append((Text)mutableText2));
    }
}

