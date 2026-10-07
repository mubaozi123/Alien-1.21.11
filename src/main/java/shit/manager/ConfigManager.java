/*
 * Decompiled with CFR 0.152.
 */
package shit.manager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import shit.Client;
import shit.misc.Logger;
import shit.module.Module;
import shit.setting.ColorSetting2;
import shit.setting.Setting;
import shit.util.Util4;

@Environment(value=EnvType.CLIENT)
public class ConfigManager {
    private final Path path3 = Path.of("alien", new String[0]);
    private final Path path5 = this.path3.resolve("configs");
    private final Path path = this.path3.resolve("options.txt");
    private final Path path2 = this.path3.resolve("friends.txt");
    private final Map map26 = new HashMap();
    private final Map map50 = new HashMap();
    private boolean flag31;

    public void m256() {
        this.m780(this.path, true);
    }

    public boolean m575(Object object) {
        Path path;
        block3: {
            block2: {
                String string = (String)object;
                path = this.m93(string);
                Object var4_4 = null;
                if (path == null) break block2;
                if (Files.exists(path, new LinkOption[0])) break block3;
            }
            return false;
        }
        this.m780(path, false);
        return true;
    }

    public boolean m731(Object object, Object object2) {
        String string;
        String string2;
        String string3;
        block15: {
            block14: {
                string3 = (String)object;
                string2 = (String)object2;
                Object var6_5 = null;
                if (string3 == null) break block14;
                if (!string3.isBlank()) break block15;
            }
            return false;
        }
        Map map = this.m218(string3.lines().toList());
        if (map.isEmpty()) {
            return false;
        }
        String string4 = string = string2 == null ? "" : string2.trim().toLowerCase(Locale.ROOT);
        int n = -1;
        switch (string4.hashCode()) {
            case 96673: {
                if (!string4.equals("all")) break;
                n = 0;
                if (null == null) break;
            }
            case -1068784020: {
                if (!string4.equals("module")) break;
                n = 1;
                if (null == null) break;
            }
            case 93742038: {
                if (!string4.equals("binds")) break;
                n = 2;
            }
        }
        switch (n) {
            case 0: {
                this.m977(map, true, true, true, true, false);
                if (null == null) break;
            }
            case 1: {
                this.m977(map, false, false, true, true, false);
                if (null == null) break;
            }
            case 2: {
                this.m977(map, false, false, false, false, true);
                if (null == null) break;
            }
            default: {
                return false;
            }
        }
        return true;
    }

    public void m1042() {
        this.m399(this.path, true);
    }

    public boolean m309(Object object) {
        String string = (String)object;
        Path path = this.m93(string);
        Object var4_4 = null;
        if (path == null) {
            return false;
        }
        this.m399(path, false);
        return true;
    }

    public List listProfiles() {
        ArrayList arrayList = new ArrayList();
        Object var2_2 = null;
        if (!Files.isDirectory(this.path5, new LinkOption[0])) {
            return arrayList;
        }
        try (Stream<Path> stream = Files.list(this.path5);){
            stream.filter(path -> {
                Object var1_1 = null;
                if (!Files.isRegularFile(path, new LinkOption[0])) return false;
                if (!path.getFileName().toString().endsWith(".txt")) return false;
                return true;
            }).map(path -> path.getFileName().toString()).map(string -> string.substring(0, string.length() - 4)).sorted(String.CASE_INSENSITIVE_ORDER).forEach(arrayList::add);
        }
        catch (IOException iOException) {
            Logger.logger2.error("Failed to list configs", (Throwable)iOException);
        }
        return arrayList;
    }

    public void m598() {
        Object var2_1 = null;
        if (!Util4.isSet51()) {
            return;
        }
        for (Module module : Client.moduleManager.getList6()) {
            Boolean bl = (Boolean)this.map50.get(module.getText69());
            if (bl != null) {
                module.setFlag3(bl);
            }
            if (null == null) continue;
        }
        this.flag31 = false;
    }

    public void m472() {
        this.flag31 = true;
    }

    public void setObj(Object object) {
        block5: {
            block4: {
                MinecraftClient minecraftClient = (MinecraftClient)object;
                Object var4_3 = null;
                if (!this.flag31) {
                    return;
                }
                if (minecraftClient == null) break block4;
                if (minecraftClient.player != null && minecraftClient.world != null) break block5;
            }
            return;
        }
        this.m598();
    }

    private void m780(Object object, boolean bl) {
        Path path = (Path)object;
        boolean bl2 = bl;
        this.setObj71(path);
        this.m977(this.map26, bl2, true, true, true, true);
    }

