/*
 * Decompiled with CFR 0.152.
 */
package shit.module.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;
import shit.data.ResourceEntry;
import shit.module.Category;
import shit.module.Module;
import shit.module.client.ClientSetting;
import shit.render.TextureRenderer2;
import shit.setting.BooleanSetting;
import shit.setting.EnumSetting;
import shit.setting.StringSetting;

@Environment(value=EnvType.CLIENT)
public class Skin
extends Module {
    public static Skin INSTANCE;
    private final StringSetting name = (StringSetting)this.m28(new StringSetting("Name", "Notch"));
    private final EnumSetting model = (EnumSetting)this.m28(new EnumSetting("Model", Model.AUTO));
    private final BooleanSetting cape = (BooleanSetting)this.m28(new BooleanSetting("Cape", true));
    private final EnumSetting capeType = (EnumSetting)this.m28(new EnumSetting("CapeType", EMode.MELON));
    private final ExecutorService executorService2 = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "Alien-SkinFetcher");
        thread.setDaemon(true);
        return thread;
    });
    private Future future;
    private ResourceEntry resourceEntry;
    private Type type10 = Type.IDLE;

    public Skin() {
        super("Skin", "Changes your local skin and cape.", Category.CLIENT);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.loadSkin();
    }

    @Override
    public void m709() {
        int[] nArray = ClientSetting.getIntArray();
        Skin skin = this;
        if (nArray != null) {
            if (skin.future != null) {
                this.future.cancel(true);
            }
            this.resourceEntry = null;
            skin = this;
        }
        skin.type10 = Type.IDLE;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public String getText57() {
        int[] nArray = ClientSetting.getIntArray();
        switch (this.type10.ordinal()) {
            default: {
                throw new MatchException(null, null);
            }
            case 0: {
                return "";
            }
            case 1: {
                return "...";
            }
            case 2: {
                ResourceEntry resourceEntry = this.resourceEntry;
                if (nArray != null) {
                    if (resourceEntry == null) return "";
                    resourceEntry = this.resourceEntry;
                }
                String string = resourceEntry.getText();
                return string;
            }
            case 3: {
                return "Failed";
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isSet92() {
        int[] nArray = ClientSetting.getIntArray();
        Skin skin = this;
        if (nArray != null) {
            if (!skin.isSet19()) return false;
            skin = this;
        }
        if (skin.resourceEntry == null) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isSet151() {
        int[] nArray = ClientSetting.getIntArray();
        if (nArray == null) return true;
        if (this.resourceEntry == null) {
            return false;
        }
        try {
            MinecraftClient.getInstance().getTextureManager().getTexture(this.resourceEntry.field9());
            return true;
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    public AssetInfo.TextureAsset getObj9() {
        int[] nArray = ClientSetting.getIntArray();
        ResourceEntry resourceEntry = this.resourceEntry;
        if (nArray != null) {
            if (resourceEntry == null) {
                return null;
            }
            resourceEntry = this.resourceEntry;
        }
        Identifier identifier = resourceEntry.field9();
        return new AssetInfo.TextureAssetInfo(identifier, identifier);
    }

    /*
     * Enabled aggressive block sorting
     */
    public PlayerSkinType getObj21() {
        PlayerSkinType playerSkinType;
        block12: {
            ResourceEntry resourceEntry;
            block11: {
                Skin skin;
                int[] nArray;
                block10: {
                    Model model;
                    Object object;
                    block9: {
                        nArray = ClientSetting.getIntArray();
                        object = this.model.getObj();
                        model = Model.SLIM;
                        if (nArray == null) break block9;
                        if (object == model) {
                            return PlayerSkinType.SLIM;
                        }
                        skin = this;
                        if (nArray == null) break block10;
                        object = skin.model.getObj();
                        model = Model.DEFAULT;
                    }
                    if (object == model) {
                        return PlayerSkinType.WIDE;
                    }
                    skin = this;
                }
                resourceEntry = skin.resourceEntry;
                if (nArray == null) break block11;
                if (resourceEntry == null) break block12;
                resourceEntry = this.resourceEntry;
            }
            if (resourceEntry.flag11()) {
                playerSkinType = PlayerSkinType.SLIM;
                return playerSkinType;
            }
        }
        playerSkinType = PlayerSkinType.WIDE;
        return playerSkinType;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isSet162() {
        int[] nArray = ClientSetting.getIntArray();
        boolean bl = this.isSet19();
        if (nArray != null) {
            if (!bl) return false;
            bl = (Boolean)this.cape.getObj();
        }
        if (nArray == null) return bl;
        if (!bl) return false;
        return true;
    }

    public AssetInfo.TextureAsset getObj10() {
        String string = ((EMode)((Object)this.capeType.getObj())).text2996;
        Identifier identifier = Identifier.of((String)"alien", (String)("cape/" + string));
        Identifier identifier2 = Identifier.of((String)"alien", (String)("cape/" + string));
        return new AssetInfo.TextureAssetInfo(identifier, identifier2);
    }

    private void loadSkin() {
        int[] nArray;
        String string;
        block8: {
            block7: {
                string = ((String)this.name.getObj()).trim();
                nArray = ClientSetting.getIntArray();
                String string2 = string;
                if (nArray != null) {
                    if (string2.isEmpty()) {
                        this.type10 = Type.FAILED;
                        return;
                    }
                    string2 = string;
                }
                ResourceEntry resourceEntry = TextureRenderer2.m481(string2);
                if (nArray == null) break block7;
                if (resourceEntry == null) break block8;
                this.resourceEntry = resourceEntry;
                this.type10 = Type.SUCCESS;
            }
            return;
        }
        Skin skin = this;
        if (nArray != null) {
            if (skin.future != null) {
                this.future.cancel(true);
            }
            this.type10 = Type.LOADING;
            skin = this;
        }
        skin.future = this.executorService2.submit(() -> {
            block2: {
                block1: {
                    int[] nArray2;
                    block0: {
                        ResourceEntry resourceEntry = TextureRenderer2.fetchSkin(string);
                        nArray2 = ClientSetting.getIntArray();
                        if (nArray2 == null) break block0;
                        if (resourceEntry == null) break block1;
                        this.resourceEntry = resourceEntry;
                        this.type10 = Type.SUCCESS;
                    }
                    if (nArray2 != null) break block2;
                }
                this.type10 = Type.FAILED;
            }
        });
    }

    @Environment(value=EnvType.CLIENT)
    public static enum EMode  {
        MELON("melon_cape.png"), VAPE("vape_cape.png"), Chicken("chicken_cape.png"), BadLion("badlion_cape.png"),
        Creeper("creeper_cape.png"), Dragon("dragon_cape.png"), Elaina("elaina_cape.png"), FDP("fdp_cape.png"),
        LUNAR_LIGHT("lunar_light_cape.png"), LUNAR_DARK("lunar_dark_cape.png"), Mojang("mojang_cape.png"),
        NovoLine("novoline_cape.png"), Sagiri("sagiri_cape.png");

        private final String text2996;

        private EMode(String text2996) {
            this.text2996 = text2996;
        }

        

        /*
         * Unable to fully structure code
         */
        
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Model {
      AUTO, DEFAULT, SLIM;

      private Model() {}



        private static Model[] getModelArray() {
            return new Model[]{AUTO, DEFAULT, SLIM};
        }
    
   }

    @Environment(value=EnvType.CLIENT)
    public static enum Type {
      IDLE, LOADING, SUCCESS, FAILED;

      private Type() {}



        private static Type[] getTypeArray7() {
            return new Type[]{IDLE, LOADING, SUCCESS, FAILED};
        }
    
   }
}

