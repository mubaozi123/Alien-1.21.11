/*
 * Decompiled with CFR 0.152.
 */
package shit;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import shit.command.CommandManager;
import shit.manager.ConfigManager;
import shit.manager.EventBus;
import shit.manager.FontManager;
import shit.manager.Manager;
import shit.manager.ModuleManager;
import shit.misc.Helper;
import shit.misc.Helper4;
import shit.misc.Logger;
import shit.misc.Logger2;
import shit.misc.MathUtil;
import shit.misc.RenderUtil3;
import shit.module.Module;

@Environment(value=EnvType.CLIENT)
public final class Client {
    public static final EventBus eventBus = new EventBus();
    public static final ModuleManager moduleManager = new ModuleManager();
    public static final CommandManager commandManager = new CommandManager();
    public static final ConfigManager configManager = new ConfigManager();
    public static final Manager manager = new Manager();
    public static final FontManager fontManager = new FontManager();
    public static final Helper helper = new Helper();
    public static final MathUtil mathUtil = new MathUtil();
    public static final RenderUtil3 renderUtil3 = new RenderUtil3();
    public static final Helper4 helper4 = new Helper4();
    private static String[] texts2;

    private Client() {
    }

    public static void m841() {
        block0: {
            fontManager.m497();
            mathUtil.m935();
            helper.m25();
            moduleManager.init();
            commandManager.m351();
            configManager.m256();
            Object var1 = null;
            Logger.logger2.info("{} client initialized.", (Object)"Alien");
            if (null == null) break block0;
            Module.setTextArray9(new String[1]);
        }
    }

    /*
     * Unable to fully structure code
     */
    static {}

    public static void setTextArray2(String[] stringArray) {
        texts2 = stringArray;
    }

    public static String[] getTextArray7() {
        return texts2;
    }
}

