/*
 * Decompiled with CFR 0.152.
 */
package shit.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.lang.invoke.LambdaMetafactory;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.function.IntFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import shit.Client;
import shit.command.Command;
import shit.command.CommandManager;
import shit.command.ToggleCommand;
import shit.module.Module;
import shit.util.ApiEndpoints3;

@Environment(value=EnvType.CLIENT)
public class LoadCommand
extends Command {
    public LoadCommand() {
        super("load", "Loads local or Alien cloud configs.", "load [list|clone <all|module|binds> <name>]");
    }

    @Override
    public void run(Object object) {
        block5: {
            String string;
            block9: {
                block6: {
                    String[] stringArray;
                    block8: {
                        boolean bl;
                        block7: {
                            stringArray = (String[])object;
                            boolean bl2 = ToggleCommand.isSet106();
                            if (!bl2) break block5;
                            if (stringArray.length <= 0) break block6;
                            String string2 = stringArray[0].toLowerCase(Locale.ROOT);
                            bl = "list".equals(string2);
                            if (bl2) {
                                if (bl) {
                                    this.m565();
                                    return;
                                }
                                bl = "clone".equals(string2);
                            }
                            if (!bl2) break block7;
                            if (bl) break block8;
                            string = "cloud";
                            if (!bl2) break block9;
                            bl = string.equals(string2);
                        }
                        if (!bl) break block6;
                    }
                    this.setObj45(stringArray);
                    return;
                }
                Client.configManager.m256();
                string = "Config loaded.";
            }
            CommandManager.setObj21(string);
        }
    }

    @Override
    public String[] getSuggestions(int n, Object object) {
        String[] stringArray;
        block9: {
            String string;
            String[] stringArray2;
            int n2;
            block7: {
                block8: {
                    n2 = n;
                    stringArray2 = (String[])object;
                    boolean bl = false;
                    if (n2 == 1) {
                        return new String[]{"list", "clone", "cloud"};
                    }
                    if (n2 != 2) break block7;
                    if (stringArray2.length <= 1) break block7;
                    string = stringArray2[1].toLowerCase(Locale.ROOT);
                    if ("clone".equals(string)) break block8;
                    if (!"cloud".equals(string)) break block7;
                }
                return new String[]{"all", "module", "binds"};
            }
            if (n2 == 3) {
                if (stringArray2.length > 2) {
                    string = LoadCommand.m323(stringArray2[2]);
                    if (!string.isEmpty()) {
                        return this.fetchCloudNames(string);
                    }
                }
            }
            stringArray = new String[]{};
            if (!false) break block9;
            Module.setTextArray9(new String[3]);
        }
        return stringArray;
    }

    /*
     * Unable to fully structure code
     */
    private void m565() { throw new UnsupportedOperationException("deobf: reconstruct from bytecode"); }

    private void setObj45(Object object) {
        String[] stringArray = (String[])object;
        boolean bl = false;
        if (stringArray.length < 3) {
            CommandManager.setObj21("Usage: .load clone <all|module|binds> <name>");
            return;
        }
        String string = LoadCommand.m323(stringArray[1]);
        String string2 = stringArray[2].trim();
        if (string.isEmpty()) {
            CommandManager.setObj21("Unknown cloud config type. Use all, module, or binds.");
            return;
        }
        if (string2.isEmpty()) {
            CommandManager.setObj21("Cloud config name is required.");
            return;
        }
        CommandManager.setObj21("Downloading Alien cloud config " + string2 + "...");
        try {
            JsonObject jsonObject;
            block10: {
                block9: {
                    jsonObject = this.m652("/cloud-config/" + string + "/" + string2, true);
                    if (!jsonObject.has("ok")) break block9;
                    if (jsonObject.get("ok").getAsBoolean()) break block10;
                }
                CommandManager.setObj21("Cloud config download failed: " + LoadCommand.m555(jsonObject, "error"));
                return;
            }
            String string3 = LoadCommand.m555(jsonObject, "content");
            if (!Client.configManager.m731(string3, string)) {
                CommandManager.setObj21("Cloud config apply failed.");
                return;
            }
            Client.configManager.m1042();
            CommandManager.setObj21("Cloud config loaded: " + LoadCommand.m555(jsonObject, "name"));
        }
        catch (Exception exception) {
            CommandManager.setObj21("Cloud config failed: " + exception.getMessage());
        }
    }

    /*
     * Unable to fully structure code
     */
    private JsonObject m652(Object var1_1, boolean var2_2) throws Exception { throw new UnsupportedOperationException("deobf: reconstruct from bytecode"); }

    /*
     * Unable to fully structure code
     */
    private String[] fetchCloudNames(Object var1_1) { throw new UnsupportedOperationException("deobf: reconstruct from bytecode"); }

    private static String m323(Object object) {
        String string;
        String string2 = (String)object;
        boolean bl = false;
        if (string2 == null) {
            return "";
        }
        String string3 = string = string2.trim().toLowerCase(Locale.ROOT);
        int n = -1;
        switch (string3.hashCode()) {
            case 96673: {
                if (!string3.equals("all")) break;
                n = 0;
                if (!false) break;
            }
            case -1068784020: {
                if (!string3.equals("module")) break;
                n = 1;
                if (!false) break;
            }
            case 93742038: {
                if (!string3.equals("binds")) break;
                n = 2;
            }
        }
        return switch (n) {
            case 0, 1, 2 -> string;
            default -> "";
        };
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static String m555(Object object, Object object2) {
        JsonElement jsonElement;
        JsonObject jsonObject = (JsonObject)object;
        String string = (String)object2;
        boolean bl = ToggleCommand.isSet106();
        JsonObject jsonObject2 = jsonObject;
        if (bl) {
            if (jsonObject2 == null) return "";
            jsonObject2 = jsonObject;
        }
        boolean bl2 = jsonObject2.has(string);
        if (bl) {
            if (!bl2) return "";
            jsonElement = jsonObject.get(string);
            if (!bl) return jsonElement.getAsString();
            bl2 = jsonElement.isJsonNull();
        }
        if (bl2) {
            return "";
        }
        try {
            jsonElement = jsonObject.get(string);
            return jsonElement.getAsString();
        }
        catch (Exception exception) {
            return "";
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static String m375(Object object) {
        String string = (String)object;
        boolean bl = ToggleCommand.isSet106();
        String string2 = string;
        if (bl) {
            if (string2 == null) return "";
            string2 = string;
        }
        if (!bl) return string2;
        if (string2.length() < 10) return "";
        string2 = string.substring(0, 10);
        return string2;
    }

    private static String m692(Object object) {
        String string = (String)object;
        boolean bl = false;
        return URLEncoder.encode(string == null ? "" : string, StandardCharsets.UTF_8);
    }

    private static /* synthetic */ String[] cfrlam$fetchCloudNames$0(int n) {
        return new String[n];
    }
}