    private void m977(Object object, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5) {
        block12: {
            Map map = (Map)object;
            boolean bl6 = bl;
            boolean bl7 = bl2;
            boolean bl8 = bl3;
            boolean bl9 = bl4;
            boolean bl10 = bl5;
            String string = (String)map.get("client_prefix");
            Object var14_14 = null;
            if (bl7) {
                if (string != null) {
                    if (!string.isBlank()) {
                        Client.commandManager.setObj8(string);
                    }
                }
            }
            boolean bl11 = Util4.isSet51();
            for (Module module : Client.moduleManager.getList6()) {
                block10: {
                    block11: {
                        String string2 = (String)map.get(module.getText69() + "_state");
                        for (Setting setting : module.getList8()) {
                            boolean bl12 = setting instanceof ColorSetting2;
                            if (bl12) {
                                if (!bl10) continue;
                            }
                            if (!bl12 && !bl9) continue;
                            String string3 = (String)map.get(module.getText69() + "_" + setting.getText26());
                            if (string3 != null) {
                                try {
                                    setting.setObj58(string3);
                                } catch (RuntimeException e) {
                                }
                            }
                            if (null == null) continue;
                        }
                        if (!bl8) break block10;
                        if (string2 == null) break block10;
                        boolean bl13 = Boolean.parseBoolean(string2);
                        this.map50.put(module.getText69(), bl13);
                        if (!bl11) break block11;
                        try {
                            module.setFlag3(bl13);
                        } catch (RuntimeException e) {
                        }
                        if (null == null) break block10;
                    }
                    this.flag31 = true;
                }
                if (null == null) continue;
            }
            if (!bl6) break block12;
            this.m597();
        }
    }

    private void m399(Object object, boolean bl) {
        Path path = (Path)object;
        boolean bl2 = bl;
        Object var6_5 = null;
        try {
            Path path2 = path.getParent();
            if (path2 != null) {
                Files.createDirectories(path2, new FileAttribute[0]);
            }
            Files.createDirectories(this.path3, new FileAttribute[0]);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("client_prefix:").append(Client.commandManager.getText10()).append('\n');
            boolean bl3 = Util4.isSet51();
            for (Module module : Client.moduleManager.getList6()) {
                boolean bl4;
                block11: {
                    block10: {
                        if (bl3) break block10;
                        if (!this.map50.containsKey(module.getText69())) break block10;
                        bl4 = (Boolean)this.map50.get(module.getText69());
                        if (null == null) break block11;
                    }
                    bl4 = module.isSet19();
                    this.map50.put(module.getText69(), bl4);
                }
                stringBuilder.append(module.getText69()).append("_state:").append(bl4).append('\n');
                for (Setting setting : module.getList8()) {
                    stringBuilder.append(module.getText69()).append('_').append(setting.getText26()).append(':').append(setting.getText29()).append('\n');
                    if (null == null) continue;
                }
                if (null == null) continue;
            }
            Files.writeString(path, (CharSequence)stringBuilder.toString(), StandardCharsets.UTF_8, new OpenOption[0]);
            if (bl2) {
                Files.write(this.path2, (Iterable<? extends CharSequence>)Client.manager.getSet2(), StandardCharsets.UTF_8, new OpenOption[0]);
            }
        }
        catch (IOException iOException) {
            Logger.logger2.error("Failed to save config", (Throwable)iOException);
        }
    }

    private void setObj71(Object object) {
        Path path = (Path)object;
        this.map26.clear();
        Object var4_3 = null;
        if (!Files.exists(path, new LinkOption[0])) {
            return;
        }
        try {
            this.map26.putAll(this.m218(Files.readAllLines(path, StandardCharsets.UTF_8)));
        }
        catch (IOException iOException) {
            Logger.logger2.error("Failed to read config", (Throwable)iOException);
        }
    }

    private Map m218(Object object) {
        List list = (List)object;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        Iterator iterator = list.iterator();
        Object var4_5 = null;
        while (iterator.hasNext()) {
            String string = (String)iterator.next();
            int n = string.indexOf(58);
            if (n <= 0) continue;
            hashMap.put(string.substring(0, n), string.substring(n + 1));
            if (null == null) continue;
        }
        return hashMap;
    }

    private void m597() {
        Client.manager.m624();
        Object var2_1 = null;
        if (!Files.exists(this.path2, new LinkOption[0])) {
            return;
        }
        try {
            List<String> list = Files.readAllLines(this.path2, StandardCharsets.UTF_8);
            for (String string : list) {
                if (!string.isBlank()) {
                    Client.manager.m151(string.trim());
                }
                if (null == null) continue;
                break;
            }
        }
        catch (IOException iOException) {
            Logger.logger2.error("Failed to read friends", (Throwable)iOException);
        }
    }

    private Path m93(Object object) {
        String string = (String)object;
        String string2 = ConfigManager.m873(string);
        if (string2.isEmpty()) {
            return null;
        }
        return this.path5.resolve(string2 + ".txt");
    }

    private static String m873(Object object) {
        String string = (String)object;
        Object var3_2 = null;
        if (string == null) {
            return "";
        }
        String string2 = string.trim().toLowerCase(Locale.ROOT);
        if (string2.isEmpty()) {
            return "";
        }
        return string2.replaceAll("[^a-z0-9._-]", "_");
    }
}

