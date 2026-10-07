/*
 * Decompiled with CFR 0.152.
 */
package shit.util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import shit.module.Module;
import shit.util.ApiEndpoints;
import shit.util.AuthUtil;
import shit.util.FileUtil;
import shit.util.HttpUtil;
import shit.util.SystemUtil;

@Environment(value=EnvType.CLIENT)
public final class ApiEndpoints3 {
    private static final DateTimeFormatter dateTimeFormatter2 = null;
    private static volatile Data2 data22;
    private static volatile boolean flag127;
    private static volatile long time63;
    private static volatile String text253;
    private static volatile long time73;
    private static volatile boolean flag76;
    private static volatile String text3180;
    private static String[] texts3;

    private ApiEndpoints3() {
    }

    public static void m217() {
        flag127 = true;
        data22 = null;
        time73 = 0L;
        time63 = 0L;
        text253 = "";
    }

    public static boolean isSet33() {
        block5: {
            block4: {
                Data2 data2 = data22;
                Object var1_1 = null;
                if (data2 == null) break block4;
                if (data2.isSet50()) break block5;
            }
            ApiEndpoints3.m217();
            return false;
        }
        if (!HttpUtil.m58("auth-confirm")) {
            data22 = new Data2(false, HttpUtil.getText28(), Instant.now());
            flag127 = true;
            time73 = 0L;
            time63 = 0L;
            text253 = "";
            return false;
        }
        flag127 = false;
        time73 = System.currentTimeMillis();
        ApiEndpoints3.m1001();
        return ApiEndpoints3.isSet90();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isSet46() {
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isSet68() {
        return true;
    }

    public static synchronized void startPeriodicRevalidation(Object object) {
        if (true) return;
        MinecraftClient minecraftClient = (MinecraftClient)object;
        Object var3_2 = null;
        if (flag76) {
            return;
        }
        flag76 = true;
        Thread thread = new Thread(() -> {
            Object var1_1 = null;
            while (!Thread.currentThread().isInterrupted()) {
                block12: {
                    Data2 data2;
                    block11: {
                        try {
                            Thread.sleep(60000L);
                        }
                        catch (InterruptedException interruptedException) {
                            if (null == null) break;
                        }
                        if (flag127) continue;
                        if (!HttpUtil.isSet44()) {
                            if (!HttpUtil.m58("revalidate")) {
                                AuthUtil.setObj59(HttpUtil.getText28());
                                if (minecraftClient == null) continue;
                                minecraftClient.execute(() -> AuthUtil.m606(minecraftClient, HttpUtil.getText28()));
                                if (null == null) continue;
                            }
                        }
                        data2 = ApiEndpoints3.m782("revalidate");
                        if (!data2.isSet50()) break block11;
                        data22 = data2;
                        time73 = System.currentTimeMillis();
                        ApiEndpoints3.m1001();
                        if (null == null) break block12;
                    }
                    String string = data2.text6();
                    if (ApiEndpoints3.m962(string)) {
                        time73 = System.currentTimeMillis();
                        ApiEndpoints3.m1001();
                        if (null == null) continue;
                    }
                    if (minecraftClient != null && minecraftClient.world != null) {
                        if (null == null) continue;
                    }
                    AuthUtil.setObj59(string.isEmpty() ? "Session verification failed." : string);
                    if (minecraftClient != null) {
                        minecraftClient.execute(() -> {
                            Object var2_2 = null;
                            AuthUtil.m606(minecraftClient, string.isEmpty() ? "Session verification failed." : string);
                        });
                    }
                }
                if (null == null) continue;
            }
        }, "Alien-Reval");
        thread.setDaemon(true);
        thread.start();
    }

    public static boolean isSet54() {
        Object var1 = null;
        return !ApiEndpoints3.isSet46();
    }

    public static String getText37() {
        Object var1 = null;
        if (!HttpUtil.isSet44()) {
            return HttpUtil.getText28();
        }
        Data2 data2 = data22;
        if (data2 == null) {
            data2 = ApiEndpoints3.getData2();
        }
        return data2.text6();
    }

    public static boolean isSet129() {
        return FileUtil.isSet20();
    }

    public static String getText5() {
        return SystemUtil.getText23();
    }

    public static String getText19() {
        return FileUtil.getText56();
    }

    public static String getText2() {
        return text3180;
    }

    public static String getText25() {
        long l = FileUtil.getLong9();
        if (l > 0L) {
            return dateTimeFormatter2.format(Instant.ofEpochSecond(l));
        }
        return "N/A";
    }

    public static synchronized Data m196(Object object) {
        String string = (String)object;
        String string2 = ApiEndpoints3.m805("start");
        Object var3_3 = null;
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = ApiEndpoints3.m59(string2);
            String string3 = ApiEndpoints3.m548("1.0.0");
            String string4 = ApiEndpoints3.m548(string);
            String string5 = ApiEndpoints3.m548(SystemUtil.getText23());
            String string6 = "{\"hwid\":\"" + string5 + "\",\"username\":\"" + string4 + "\",\"client\":\"alien\",\"version\":\"" + string3 + "\"}";
            ApiEndpoints3.m388(httpURLConnection, string6);
            int n = httpURLConnection.getResponseCode();
            String string7 = ApiEndpoints3.m985(httpURLConnection, n);
            if (n >= 200) {
                if (n < 300) {
                    if (string7.contains("\"ok\":true")) {
                        String string8 = ApiEndpoints3.m520(string7, "code");
                        String string9 = ApiEndpoints3.m520(string7, "url");
                        if (string8.isEmpty() || string9.isEmpty()) {
                            Data data = new Data(false, "", "", "Invalid web login response.");
                            return data;
                        }
                        Data data = new Data(true, string8, string9, "");
                        return data;
                    }
                }
            }
            Object object2 = ApiEndpoints3.m520(string7, "error");
            if (((String)object2).isEmpty()) {
                object2 = "Web login start failed: HTTP " + n;
            }
            Data data = new Data(false, "", "", (String)object2);
            return data;
        }
        catch (Exception exception) {
            Data data = new Data(false, "", "", "Web login failed: " + exception.getClass().getSimpleName());
            if (Module.getTextArray9() == null) {
                ApiEndpoints3.setTextArray4(new String[1]);
            }
            return data;
        }
        finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    public static synchronized Data2 m585(Object object) {
        String string;
        block21: {
            block20: {
                string = (String)object;
                Object var3_2 = null;
                if (string == null) break block20;
                if (!string.isBlank()) break block21;
            }
            return ApiEndpoints3.m809(false, "Missing web login code.");
        }
        String string2 = ApiEndpoints3.m805("poll");
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = ApiEndpoints3.m59(string2);
            String string3 = ApiEndpoints3.m548("1.0.0");
            String string4 = ApiEndpoints3.m548(SystemUtil.getText23());
            String string5 = ApiEndpoints3.m548(string);
            String string6 = "{\"code\":\"" + string5 + "\",\"hwid\":\"" + string4 + "\",\"client\":\"alien\",\"version\":\"" + string3 + "\"}";
            ApiEndpoints3.m388(httpURLConnection, string6);
            int n = httpURLConnection.getResponseCode();
            String string7 = ApiEndpoints3.m985(httpURLConnection, n);
            boolean bl = string7.contains("\"allowed\":true");
            boolean bl2 = string7.contains("\"pending\":true");
            if (bl) {
                String string8 = ApiEndpoints3.m520(string7, "token");
                String string9 = ApiEndpoints3.m520(string7, "owner");
                if (!string9.isEmpty()) {
                    text3180 = string9;
                }
                long l = ApiEndpoints3.m1028(string7);
                FileUtil.m706(string8, l);
                if (l > 0L) {
                    String string10 = dateTimeFormatter2.format(Instant.ofEpochSecond(l));
                    Data2 data2 = ApiEndpoints3.m809(true, "Web login verified. Expires: " + string10);
                    return data2;
                }
                Data2 data2 = ApiEndpoints3.m809(true, "Web login verified.");
                return data2;
            }
            if (bl2) {
                if (n >= 200) {
                    if (n < 300) {
                        Data2 data2 = ApiEndpoints3.m809(false, "Waiting for website confirmation.");
                        return data2;
                    }
                }
            }
            Object object2 = ApiEndpoints3.m520(string7, "reason");
            if (((String)object2).isEmpty()) {
                object2 = ApiEndpoints3.m520(string7, "error");
            }
            if (((String)object2).isEmpty()) {
                object2 = "Web login failed: HTTP " + n;
            }
            Data2 data2 = ApiEndpoints3.m809(false, object2);
            return data2;
        }
        catch (Exception exception) {
            Data2 data2 = ApiEndpoints3.m809(false, "NetworkError: " + exception.getClass().getSimpleName() + ": " + exception.getMessage());
            return data2;
        }
        finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    public static synchronized Data2 getData2() {
        Object var1 = null;
        if (data22 != null) {
            return data22;
        }
        String string = FileUtil.getText56();
        if (string.isEmpty()) {
            return ApiEndpoints3.m809(false, "Token required.");
        }
        HttpURLConnection httpURLConnection = null;
        try {
            int n;
            block19: {
                block18: {
                    httpURLConnection = ApiEndpoints3.m59(ApiEndpoints.getText65());
                    String string2 = ApiEndpoints3.m548("1.0.0");
                    String string3 = ApiEndpoints3.m548(ApiEndpoints3.getText58());
                    String string4 = ApiEndpoints3.m548(SystemUtil.getText23());
                    String string5 = ApiEndpoints3.m548(string);
                    String string6 = "{\"token\":\"" + string5 + "\",\"hwid\":\"" + string4 + "\",\"ip\":\"" + string3 + "\",\"client\":\"alien\",\"version\":\"" + string2 + "\",\"phase\":\"login\"}";
                    ApiEndpoints3.m388(httpURLConnection, string6);
                    n = httpURLConnection.getResponseCode();
                    if (n >= 500) break block18;
                    if (n == 429) break block18;
                    if (n != 0) break block19;
                }
                Data2 data2 = ApiEndpoints3.m809(false, "NetworkError: HTTP " + n);
                return data2;
            }
            String string7 = ApiEndpoints3.m985(httpURLConnection, n);
            boolean bl = string7.contains("\"allowed\":true");
            String string8 = ApiEndpoints3.m520(string7, "reason");
            if (bl) {
                String string9 = ApiEndpoints3.m520(string7, "owner");
                if (!string9.isEmpty()) {
                    text3180 = string9;
                }
                long l = ApiEndpoints3.m1028(string7);
                if (l > 0L) {
                    FileUtil.m706(string, l);
                    String string10 = dateTimeFormatter2.format(Instant.ofEpochSecond(l));
                    Data2 data2 = ApiEndpoints3.m809(true, "Verified. Expires: " + string10);
                    return data2;
                }
                Data2 data2 = ApiEndpoints3.m809(true, "Verified.");
                return data2;
            }
            if (string8.isEmpty()) {
                string8 = "Token is invalid or expired.";
            }
            Data2 data2 = ApiEndpoints3.m809(false, string8);
            return data2;
        }
        catch (Exception exception) {
            Data2 data2 = ApiEndpoints3.m809(false, "NetworkError: " + exception.getClass().getSimpleName() + ": " + exception.getMessage());
            return data2;
        }
        finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    public static synchronized Data2 getData22() {
        Data2 data2;
        Data2 data22 = ApiEndpoints3.m782("postLogin");
        Object var1_1 = null;
        if (!data22.isSet50()) {
            ApiEndpoints3.data22 = data22;
            flag127 = true;
            time73 = 0L;
            time63 = 0L;
            text253 = "";
            return data22;
        }
        ApiEndpoints3.data22 = data22;
        if (ApiEndpoints3.isSet33()) {
            return data22;
        }
        ApiEndpoints3.data22 = data2 = new Data2(false, "Session seal check failed.", Instant.now());
        flag127 = true;
        time73 = 0L;
        time63 = 0L;
        text253 = "";
        return data2;
    }

    private static synchronized Data2 m782(Object object) {
        String string = (String)object;
        String string2 = FileUtil.getText56();
        Object var3_3 = null;
        if (string2.isEmpty()) {
            return new Data2(false, "Token required.", Instant.now());
        }
        HttpURLConnection httpURLConnection = null;
        try {
            int n;
            block18: {
                block17: {
                    httpURLConnection = ApiEndpoints3.m59(ApiEndpoints.getText65());
                    String string3 = ApiEndpoints3.m548(string);
                    String string4 = ApiEndpoints3.m548("1.0.0");
                    String string5 = ApiEndpoints3.m548(ApiEndpoints3.getText58());
                    String string6 = ApiEndpoints3.m548(SystemUtil.getText23());
                    String string7 = ApiEndpoints3.m548(string2);
                    String string8 = "{\"token\":\"" + string7 + "\",\"hwid\":\"" + string6 + "\",\"ip\":\"" + string5 + "\",\"client\":\"alien\",\"version\":\"" + string4 + "\",\"phase\":\"" + string3 + "\"}";
                    ApiEndpoints3.m388(httpURLConnection, string8);
                    n = httpURLConnection.getResponseCode();
                    if (n >= 500) break block17;
                    if (n != 429 && n != 0) break block18;
                }
                Data2 data2 = new Data2(false, "NetworkError: HTTP " + n, Instant.now());
                return data2;
            }
            String string9 = ApiEndpoints3.m985(httpURLConnection, n);
            boolean bl = string9.contains("\"allowed\":true");
            String string10 = ApiEndpoints3.m520(string9, "reason");
            if (bl) {
                String string11 = ApiEndpoints3.m520(string9, "owner");
                if (!string11.isEmpty()) {
                    text3180 = string11;
                }
                long l = ApiEndpoints3.m1028(string9);
                if (l > 0L) {
                    FileUtil.m706(string2, l);
                    String string12 = dateTimeFormatter2.format(Instant.ofEpochSecond(l));
                    Data2 data2 = new Data2(true, "Verified. Expires: " + string12, Instant.now());
                    return data2;
                }
                Data2 data2 = new Data2(true, "Verified.", Instant.now());
                return data2;
            }
            if (string10.isEmpty()) {
                string10 = "Token is invalid or expired.";
            }
            Data2 data2 = new Data2(false, string10, Instant.now());
            return data2;
        }
        catch (Exception exception) {
            Data2 data2 = new Data2(false, "NetworkError: " + exception.getClass().getSimpleName() + ": " + exception.getMessage(), Instant.now());
            return data2;
        }
        finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    public static boolean isSet90() {
        String string;
        long l;
        block3: {
            block2: {
                l = time63;
                string = text253;
                Data2 data2 = data22;
                Object var1_3 = null;
                if (l == 0L) break block2;
                if (string == null) break block2;
                if (string.length() < 48) break block2;
                if (data2 == null) break block2;
                if (data2.isSet50()) break block3;
            }
            return false;
        }
        return ApiEndpoints3.m1046(string, ApiEndpoints3.m871(l, time73));
    }

    private static void m1001() {
        long l;
        block3: {
            block2: {
                Object var1 = null;
                if (data22 == null) break block2;
                if (!data22.isSet50()) break block2;
                if (time73 != 0L) break block3;
            }
            time63 = 0L;
            text253 = "";
            return;
        }
        time63 = l = ApiEndpoints3.getLong5();
        text253 = ApiEndpoints3.m871(l, time73);
    }

    private static String m871(long l, long l2) {
        try {
            long l3 = l;
            long l4 = l2;
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            ApiEndpoints3.m680(messageDigest, "Alien-Session-Seal-v1");
            ApiEndpoints3.m680(messageDigest, SystemUtil.getText23());
            ApiEndpoints3.m680(messageDigest, SystemUtil.getText3());
            ApiEndpoints3.m680(messageDigest, "1.0.0");
            ApiEndpoints3.m680(messageDigest, String.valueOf(FileUtil.getLong9()));
            ApiEndpoints3.m680(messageDigest, text3180);
            ApiEndpoints3.m680(messageDigest, String.valueOf(l3));
            ApiEndpoints3.m680(messageDigest, String.valueOf(l4));
            return HexFormat.of().formatHex(messageDigest.digest());
        }
        catch (Throwable throwable) {
            return "";
        }
    }

    private static long getLong5() {
        long l = new SecureRandom().nextLong();
        Object var1_1 = null;
        if (l == 0L) {
            l = System.nanoTime() ^ System.currentTimeMillis();
        }
        return l;
    }

    private static void m680(Object object, Object object2) {
        MessageDigest messageDigest = (MessageDigest)object;
        String string = (String)object2;
        Object var5_4 = null;
        if (string != null) {
            messageDigest.update(string.getBytes(StandardCharsets.UTF_8));
        }
        messageDigest.update((byte)90);
    }

    private static boolean m1046(Object object, Object object2) {
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
        byte[] byArray = string2.getBytes(StandardCharsets.UTF_8);
        byte[] byArray2 = string.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(byArray, byArray2);
    }

    static String getText58() {
        String[] stringArray;
        String[] stringArray2 = stringArray = new String[]{"https://api.ipify.org", "https://ifconfig.me/ip", "https://icanhazip.com"};
        Object var1_2 = null;
        for (String string : stringArray2) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection)URI.create(string).toURL().openConnection();
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setReadTimeout(3000);
                httpURLConnection.setRequestMethod("GET");
                if (httpURLConnection.getResponseCode() == 200) {
                    byte[] byArray = httpURLConnection.getInputStream().readAllBytes();
                    httpURLConnection.disconnect();
                    return new String(byArray, StandardCharsets.UTF_8).trim();
                }
                httpURLConnection.disconnect();
                continue;
            }
            catch (Exception exception) {}
            if (null == null) continue;
            Module.setTextArray9(new String[4]);
            break;
        }
        return "unknown";
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static boolean m962(Object object) {
        String string = (String)object;
        Object var3_2 = null;
        if (string == null) return false;
        if (string.isEmpty()) {
            return false;
        }
        if (string.startsWith("NetworkError")) return true;
        if (string.contains("SocketTimeout")) return true;
        if (string.contains("UnknownHost")) return true;
        if (string.contains("ConnectException")) return true;
        if (string.contains("SSLException")) return true;
        if (string.contains("SocketException")) return true;
        if (string.contains("HTTP 5")) return true;
        if (string.contains("HTTP 429")) return true;
        if (!string.contains("HTTP 0")) return false;
        return true;
    }

    private static Data2 m809(boolean bl, Object object) {
        Data2 data2;
        boolean bl2 = bl;
        String string = (String)object;
        data22 = data2 = new Data2(bl2, string == null ? "" : string, Instant.now());
        return data2;
    }

    private static HttpURLConnection m59(Object object) {
        try {
            String string = (String)object;
            HttpURLConnection httpURLConnection = (HttpURLConnection)URI.create(string).toURL().openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            return httpURLConnection;
        }
        catch (java.io.IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void m388(Object object, Object object2) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection)object;
            String string = (String)object2;
            byte[] byArray = string.getBytes(StandardCharsets.UTF_8);
            httpURLConnection.setFixedLengthStreamingMode(byArray.length);
            Object var5_6 = null;
            try (OutputStream outputStream = httpURLConnection.getOutputStream();){
                outputStream.write(byArray);
            }
        }
        catch (java.io.IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static String m805(Object object) {
        String string = (String)object;
        String string2 = ApiEndpoints.getText65();
        int n = string2.indexOf(63);
        String string3 = n >= 0 ? string2.substring(0, n) : string2;
        return string3 + "?route=/web-login/" + string;
    }

    private static long m1028(Object object) {
        String string = (String)object;
        String string2 = ApiEndpoints3.m520(string, "expiresAt");
        Object var3_3 = null;
        if (!string2.isEmpty()) {
            try {
                return Long.parseLong(string2);
            }
            catch (NumberFormatException numberFormatException) {}
        }
        return 0L;
    }

    private static String m985(Object object, int n) {
        HttpURLConnection httpURLConnection = (HttpURLConnection)object;
        int n2 = n;
        Object var5_4 = null;
        try {
            byte[] byArray;
            block5: {
                block6: {
                    block4: {
                        if (n2 < 400) break block4;
                        if (httpURLConnection.getErrorStream() == null) break block4;
                        byArray = httpURLConnection.getErrorStream().readAllBytes();
                        if (null == null) break block5;
                    }
                    if (httpURLConnection.getInputStream() == null) break block6;
                    byArray = httpURLConnection.getInputStream().readAllBytes();
                    if (null == null) break block5;
                }
                return "";
            }
            return new String(byArray, StandardCharsets.UTF_8);
        }
        catch (Exception exception) {
            return "";
        }
    }

    private static String m520(Object object, Object object2) {
        String string;
        String string2;
        block10: {
            block9: {
                string2 = (String)object;
                string = (String)object2;
                Object var5_4 = null;
                if (string2 == null) break block9;
                if (string != null) break block10;
            }
            return "";
        }
        String string3 = "\"" + string + "\"";
        int n = string2.indexOf(string3);
        if (n < 0) {
            return "";
        }
        int n2 = string2.indexOf(58, n + string3.length());
        if (n2 < 0) {
            return "";
        }
        int n3 = string2.indexOf(34, n2 + 1);
        if (n3 < 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl = false;
        for (int i = n3 + 1; i < string2.length(); ++i) {
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
            if (c == '\"' && null == null) break;
            stringBuilder.append(c);
            if (null == null) continue;
        }
        return stringBuilder.toString();
    }

    private static String m548(Object object) {
        String string = (String)object;
        Object var3_2 = null;
        if (string == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(string.length() + 16);
        block7: for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            switch (c) {
                case '\\': {
                    stringBuilder.append("\\\\");
                    if (null == null) continue block7;
                }
                case '\"': {
                    stringBuilder.append("\\\"");
                    if (null == null) continue block7;
                }
                case '\n': {
                    stringBuilder.append("\\n");
                    if (null == null) continue block7;
                }
                case '\r': {
                    stringBuilder.append("\\r");
                    if (null == null) continue block7;
                }
                case '\t': {
                    stringBuilder.append("\\t");
                    if (null == null) continue block7;
                }
                default: {
                    stringBuilder.append(c);
                    continue block7;
                }
            }
        }
        return stringBuilder.toString();
    }

    /*
     * Unable to fully structure code
     */
    static {}

    public static void setTextArray4(String[] stringArray) {
        texts3 = stringArray;
    }

    public static String[] getTextArray5() {
        return texts3;
    }

    @Environment(value=EnvType.CLIENT)
    public record Data(boolean flag2, String text3, String text4, String text5) {
    }

    @Environment(value=EnvType.CLIENT)
    public static final class Data2  {
        private final boolean flag3;
        private final String text6;
        private final Instant instant;

        public Data2(boolean bl, String string, Instant instant) {
            this.flag3 = bl;
            this.text6 = string;
            this.instant = instant;
        }

        public boolean isSet50() {
            return this.flag3;
        }

        public String text6() {
            return this.text6;
        }

        public Instant instant() {
            return this.instant;
        }
    }
}

