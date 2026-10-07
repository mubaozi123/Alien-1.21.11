/*
 * Decompiled with CFR 0.152.
 */
package shit.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import shit.manager.SystemManager;
import shit.util.ApiEndpoints;
import shit.util.AuthUtil;

@Environment(value=EnvType.CLIENT)
public final class HttpUtil {
    private static volatile Data data2;
    private static volatile boolean flag78;
    private static volatile boolean flag166;
    private static volatile long time64;
    private static volatile boolean flag43;
    private static volatile String text1914;
    private static volatile String text470;

    private HttpUtil() {
    }

    public static boolean isSet123() {
        return HttpUtil.m58("startup");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isSet44() {
        Object var1 = null;
        if (!flag166) {
            return false;
        }
        long l = time64;
        if (l <= 0L) return false;
        if (System.currentTimeMillis() - l >= 600000L) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String getText28() {
        Object var1 = null;
        if (text1914 == null) return "Start Alien through the personalized Loader.";
        if (text1914.isBlank()) {
            return "Start Alien through the personalized Loader.";
        }
        String string = text1914;
        return string;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isSet169() {
        Data data = HttpUtil.getData();
        Object var1_1 = null;
        if (data == null) return false;
        if (!"alien".equals(data.text15())) return false;
        if (!HttpUtil.isSet115()) return false;
        if (HttpUtil.m449("ALIEN_LAUNCH_SECRET").isBlank()) return false;
        if (HttpUtil.m449("ALIEN_LAUNCH_NONCE").isBlank()) return false;
        return true;
    }

    public static synchronized boolean m58(Object object) {
        String string;
        String string2;
        String string3;
        Data data;
        String string4;
        block14: {
            block13: {
                string4 = (String)object;
                data = HttpUtil.getData();
                Object var3_3 = null;
                if (data == null) {
                    return HttpUtil.m863("Loader session is missing. Download and start the personalized Alien Loader.");
                }
                if (!"alien".equals(data.text15())) {
                    return HttpUtil.m863("Loader session is for another client.");
                }
                if (!HttpUtil.isSet115()) {
                    return HttpUtil.m863("This runtime must be started by the Alien Loader.");
                }
                string3 = HttpUtil.m449("ALIEN_LAUNCH_SECRET");
                string2 = HttpUtil.m449("ALIEN_LAUNCH_NONCE");
                string = HttpUtil.m449("ALIEN_SESSION_ID");
                if (string3.isBlank()) break block13;
                if (!string2.isBlank()) break block14;
            }
            return HttpUtil.m863("Loader launch proof is missing.");
        }
        if (!string.isBlank()) {
            if (!HttpUtil.m229(string, data.text14())) {
                return HttpUtil.m863("Loader launch proof does not match this runtime.");
            }
        }
        try {
            String string5;
            block16: {
                block15: {
                    string5 = HttpUtil.m301("/loader/launch-verify", HttpUtil.m112(data, string3, string2, string4));
                    if (string5.contains("\"allowed\":true")) break block15;
                    if (!string5.contains("\"ok\":true")) break block16;
                }
                flag166 = true;
                time64 = System.currentTimeMillis();
                text1914 = "Loader session verified.";
                HttpUtil.startHeartbeat();
                return true;
            }
            return HttpUtil.m863(HttpUtil.m242(string5, "Loader session rejected."));
        }
        catch (Throwable throwable) {
            return HttpUtil.m863("Loader session verification failed: " + HttpUtil.m664(throwable));
        }
    }

    private static synchronized void startHeartbeat() {
        Object var1 = null;
        if (flag43) {
            return;
        }
        flag43 = true;
        Thread thread = new Thread(() -> {
            int n = 0;
            Object var0_1 = null;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(60000L);
                }
                catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    if (null == null) break;
                }
                if (HttpUtil.isSet52()) {
                    n = 0;
                    if (null == null) continue;
                }
                if (HttpUtil.m550(HttpUtil.getText28())) {
                    if (++n < 6 && null == null) continue;
                }
                AuthUtil.setObj59(HttpUtil.getText28());
                try {
                    MinecraftClient minecraftClient = MinecraftClient.getInstance();
                    if (minecraftClient == null) break;
                    minecraftClient.execute(() -> AuthUtil.m606(minecraftClient, HttpUtil.getText28()));
                }
                catch (Throwable throwable) {}
                break;
            }
        }, "Alien-Loader-Session");
        thread.setDaemon(true);
        thread.start();
    }

