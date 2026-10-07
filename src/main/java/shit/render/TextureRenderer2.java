/*
 * Decompiled with CFR 0.152.
 */
package shit.render;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import shit.data.ResourceEntry;
import shit.module.Module;

@Environment(value=EnvType.CLIENT)
public final class TextureRenderer2 {
    private static final Map map38 = new java.util.LinkedHashMap<>();
    private static String text170;

    private TextureRenderer2() {
    }

    public static ResourceEntry fetchSkin(Object object) {
        String string = (String)object;
        String string2 = string.toLowerCase();
        ResourceEntry resourceEntry = TextureRenderer2.m481(string);
        Object var3_4 = null;
        if (resourceEntry != null) {
            return resourceEntry;
        }
        try {
            String string3 = TextureRenderer2.m330(string);
            if (string3 == null) {
                return null;
            }
            Data data = TextureRenderer2.m408(string3);
            if (data == null) {
                return null;
            }
            NativeImage nativeImage = TextureRenderer2.m296(data);
            if (nativeImage == null) {
                return null;
            }
            Identifier identifier = Identifier.of((String)"alien", (String)("skin/custom_" + TextureRenderer2.m525(string2)));
            ResourceEntry resourceEntry2 = new ResourceEntry(identifier, data.flag8(), string, System.currentTimeMillis());
            map38.put(string2, resourceEntry2);
            NativeImage nativeImage2 = nativeImage;
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            minecraftClient.execute(() -> minecraftClient.getTextureManager().registerTexture(identifier, (AbstractTexture)new NativeImageBackedTexture(() -> identifier.toString(), nativeImage2)));
            return resourceEntry2;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static ResourceEntry m481(Object object) {
        block3: {
            String string = (String)object;
            ResourceEntry resourceEntry = (ResourceEntry)map38.get(string.toLowerCase());
            Object var3_3 = null;
            if (resourceEntry != null) {
                if (!resourceEntry.isSet3()) {
                    return resourceEntry;
                }
            }
            if (resourceEntry != null) {
                map38.remove(string.toLowerCase());
            }
            if (Module.getTextArray9() != null) break block3;
            TextureRenderer2.setText15("KpCo7");
        }
        return null;
    }

    private static String m330(Object object) throws Exception {
        String string = (String)object;
        URL uRL = URI.create("https://api.mojang.com/users/profiles/minecraft/" + string).toURL();
        HttpURLConnection httpURLConnection = TextureRenderer2.m1052(uRL, 5000);
        Object var3_4 = null;
        try {
            if (httpURLConnection.getResponseCode() != 200) {
                String string2 = null;
                return string2;
            }
            JsonObject jsonObject = TextureRenderer2.m22(httpURLConnection);
            String string3 = jsonObject.has("id") ? jsonObject.get("id").getAsString() : null;
            return string3;
        }
        finally {
            httpURLConnection.disconnect();
        }
    }

    private static Data m408(Object object) throws Exception {
        String string = (String)object;
        URL uRL = URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + string.replace("-", "")).toURL();
        HttpURLConnection httpURLConnection = TextureRenderer2.m1052(uRL, 5000);
        Object var3_4 = null;
        try {
            JsonObject jsonObject;
            block19: {
                block18: {
                    JsonObject jsonObject2;
                    if (httpURLConnection.getResponseCode() != 200) {
                        Data data = null;
                        return data;
                    }
                    JsonObject jsonObject3 = TextureRenderer2.m22(httpURLConnection);
                    JsonArray jsonArray = jsonObject3.getAsJsonArray("properties");
                    if (jsonArray == null) {
                        Data data = null;
                        return data;
                    }
                    String string2 = null;
                    for (int i = 0; i < jsonArray.size(); ++i) {
                        jsonObject2 = jsonArray.get(i).getAsJsonObject();
                        String string3 = jsonObject2.has("name") ? jsonObject2.get("name").getAsString() : "";
                        if (!"textures".equals(string3)) continue;
                        string2 = jsonObject2.has("value") ? jsonObject2.get("value").getAsString() : null;
                        break;
                    }
                    if (string2 == null) {
                        Data data = null;
                        return data;
                    }
                    JsonObject jsonObject4 = JsonParser.parseString((String)new String(Base64.getDecoder().decode(string2), StandardCharsets.UTF_8)).getAsJsonObject();
                    jsonObject2 = jsonObject4.getAsJsonObject("textures");
                    if (jsonObject2 == null) {
                        Data data = null;
                        return data;
                    }
                    jsonObject = jsonObject2.getAsJsonObject("SKIN");
                    if (jsonObject == null) break block18;
                    if (jsonObject.has("url")) break block19;
                }
                Data data = null;
                return data;
            }
            boolean bl = false;
            JsonObject jsonObject5 = jsonObject.getAsJsonObject("metadata");
            if (jsonObject5 != null) {
                if (jsonObject5.has("model")) {
                    bl = "slim".equals(jsonObject5.get("model").getAsString());
                }
            }
            Data data = new Data(jsonObject.get("url").getAsString(), bl);
            return data;
        }
        finally {
            httpURLConnection.disconnect();
        }
    }

    private static NativeImage m296(Object object) throws Exception {
        Data data = (Data)object;
        HttpURLConnection httpURLConnection = TextureRenderer2.m1052(URI.create(data.text12()).toURL(), 10000);
        Object var3_3 = null;
        try {
            if (httpURLConnection.getResponseCode() != 200) {
                NativeImage nativeImage = null;
                return nativeImage;
            }
            NativeImage nativeImage = NativeImage.read((InputStream)httpURLConnection.getInputStream());
            return nativeImage;
        }
        finally {
            httpURLConnection.disconnect();
        }
    }

    private static HttpURLConnection m1052(Object object, int n) throws java.io.IOException {
        URL uRL = (URL)object;
        int n2 = n;
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(n2);
        httpURLConnection.setRequestProperty("User-Agent", "Alien/1.0");
        return httpURLConnection;
    }

    private static JsonObject m22(Object object) throws java.io.IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection)object;
        Object var3_3 = null;
        try (InputStream inputStream = httpURLConnection.getInputStream();){
            JsonObject jsonObject = JsonParser.parseString((String)new String(inputStream.readAllBytes(), StandardCharsets.UTF_8)).getAsJsonObject();
            return jsonObject;
        }
    }

    private static String m525(Object object) {
        String string = (String)object;
        return string.replaceAll("[^a-z0-9/._-]", "_");
    }

    private static /* synthetic */ void cfrlam$registerTexture$3(MinecraftClient minecraftClient, Identifier identifier, NativeImage nativeImage) {
        minecraftClient.getTextureManager().registerTexture(identifier, (AbstractTexture)new NativeImageBackedTexture(() -> identifier.toString(), nativeImage));
    }

    /*
     * Unable to fully structure code
     */
    static {}

    public static void setText15(String string) {
        text170 = string;
    }

    public static String getText70() {
        return text170;
    }

    @Environment(value=EnvType.CLIENT)
    record Data(String text12, boolean flag8) {
    }
}

