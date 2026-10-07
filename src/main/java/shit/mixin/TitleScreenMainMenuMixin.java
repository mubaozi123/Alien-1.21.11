/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.Client;
import shit.manager.FontManager2;
import shit.manager.ShaderProgramManager;
import shit.misc.RenderUtil2;
import shit.mixin.ScreenAccessor;
import shit.module.client.MainMenu;

@Environment(value=EnvType.CLIENT)
@Mixin(value={TitleScreen.class})
public class TitleScreenMainMenuMixin {
    @Unique
    private final List<shit.misc.RenderUtil2> alien$buttons = new ArrayList<>();

    @Inject(method={"init()V"}, at={@At(value="TAIL")})
    private void alien$onInit(CallbackInfo callbackInfo) {
        MainMenu mainMenu = MainMenu.INSTANCE;
        if (mainMenu != null) {
            mainMenu.m590();
        }
        if (mainMenu == null || !mainMenu.isSet110()) {
            return;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        this.alien$buttons.clear();
        ((ScreenAccessor)((Object)this)).alien$clearWidgets();
        TitleScreen titleScreen = (TitleScreen)(Object)this;
        this.alien$buttons.add(new RenderUtil2("Singleplayer", 30.0f, () -> minecraftClient.setScreen((Screen)new SelectWorldScreen((Screen)titleScreen))));
        this.alien$buttons.add(new RenderUtil2("Multiplayer", 30.0f, () -> minecraftClient.setScreen((Screen)new MultiplayerScreen((Screen)titleScreen))));
        this.alien$buttons.add(new RenderUtil2("Options", 30.0f, () -> minecraftClient.setScreen((Screen)new OptionsScreen((Screen)titleScreen, minecraftClient.options))));
        this.alien$buttons.add(new RenderUtil2("Exit", 30.0f, () -> ((MinecraftClient)minecraftClient).scheduleStop()));
        this.alien$layoutButtons(minecraftClient);
        this.setClient(minecraftClient);
        ScreenMouseEvents.afterMouseClick((Screen)titleScreen).register((screen, click, bl) -> {
            if (!mainMenu.isSet110()) {
                return bl;
            }
            for (RenderUtil2 renderUtil2 : this.alien$buttons) {
                renderUtil2.m421(click.x(), click.y(), click.button());
            }
            return bl;
        });
        ScreenMouseEvents.afterMouseRelease((Screen)titleScreen).register((screen, click, bl) -> {
            if (!mainMenu.isSet110()) {
                return bl;
            }
            for (RenderUtil2 renderUtil2 : this.alien$buttons) {
                renderUtil2.m946(click.x(), click.y(), click.button());
            }
            return bl;
        });
    }

    @Unique
    private void setClient(MinecraftClient minecraftClient) {
        float f = 0.0f;
        if (Client.fontManager.isSet89()) {
            for (RenderUtil2 renderUtil2 : this.alien$buttons) {
                float f2 = minecraftClient.textRenderer.getWidth(renderUtil2.getText71());
                if (!(f2 > f)) continue;
                f = f2;
            }
        } else {
            FontManager2 fontManager2 = Client.fontManager.renderer2();
            for (RenderUtil2 renderUtil2 : this.alien$buttons) {
                float f3 = fontManager2.m277(renderUtil2.getText71());
                if (!(f3 > f)) continue;
                f = f3;
            }
        }
        RenderUtil2.value172 = f + 12.0f;
    }

    @Unique
    private void alien$layoutButtons(MinecraftClient minecraftClient) {
        int n = minecraftClient.getWindow().getFramebufferWidth();
        int n2 = minecraftClient.getWindow().getFramebufferHeight();
        float f = Math.min((float)n / 6.0f, 300.0f);
        float f2 = Math.min((float)(n + n2 * 2) / 25.0f, 150.0f);
        double d = minecraftClient.getWindow().getScaleFactor();
        float f3 = (float)((double)n / d);
        float f4 = (float)((double)n2 / d);
        float f5 = (float)((double)f / d);
        float f6 = (float)(10.0 / d);
        float f7 = (float)((double)f2 / d);
        int n3 = this.alien$buttons.size();
        float f8 = (float)n3 * f5 + (float)(n3 - 1) * f6;
        float f9 = (f3 - f8) / 2.0f;
        float f10 = f4 - f7;
        for (int i = 0; i < n3; ++i) {
            ((RenderUtil2)(Object)this.alien$buttons.get(i)).m803(f9 + (float)i * (f5 + f6), f10, f5);
        }
    }

    @Redirect(method={"render(Lnet/minecraft/client/gui/DrawContext;IIF)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/screen/TitleScreen;renderPanoramaBackground(Lnet/minecraft/client/gui/DrawContext;F)V"))
    private void m903(TitleScreen titleScreen, DrawContext drawContext, float f) {
        MainMenu mainMenu = MainMenu.INSTANCE;
        if (mainMenu == null || !mainMenu.isSet9()) {
            ((ScreenAccessor)titleScreen).alien$renderPanorama(drawContext, f);
            return;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        try {
            ShaderProgramManager.render4((Object)mainMenu.getObj8(), minecraftClient.getWindow().getFramebufferWidth(), minecraftClient.getWindow().getFramebufferHeight(), (double)drawContext.getScaledWindowWidth() / 2.0, (double)drawContext.getScaledWindowHeight() / 2.0);
        }
        catch (Throwable throwable) {
            ((ScreenAccessor)titleScreen).alien$renderPanorama(drawContext, f);
        }
    }

    @Redirect(method={"render(Lnet/minecraft/client/gui/DrawContext;IIF)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/LogoDrawer;draw(Lnet/minecraft/client/gui/DrawContext;IF)V"))
    private void alien$suppressLogo(LogoDrawer logoDrawer, DrawContext drawContext, int n, float f) {
        MainMenu mainMenu = MainMenu.INSTANCE;
        if (mainMenu == null || !mainMenu.isSet110()) {
            logoDrawer.draw(drawContext, n, f);
        }
    }

    @Redirect(method={"render(Lnet/minecraft/client/gui/DrawContext;IIF)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/screen/SplashTextRenderer;render(Lnet/minecraft/client/gui/DrawContext;ILnet/minecraft/client/font/TextRenderer;F)V"))
    private void alien$suppressSplash(SplashTextRenderer splashTextRenderer, DrawContext drawContext, int n, TextRenderer textRenderer, float f) {
        MainMenu mainMenu = MainMenu.INSTANCE;
        if (mainMenu == null || !mainMenu.isSet110()) {
            splashTextRenderer.render(drawContext, n, textRenderer, f);
        }
    }

    @Inject(method={"render(Lnet/minecraft/client/gui/DrawContext;IIF)V"}, at={@At(value="TAIL")})
    private void alien$renderButtons(DrawContext drawContext, int n, int n2, float f, CallbackInfo callbackInfo) {
        MainMenu mainMenu = MainMenu.INSTANCE;
        if (mainMenu == null || !mainMenu.isSet110()) {
            return;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        int n3 = minecraftClient.getWindow().getFramebufferWidth();
        minecraftClient.getWindow().getFramebufferHeight();
        double d = minecraftClient.getWindow().getScaleFactor();
        float f2 = (float)((double)n3 / 15.0 / d);
        float f3 = f2 / 2.0f;
        String string = mainMenu.getText45();
        if (Client.fontManager.isSet89()) {
            drawContext.drawText(minecraftClient.textRenderer, string, Math.round(f2), Math.round(f3), -1, true);
        } else {
            FontManager2 fontManager2 = Client.fontManager.renderer(28.0f);
            int n4 = Math.round(Client.fontManager.getFloat47());
            fontManager2.m5(drawContext, string, Math.round(f2), Math.round(f3) + n4, -1, true);
        }
        for (RenderUtil2 renderUtil2 : this.alien$buttons) {
            renderUtil2.m194(n, n2);
            renderUtil2.setObj113(drawContext);
        }
    }
}