    private static boolean isSet52() {
        Data data = HttpUtil.getData();
        Object var1_1 = null;
        if (data == null) {
            return HttpUtil.m863("Loader session is missing.");
        }
        try {
            String string;
            block7: {
                block6: {
                    string = HttpUtil.m301("/loader/runtime-heartbeat", HttpUtil.m112(data, HttpUtil.m449("ALIEN_LAUNCH_SECRET"), HttpUtil.m449("ALIEN_LAUNCH_NONCE"), "heartbeat"));
                    if (string.contains("\"allowed\":true")) break block6;
                    if (!string.contains("\"ok\":true")) break block7;
                }
                flag166 = true;
                time64 = System.currentTimeMillis();
                text1914 = "Loader session active.";
                return true;
            }
            return HttpUtil.m863(HttpUtil.m242(string, "Loader session expired."));
        }
        catch (Throwable throwable) {
            String string = "Loader heartbeat failed: " + HttpUtil.m664(throwable);
            return HttpUtil.m550(string) ? HttpUtil.m861(string) : HttpUtil.m863(string);
        }
    }

    private static String m112(Object object, Object object2, Object object3, Object object4) {
        Data data = (Data)object;
        String string = (String)object2;
        String string2 = (String)object3;
        String string3 = (String)object4;
        String string4 = HttpUtil.m449("ALIEN_PACKAGE_SHA256");
        Object var9_9 = null;
        if (string4.isBlank()) {
            string4 = data.text18();
        }
        String string5 = HttpUtil.m956(data.text19());
        String string6 = HttpUtil.m956(HttpUtil.getText63());
        String string7 = HttpUtil.m956(string4);
        String string8 = HttpUtil.m956(string3);
        String string9 = HttpUtil.m956("1.0.0");
        String string10 = HttpUtil.m956(HttpUtil.getText15());
        String string11 = HttpUtil.m956(string2);
        String string12 = HttpUtil.m956(string);
        String string13 = HttpUtil.m956(data.text14());
        return "{\"sessionId\":\"" + string13 + "\",\"launchSecret\":\"" + string12 + "\",\"launchNonce\":\"" + string11 + "\",\"hwid\":\"" + string10 + "\",\"client\":\"alien\",\"version\":\"" + string9 + "\",\"phase\":\"" + string8 + "\",\"packageSha256\":\"" + string7 + "\",\"runtimeSha256\":\"" + string6 + "\",\"watermarkSig\":\"" + string5 + "\"}";
    }

    /*
     * Unable to fully structure code
     */
    private static String m301(Object var0, Object var1_1) { throw new UnsupportedOperationException("deobf: reconstruct from bytecode"); }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Data getData() {
        Object var1 = null;
        if (flag78) {
            return data2;
        }
        Class<HttpUtil> clazz = HttpUtil.class;
        synchronized (HttpUtil.class) {
            if (flag78) {
                return data2;
            }
            flag78 = true;
            try (InputStream inputStream = HttpUtil.class.getResourceAsStream("/assets/alien/loader-session.dat");){
                long l;
                String string;
                String string2;
                String string3;
                String string4;
                String string5;
                String string6;
                block19: {
                    block18: {
                        if (inputStream == null) {
                            Data data = null;
                            return data;
                        }
                        String string7 = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).trim();
                        string6 = HttpUtil.m914(string7, "sid");
                        string5 = HttpUtil.m914(string7, "client");
                        string4 = HttpUtil.m914(string7, "owner");
                        string3 = HttpUtil.m914(string7, "tokenId");
                        string2 = HttpUtil.m914(string7, "sha256");
                        string = HttpUtil.m914(string7, "sig");
                        l = HttpUtil.m784(HttpUtil.m914(string7, "exp"));
                        if (string6.isBlank()) break block18;
                        if (!string5.isBlank() && !string2.isBlank()) break block19;
                    }
                    Data data = null;
                    return data;
                }
                Data data = data2 = new Data(string6, string5, string4, string3, string2, string, l);
                return data;
            }
            catch (Throwable throwable) {
                data2 = null;
                return null;
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static boolean isSet115() {
        Object var1 = null;
        if ("1".equals(HttpUtil.m449("ALIEN_LOADER_LAUNCH"))) return true;
        if (!"true".equalsIgnoreCase(System.getProperty("alien.loader.launch", ""))) return false;
        return true;
    }

    private static String getText63() {
        String string = text470;
        Object var1_1 = null;
        if (!string.isBlank()) {
            return string;
        }
        try {
            block16: {
                block15: {
                    if (HttpUtil.class.getProtectionDomain() == null) break block15;
                    if (HttpUtil.class.getProtectionDomain().getCodeSource() != null) break block16;
                }
                return "";
            }
            Path path = Path.of(HttpUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (!Files.isRegularFile(path, new LinkOption[0])) {
                return "";
            }
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);
            try {
                int n;
                byte[] byArray = new byte[16384];
                while ((n = inputStream.read(byArray)) > 0) {
                    messageDigest.update(byArray, 0, n);
                    if (null == null) continue;
                }
            }
            catch (Throwable throwable) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    }
                    catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                }
                throw throwable;
            }
            {}
            if (inputStream != null) {
                inputStream.close();
            }
            text470 = HexFormat.of().formatHex(messageDigest.digest());
            return text470;
        }
        catch (Throwable throwable) {
            return "";
        }
    }

    private static String getText15() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            HttpUtil.m455(messageDigest, SystemManager.m849("user.name"));
            HttpUtil.m455(messageDigest, SystemManager.m849("os.name"));
            HttpUtil.m455(messageDigest, SystemManager.m849("os.arch"));
            HttpUtil.m455(messageDigest, SystemManager.m576("COMPUTERNAME"));
            HttpUtil.m455(messageDigest, SystemManager.m576("PROCESSOR_IDENTIFIER"));
            HttpUtil.m455(messageDigest, SystemManager.m576("PROCESSOR_ARCHITECTURE"));
            return HexFormat.of().formatHex(messageDigest.digest());
        }
        catch (Throwable throwable) {
            return "unknown";
        }
    }

    private static void m455(Object object, Object object2) {
        MessageDigest messageDigest = (MessageDigest)object;
        String string = (String)object2;
        Object var5_4 = null;
        if (string != null) {
            messageDigest.update(string.getBytes(StandardCharsets.UTF_8));
        }
        messageDigest.update((byte)0);
    }

    /*
     * Unable to fully structure code
     */
    private static boolean m863(Object var0) { throw new UnsupportedOperationException("deobf: reconstruct from bytecode"); }

    /*
     * Unable to fully structure code
     */
    private static boolean m861(Object var0) { throw new UnsupportedOperationException("deobf: reconstruct from bytecode"); }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static boolean m550(Object object) {
        String string = (String)object;
        Object var3_2 = null;
        if (string == null) return false;
        if (string.isBlank()) {
            return false;
        }
        if (string.contains("SocketTimeout")) return true;
        if (string.contains("UnknownHost")) return true;
        if (string.contains("ConnectException")) return true;
        if (string.contains("SSLException")) return true;
        if (string.contains("SocketException")) return true;
        if (string.contains("Connection reset")) return true;
        if (string.contains("timed out")) return true;
        if (string.contains("HTTP 5")) return true;
        if (string.contains("HTTP 429")) return true;
        if (!string.contains("HTTP 0")) return false;
        return true;
    }

    private static String m242(Object object, Object object2) {
        String string = (String)object;
        String string2 = (String)object2;
        String string3 = HttpUtil.m914(string, "error");
        Object var5_5 = null;
        if (string3.isBlank()) {
            string3 = HttpUtil.m914(string, "reason");
        }
        return string3.isBlank() ? string2 : string3;
    }

    private static String m664(Object object) {
        String string;
        Throwable throwable;
        block5: {
            block4: {
                throwable = (Throwable)object;
                Object var3_2 = null;
                if (throwable == null) {
                    return "unknown";
                }
                string = throwable.getMessage();
                if (string == null) break block4;
                if (!string.isBlank()) break block5;
            }
            return throwable.getClass().getSimpleName();
        }
        return throwable.getClass().getSimpleName() + ": " + string;
    }

    private static String m449(Object object) {
        String string = (String)object;
        String string2 = System.getenv(string);
        Object var3_3 = null;
        return string2 == null ? "" : string2.trim();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static long m784(Object object) {
        String string = (String)object;
        Object var3_2 = null;
        try {
            if (string == null) return 0L;
            if (string.isBlank()) {
                return 0L;
            }
            long l = Long.parseLong(string.trim());
            return l;
        }
        catch (Throwable throwable) {
            return 0L;
        }
    }

    private static boolean m229(Object object, Object object2) {
        String string;
        String string2;
        block3: {
            block2: {
                string2 = (String)object;
                string = (String)object2;
                Object var5_4 = null;
                if (string2 == null) break block2;
                if (string != null) break block3;
            }
            return false;
        }
        return MessageDigest.isEqual(string2.getBytes(StandardCharsets.UTF_8), string.getBytes(StandardCharsets.UTF_8));
    }

    private static String m914(Object object, Object object2) {
        int n;
        int n2;
        String string;
        String string2;
        block14: {
            block13: {
                string2 = (String)object;
                string = (String)object2;
                Object var5_4 = null;
                if (string2 == null) break block13;
                if (string != null) break block14;
            }
            return "";
        }
        String string3 = "\"" + string + "\"";
        int n3 = string2.indexOf(string3);
        if (n3 < 0) {
            return "";
        }
        int n4 = string2.indexOf(58, n3 + string3.length());
        if (n4 < 0) {
            return "";
        }
        for (n2 = n4 + 1; n2 < string2.length(); ++n2) {
            if (!Character.isWhitespace(string2.charAt(n2))) break;
            if (null == null) continue;
            break;
        }
        if (n2 >= string2.length()) {
            return "";
        }
        if (string2.charAt(n2) == '\"') {
            StringBuilder stringBuilder = new StringBuilder();
            boolean bl = false;
            for (int i = n2 + 1; i < string2.length(); ++i) {
                char c = string2.charAt(i);
                if (bl) {
                    stringBuilder.append((char)(c == 'n' ? 10 : (c == 'r' ? 13 : (c == 't' ? 9 : (int)c))));
                    bl = false;
                    if (null == null) continue;
                }
                if (c == '\\') {
                    bl = true;
                    if (null == null) continue;
                }
                if (c == '\"') {
                    return stringBuilder.toString();
                }
                stringBuilder.append(c);
                if (null == null) continue;
            }
            return "";
        }
        for (n = n2; n < string2.length(); ++n) {
            char c = string2.charAt(n);
            if (c == ',') break;
            if (c == '}' || Character.isWhitespace(c)) break;
            if (null == null) continue;
        }
        return string2.substring(n2, n).trim();
    }

    private static String m956(Object object) {
        String string = (String)object;
        Object var3_2 = null;
        if (string == null) {
            return "";
        }
        return string.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    static {
        text1914 = "Start Alien through the personalized Loader.";
        text470 = "";
    }

    @Environment(value=EnvType.CLIENT)
    static final class Data  {
        private final String text14;
        private final String text15;
        private final String text16;
        private final String text17;
        private final String text18;
        private final String text19;
        private final long time;

        private Data(String string, String string2, String string3, String string4, String string5, String string6, long l) {
            this.text14 = string;
            this.text15 = string2;
            this.text16 = string3;
            this.text17 = string4;
            this.text18 = string5;
            this.text19 = string6;
            this.time = l;
        }

        public String text14() {
            return this.text14;
        }

        public String text15() {
            return this.text15;
        }

        public String getText39() {
            return this.text16;
        }

        public String text17() {
            return this.text17;
        }

        public String text18() {
            return this.text18;
        }

        public String text19() {
            return this.text19;
        }

        public long getLong3() {
            return this.time;
        }
    }
}

