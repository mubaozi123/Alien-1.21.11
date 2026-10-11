/*
 * AutoCrystal - ported to the Alien (shit.*) codebase, Minecraft 1.21.11 rendering API.
 *
 * Dependencies that do not exist in this codebase were removed because they do not
 * affect the core place/break/calculation functionality:
 *  - RotationEvent / global rotation manager -> replaced with Client.mathUtil direct rotation
 *  - PlayerEntityPredict / simulation        -> targets are evaluated at their current position
 *  - Blink / ElytraFly / Velocity            -> cross-module pause checks dropped
 *  - AutoAnchor / AutoWeb / PacketMine       -> anchor-pause, web-reset, mining-override dropped
 *  - JelloUtil / ThunderHack ESP             -> target ESP limited to Box / Fill / None
 *  - thread mode                             -> dropped (calc runs on tick)
 */
package shit.module.combat;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Matrix4f;
import shit.Client;
import shit.event.EventHandler;
import shit.event.Event2;
import shit.event.PacketEvent;
import shit.event.RenderLevelEvent;
import shit.misc.Helper7;
import shit.misc.MathUtil;
import shit.module.Category;
import shit.module.Module;
import shit.render.EspRenderLayers;
import shit.setting.BooleanSetting;
import shit.setting.ColorSetting;
import shit.setting.EnumSetting;
import shit.setting.NumberSetting;
import shit.util.BlockUtil;
import shit.util.ItemUtil;
import shit.util.MC;
import shit.util.RenderUtil3;

@Environment(value=EnvType.CLIENT)
public class AutoCrystal
extends Module {
    public static AutoCrystal INSTANCE;

    public static AutoCrystal getInstance() {
        return INSTANCE;
    }
    public BlockPos crystalPos;
    public PlayerEntity displayTarget;
    public float breakDamage;
    public float tempDamage;
    public float lastDamage;
    double currentFade = 0.0;
    private EndCrystalEntity tempBreakCrystal;
    private EndCrystalEntity breakCrystal;
    private BlockPos tempPos;
    private BlockPos syncPos;
    private Vec3d placeVec3d;
    private Vec3d curVec3d;
    int lastSlot;
    BlockPos tempBasePos;
    BlockPos basePos;
    private final Helper7 lastBreakTimer = new Helper7();
    private final Helper7 baseTimer = new Helper7();
    private final Helper7 placeTimer = new Helper7();
    private final Helper7 noPosTimer = new Helper7();
    private final Helper7 switchTimer = new Helper7();
    private final Helper7 calcDelay = new Helper7();
    private final Helper7 syncTimer = new Helper7();
    private long lastBreakMs;
    private final DecimalFormat df = new DecimalFormat("0.0");

    private final EnumSetting page = (EnumSetting)this.m28(new EnumSetting("Page", Page.General));
    private final BooleanSetting breakOnlyHasCrystal = (BooleanSetting)this.m28(new BooleanSetting("OnlyHold", true, () -> this.page.getObj() == Page.Check, null, "", false));
    private final BooleanSetting eatingPause = (BooleanSetting)this.m28(new BooleanSetting("EatingPause", true, () -> this.page.getObj() == Page.Check, null, "", false));
    private final NumberSetting switchCooldown = (NumberSetting)this.m28(new NumberSetting("SwitchPause", 100.0, 0.0, 1000.0, 1.0, 1.0, () -> this.page.getObj() == Page.Check, null, "", false));
    private final NumberSetting targetRange = (NumberSetting)this.m28(new NumberSetting("TargetRange", 12.0, 0.0, 20.0, 1.0, 1.0, () -> this.page.getObj() == Page.Check, null, "", false));
    private final NumberSetting updateDelay = (NumberSetting)this.m28(new NumberSetting("UpdateDelay", 50.0, 0.0, 1000.0, 1.0, 1.0, () -> this.page.getObj() == Page.Check, null, "", false));

    private final BooleanSetting rotate = (BooleanSetting)this.m28(new BooleanSetting("Rotate", true, () -> this.page.getObj() == Page.Rotation, null, "", false));
    private final BooleanSetting onPlace = (BooleanSetting)this.m28(new BooleanSetting("OnPlace", false, () -> this.page.getObj() == Page.Rotation && ((Boolean)this.rotate.getObj()).booleanValue(), null, "", false));
    private final BooleanSetting onBreak = (BooleanSetting)this.m28(new BooleanSetting("OnBreak", false, () -> this.page.getObj() == Page.Rotation && ((Boolean)this.rotate.getObj()).booleanValue(), null, "", false));

    private final NumberSetting minDamage = (NumberSetting)this.m28(new NumberSetting("Min", 5.0, 0.0, 36.0, 1.0, 1.0, () -> this.page.getObj() == Page.General, null, "", false));
    private final NumberSetting maxSelf = (NumberSetting)this.m28(new NumberSetting("Max", 12.0, 0.0, 36.0, 1.0, 1.0, () -> this.page.getObj() == Page.General, null, "", false));
    private final NumberSetting reserve = (NumberSetting)this.m28(new NumberSetting("Reserve", 2.0, 0.0, 10.0, 1.0, 1.0, () -> this.page.getObj() == Page.General, null, "", false));
    private final BooleanSetting balance = (BooleanSetting)this.m28(new BooleanSetting("Balance", true, () -> this.page.getObj() == Page.General, null, "", false));
    private final NumberSetting balanceOffset = (NumberSetting)this.m28(new NumberSetting("BalanceOffset", 0.0, -20.0, 20.0, 0.1, 0.1, () -> this.page.getObj() == Page.General && ((Boolean)this.balance.getObj()).booleanValue(), null, "", false));
    private final BooleanSetting place = (BooleanSetting)this.m28(new BooleanSetting("Place", true, () -> this.page.getObj() == Page.General, null, "", false));
    public final NumberSetting placeRange = (NumberSetting)this.m28(new NumberSetting("PlaceRange", 5.0, 0.0, 6.0, 0.01, 0.01, () -> this.page.getObj() == Page.General && ((Boolean)this.place.getObj()).booleanValue(), null, "", false));
    private final NumberSetting placeDelay = (NumberSetting)this.m28(new NumberSetting("PlaceDelay", 300.0, 0.0, 1000.0, 1.0, 1.0, () -> this.page.getObj() == Page.General && ((Boolean)this.place.getObj()).booleanValue(), null, "", false));
    private final EnumSetting autoSwap = (EnumSetting)this.m28(new EnumSetting("AutoSwap", SwapMode.None, () -> this.page.getObj() == Page.General && ((Boolean)this.place.getObj()).booleanValue(), null, "", false));
    private final BooleanSetting afterBreak = (BooleanSetting)this.m28(new BooleanSetting("AfterBreak", true, () -> this.page.getObj() == Page.General && ((Boolean)this.place.getObj()).booleanValue(), null, "", false));
    private final BooleanSetting forcePlace = (BooleanSetting)this.m28(new BooleanSetting("ForcePlace", false, () -> this.page.getObj() == Page.General && ((Boolean)this.place.getObj()).booleanValue(), null, "", false));
    private final BooleanSetting breakSetting = (BooleanSetting)this.m28(new BooleanSetting("Break", true, () -> this.page.getObj() == Page.General, null, "", false));
    public final NumberSetting breakRange = (NumberSetting)this.m28(new NumberSetting("BreakRange", 4.0, 0.0, 6.0, 0.01, 0.01, () -> this.page.getObj() == Page.General && ((Boolean)this.breakSetting.getObj()).booleanValue(), null, "", false));
    private final NumberSetting breakDelay = (NumberSetting)this.m28(new NumberSetting("BreakDelay", 300.0, 0.0, 1000.0, 1.0, 1.0, () -> this.page.getObj() == Page.General && ((Boolean)this.breakSetting.getObj()).booleanValue(), null, "", false));
    private final NumberSetting minAge = (NumberSetting)this.m28(new NumberSetting("MinAge", 0.0, 0.0, 20.0, 1.0, 1.0, () -> this.page.getObj() == Page.General && ((Boolean)this.breakSetting.getObj()).booleanValue(), null, "", false));
    private final BooleanSetting breakRemove = (BooleanSetting)this.m28(new BooleanSetting("Remove", false, () -> this.page.getObj() == Page.General && ((Boolean)this.breakSetting.getObj()).booleanValue(), null, "", false));
    private final BooleanSetting resetCD = (BooleanSetting)this.m28(new BooleanSetting("ResetAttack", true, () -> this.page.getObj() == Page.General && ((Boolean)this.breakSetting.getObj()).booleanValue(), null, "", false));
    private final NumberSetting wallRange = (NumberSetting)this.m28(new NumberSetting("WallRange", 6.0, 0.0, 6.0, 1.0, 1.0, () -> this.page.getObj() == Page.General, null, "", false));

    private final EnumSetting mode = (EnumSetting)this.m28(new EnumSetting("TargetESP", TargetESP.Fill, () -> this.page.getObj() == Page.Render, null, "", false));
    private final ColorSetting color = (ColorSetting)this.m28(new ColorSetting("TargetColor", 0x32FFFFFF, () -> this.page.getObj() == Page.Render, null, "", false));
    private final ColorSetting outlineColor = (ColorSetting)this.m28(new ColorSetting("TargetOutlineColor", 0x32FFFFFF, () -> this.page.getObj() == Page.Render, null, "", false));
    private final ColorSetting hitColor = (ColorSetting)this.m28(new ColorSetting("HitColor", 0x96FFFFFF, () -> this.page.getObj() == Page.Render, null, "", false));
    private final ColorSetting hitOutlineColor = (ColorSetting)this.m28(new ColorSetting("HitOutlineColor", 0x96FFFFFF, () -> this.page.getObj() == Page.Render, null, "", false));
    private final BooleanSetting render = (BooleanSetting)this.m28(new BooleanSetting("Render", true, () -> this.page.getObj() == Page.Render, null, "", false));
    private final BooleanSetting sync = (BooleanSetting)this.m28(new BooleanSetting("Sync", true, () -> this.page.getObj() == Page.Render && ((Boolean)this.render.getObj()).booleanValue(), null, "", false));
    private final BooleanSetting shrink = (BooleanSetting)this.m28(new BooleanSetting("Shrink", true, () -> this.page.getObj() == Page.Render && ((Boolean)this.render.getObj()).booleanValue(), null, "", false));
    private final ColorSetting box = (ColorSetting)this.m28(new ColorSetting("Box", -1, true, () -> this.page.getObj() == Page.Render && ((Boolean)this.render.getObj()).booleanValue(), null, "", false));
    private final ColorSetting fill = (ColorSetting)this.m28(new ColorSetting("Fill", 0x64FFFFFF, true, () -> this.page.getObj() == Page.Render && ((Boolean)this.render.getObj()).booleanValue(), null, "", false));
    private final NumberSetting sliderSpeed = (NumberSetting)this.m28(new NumberSetting("SliderSpeed", 0.2, 0.01, 1.0, 0.01, 0.01, () -> this.page.getObj() == Page.Render && ((Boolean)this.render.getObj()).booleanValue(), null, "", false));
    private final NumberSetting startFadeTime = (NumberSetting)this.m28(new NumberSetting("StartFade", 0.3, 0.0, 2.0, 0.01, 0.01, () -> this.page.getObj() == Page.Render && ((Boolean)this.render.getObj()).booleanValue(), null, "", false));
    private final NumberSetting fadeSpeed = (NumberSetting)this.m28(new NumberSetting("FadeSpeed", 0.2, 0.01, 1.0, 0.01, 0.01, () -> this.page.getObj() == Page.Render && ((Boolean)this.render.getObj()).booleanValue(), null, "", false));

    private final BooleanSetting terrainIgnore = (BooleanSetting)this.m28(new BooleanSetting("TerrainIgnore", true, () -> this.page.getObj() == Page.Calc, null, "", false));
    private final NumberSetting attackVecStep = (NumberSetting)this.m28(new NumberSetting("AttackVecStep", 0.1, 0.01, 1.0, 0.01, 0.01, () -> this.page.getObj() == Page.Calc, null, "", false));

    private final BooleanSetting basePlace = (BooleanSetting)this.m28(new BooleanSetting("BasePlace", true, () -> this.page.getObj() == Page.Base, null, "", false));
    private final NumberSetting baseMin = (NumberSetting)this.m28(new NumberSetting("BaseMin", 6.0, 0.0, 36.0, 0.1, 0.1, () -> this.page.getObj() == Page.Base, null, "", false));
    private final NumberSetting baseMax = (NumberSetting)this.m28(new NumberSetting("BaseMax", 12.0, 0.0, 36.0, 0.1, 0.1, () -> this.page.getObj() == Page.Base, null, "", false));
    private final NumberSetting overrideMax = (NumberSetting)this.m28(new NumberSetting("MaxOverride", 8.0, 0.0, 36.0, 0.1, 0.1, () -> this.page.getObj() == Page.Base, null, "", false));
    private final BooleanSetting baseBalance = (BooleanSetting)this.m28(new BooleanSetting("BaseBalance", true, () -> this.page.getObj() == Page.Base, null, "", false));
    private final BooleanSetting onlyBelow = (BooleanSetting)this.m28(new BooleanSetting("OnlyBelow", true, () -> this.page.getObj() == Page.Base, null, "", false));
    private final BooleanSetting inventory = (BooleanSetting)this.m28(new BooleanSetting("InventorySwap", true, () -> this.page.getObj() == Page.Base, null, "", false));
    private final NumberSetting delay = (NumberSetting)this.m28(new NumberSetting("Delay", 3000.0, 0.0, 10000.0, 1.0, 1.0, () -> this.page.getObj() == Page.Base, null, "", false));

    private final BooleanSetting antiSurround = (BooleanSetting)this.m28(new BooleanSetting("AntiSurround", false, () -> this.page.getObj() == Page.Misc, null, "", false));
    private final NumberSetting antiSurroundMax = (NumberSetting)this.m28(new NumberSetting("WhenLower", 5.0, 0.0, 36.0, 1.0, 1.0, () -> this.page.getObj() == Page.Misc && ((Boolean)this.antiSurround.getObj()).booleanValue(), null, "", false));
    private final BooleanSetting slowPlace = (BooleanSetting)this.m28(new BooleanSetting("Timeout", true, () -> this.page.getObj() == Page.Misc, null, "", false));
    private final NumberSetting slowDelay = (NumberSetting)this.m28(new NumberSetting("TimeoutDelay", 600.0, 0.0, 2000.0, 1.0, 1.0, () -> this.page.getObj() == Page.Misc && ((Boolean)this.slowPlace.getObj()).booleanValue(), null, "", false));
    private final NumberSetting slowMinDamage = (NumberSetting)this.m28(new NumberSetting("TimeoutMin", 1.5, 0.0, 36.0, 1.0, 1.0, () -> this.page.getObj() == Page.Misc && ((Boolean)this.slowPlace.getObj()).booleanValue(), null, "", false));
    private final BooleanSetting lethalOverride = (BooleanSetting)this.m28(new BooleanSetting("LethalOverride", true, () -> this.page.getObj() == Page.Misc, null, "", false));
    private final NumberSetting forceMaxHealth = (NumberSetting)this.m28(new NumberSetting("LowerThan", 7.0, 0.0, 36.0, 0.1, 0.1, () -> this.page.getObj() == Page.Misc && ((Boolean)this.lethalOverride.getObj()).booleanValue(), null, "", false));
    private final NumberSetting forceMin = (NumberSetting)this.m28(new NumberSetting("ForceMin", 1.5, 0.0, 36.0, 1.0, 1.0, () -> this.page.getObj() == Page.Misc && ((Boolean)this.lethalOverride.getObj()).booleanValue(), null, "", false));
    private final BooleanSetting armorBreaker = (BooleanSetting)this.m28(new BooleanSetting("ArmorBreaker", true, () -> this.page.getObj() == Page.Misc, null, "", false));
    private final NumberSetting maxDurable = (NumberSetting)this.m28(new NumberSetting("MaxDurable", 8.0, 0.0, 100.0, 1.0, 1.0, () -> this.page.getObj() == Page.Misc && ((Boolean)this.armorBreaker.getObj()).booleanValue(), null, "", false));
    private final NumberSetting armorBreakerDamage = (NumberSetting)this.m28(new NumberSetting("BreakerMin", 3.0, 0.0, 36.0, 1.0, 1.0, () -> this.page.getObj() == Page.Misc && ((Boolean)this.armorBreaker.getObj()).booleanValue(), null, "", false));
    private final NumberSetting hurtTime = (NumberSetting)this.m28(new NumberSetting("HurtTime", 10.0, 0.0, 10.0, 1.0, 1.0, () -> this.page.getObj() == Page.Misc, null, "", false));
    private final NumberSetting waitHurt = (NumberSetting)this.m28(new NumberSetting("WaitHurt", 10.0, 0.0, 10.0, 1.0, 1.0, () -> this.page.getObj() == Page.Misc, null, "", false));
    private final NumberSetting syncTimeout = (NumberSetting)this.m28(new NumberSetting("WaitTimeOut", 500.0, 0.0, 2000.0, 10.0, 10.0, () -> this.page.getObj() == Page.Misc, null, "", false));

    public AutoCrystal() {
        super("AutoCrystal", "Automatically places and breaks end crystals.", Category.COMBAT);
        INSTANCE = this;
    }

    @Override
    public String getText57() {
        return this.displayTarget != null && this.lastDamage > 0.0f ? this.df.format(this.lastDamage) : null;
    }

    @Override
    public void onEnable() {
        this.crystalPos = null;
        this.tempPos = null;
        this.tempBreakCrystal = null;
        this.displayTarget = null;
        this.syncTimer.m533();
        this.lastBreakTimer.m533();
    }

    @Override
    public void m709() {
        this.crystalPos = null;
        this.tempPos = null;
        if (Client.renderUtil3.isSet82()) {
            Client.renderUtil3.m608();
        }
        super.m709();
    }

    @EventHandler
    private void onTick(Event2.Event2Inner event2Inner) {
        if (Module.isSet37()) {
            return;
        }
        if (this.calcDelay.m336(this.updateDelay.getLong())) {
            this.calcDelay.m533();
            this.calcCrystalPos();
            this.basePos = this.tempBasePos;
            this.lastDamage = this.tempDamage;
            this.breakCrystal = this.tempBreakCrystal;
            this.crystalPos = this.tempPos;
        }
        if (!this.shouldReturn()) {
            this.doInteract();
            BlockPos basePos = this.basePos;
            if (((Boolean)this.basePlace.getObj()).booleanValue() && basePos != null && BlockUtil.m57(basePos)) {
                this.doBasePlace(basePos);
            }
        }
    }

    @EventHandler
    private void onPacketSend(PacketEvent.PacketEventInner2 packetEventInner2) {
        if (packetEventInner2.getPacket() instanceof UpdateSelectedSlotC2SPacket packet && this.lastSlot != packet.getSelectedSlot()) {
            this.lastSlot = packet.getSelectedSlot();
            if (this.autoSwap.getObj() != SwapMode.Silent2) {
                this.switchTimer.m533();
            }
        }
    }

    @EventHandler
    private void onRender3D(RenderLevelEvent renderLevelEvent) {
        Matrix4f matrix4f = renderLevelEvent.getMatrix4f3();
        if (this.displayTarget != null && !this.noPosTimer.m336(500L)) {
            this.doRender(matrix4f, this.displayTarget, (TargetESP)this.mode.getObj());
        }
        if (!((Boolean)this.render.getObj()).booleanValue()) {
            return;
        }
        BlockPos cpos = ((Boolean)this.sync.getObj()).booleanValue() && this.crystalPos != null ? this.syncPos : this.crystalPos;
        if (cpos != null) {
            this.placeVec3d = cpos.down().toCenterPos();
        }
        if (this.placeVec3d == null) {
            return;
        }
        boolean faded = this.noPosTimer.m336((long)((Double)this.startFadeTime.getObj()).doubleValue() * 1000L);
        double target = faded ? 0.0 : 0.5;
        if (this.fadeSpeed.getDouble16() >= 1.0) {
            this.currentFade = target;
        } else {
            double speed = Math.max(0.001, this.fadeSpeed.getDouble20() / 10.0);
            this.currentFade += (target - this.currentFade) * speed;
            if (Math.abs(target - this.currentFade) < 0.001) {
                this.currentFade = target;
            }
        }
        if (this.currentFade == 0.0) {
            this.curVec3d = null;
            return;
        }
        if (this.curVec3d != null && this.sliderSpeed.getDouble16() < 1.0) {
            double speed = this.sliderSpeed.getDouble20() / 10.0;
            this.curVec3d = new Vec3d(
                this.curVec3d.x + (this.placeVec3d.x - this.curVec3d.x) * speed,
                this.curVec3d.y + (this.placeVec3d.y - this.curVec3d.y) * speed,
                this.curVec3d.z + (this.placeVec3d.z - this.curVec3d.z) * speed);
        } else {
            this.curVec3d = this.placeVec3d;
        }
        Box cbox = new Box(this.curVec3d, this.curVec3d);
        cbox = ((Boolean)this.shrink.getObj()).booleanValue() ? cbox.expand(this.currentFade) : cbox.expand(0.5);
        if (this.fill.isSet30()) {
            int fcolor = (Integer)this.fill.getObj();
            EspRenderLayers.m69(matrix4f, cbox, RenderUtil3.m517(fcolor, (int)((double)(fcolor >>> 24 & 0xFF) * this.currentFade * 2.0)), true);
        }
        if (this.box.isSet30()) {
            int bcolor = (Integer)this.box.getObj();
            EspRenderLayers.m688(matrix4f, cbox, RenderUtil3.m517(bcolor, (int)((double)(bcolor >>> 24 & 0xFF) * this.currentFade * 2.0)), true);
        }
    }

    public void doRender(Matrix4f matrix4f, Entity entity, TargetESP mode) {
        if (mode == TargetESP.None || !entity.isAlive()) {
            return;
        }
        Box box = entity.getBoundingBox().expand(0.0, 0.1, 0.0);
        boolean hit = this.breakCrystal != null || (this.tempBreakCrystal != null && this.breakDamage > 0.0f);
        int fillColor = hit ? (Integer)this.hitColor.getObj() : (Integer)this.color.getObj();
        int lineColor = hit ? (Integer)this.hitOutlineColor.getObj() : (Integer)this.outlineColor.getObj();
        if (mode == TargetESP.Fill) {
            EspRenderLayers.m69(matrix4f, box, fillColor, true);
        }
        EspRenderLayers.m688(matrix4f, box, lineColor, true);
    }

    private void doInteract() {
        BlockPos crystalPos = this.crystalPos;
        if (crystalPos != null) {
            this.doCrystal(crystalPos);
        }
        if (this.breakCrystal != null) {
            this.doBreak(this.breakCrystal);
            this.breakCrystal = null;
        }
    }

    public Vec3d getAttackVec(Vec3d feetPos) {
        Vec3d eye = MC.client3.player.getEyePos();
        double range = this.breakRange.getDouble20();
        double step = Math.max(0.01, this.attackVecStep.getDouble20());
        double best = Double.MAX_VALUE;
        Vec3d bestVec = null;
        for (double y = 0.0; y <= 2.0 + 1e-6; y += step) {
            for (double x = -0.5; x <= 0.5 + 1e-6; x += step) {
                for (double z = -0.5; z <= 0.5 + 1e-6; z += step) {
                    Vec3d point = new Vec3d(feetPos.x + x, feetPos.y + y, feetPos.z + z);
                    double dist = eye.distanceTo(point);
                    if (dist > range || dist >= best) continue;
                    if (this.rayBlocked(eye, point)) continue;
                    best = dist;
                    bestVec = point;
                }
            }
        }
        return bestVec;
    }

    private boolean rayBlocked(Vec3d from, Vec3d to) {
        HitResult result = MC.client3.world.raycast(new RaycastContext(from, to, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, MC.client3.player));
        return result != null && result.getType() == HitResult.Type.BLOCK;
    }

    private boolean shouldReturn() {
        if (((Boolean)this.eatingPause.getObj()).booleanValue() && MC.client3.player.isUsingItem()) {
            this.lastBreakTimer.m533();
            return true;
        }
        return false;
    }

    private List<PlayerEntity> getEnemies() {
        List<PlayerEntity> list = new ArrayList<PlayerEntity>();
        double range = this.targetRange.getDouble20();
        for (PlayerEntity player : MC.client3.world.getPlayers()) {
            if (player == MC.client3.player || !player.isAlive() || player.isSpectator()) continue;
            if (MC.client3.player.distanceTo(player) > range) continue;
            if (player.hurtTime > this.hurtTime.getInt50()) continue;
            list.add(player);
        }
        return list;
    }

    private void calcCrystalPos() {
        if (((Boolean)this.breakOnlyHasCrystal.getObj()).booleanValue()
                && !MC.client3.player.getMainHandStack().isOf(Items.END_CRYSTAL)
                && !MC.client3.player.getOffHandStack().isOf(Items.END_CRYSTAL)
                && !this.hasCrystal()) {
            this.tempPos = null;
            this.tempBreakCrystal = null;
            this.lastBreakTimer.m533();
            return;
        }
        boolean needBasePlace = ((Boolean)this.basePlace.getObj()).booleanValue() && this.baseTimer.m336(this.delay.getLong()) && this.getBlock() != -1;
        this.tempBreakCrystal = null;
        this.breakDamage = 0.0f;
        this.tempPos = null;
        this.tempDamage = 0.0f;
        this.tempBasePos = null;
        float baseDamage = 0.0f;
        List<PlayerEntity> list = this.getEnemies();
        if (list.isEmpty()) {
            this.lastBreakTimer.m533();
            return;
        }
        for (EndCrystalEntity crystal : MC.client3.world.getNonSpectatingEntities(EndCrystalEntity.class, MC.client3.player.getBoundingBox().expand(this.breakRange.getDouble20() + 2.0))) {
            if (crystal.age < this.minAge.getInt50()) continue;
            Vec3d attackVec = this.getAttackVec(crystal.getPos());
            if (attackVec == null) continue;
            if (!MC.client3.player.canSee((Entity)crystal) && MC.client3.player.getEyePos().distanceTo(attackVec) > this.wallRange.getDouble20()) continue;
            float selfDamage = this.calculateDamage(crystal.getPos(), MC.client3.player);
            for (PlayerEntity target : list) {
                float damage = this.calculateDamage(crystal.getPos(), target);
                if (damage <= this.breakDamage || !this.checkSelfDamage(selfDamage, damage, target)) continue;
                this.breakDamage = damage;
                this.tempBreakCrystal = crystal;
                this.displayTarget = target;
            }
        }
        double range = this.breakRange.getDouble20() + 1.5;
        BlockPos center = BlockPos.ofFloored((double)MC.client3.player.getX(), (double)MC.client3.player.getY(), (double)MC.client3.player.getZ());
        int r = (int)Math.ceil(range);
        for (int x = -r; x <= r; ++x) {
            for (int y = -r; y <= r; ++y) {
                for (int z = -r; z <= r; ++z) {
                    BlockPos pos = center.add(x, y, z);
                    if (pos.getSquaredDistance((double)center.getX(), (double)center.getY(), (double)center.getZ()) > range * range) continue;
                    boolean base = needBasePlace && BlockUtil.m57(pos.down());
                    Vec3d attackVec = this.getAttackVec(pos.toBottomCenterPos());
                    if (attackVec == null || this.behindWall(pos, attackVec) || !this.canTouch(pos.down()) || !this.canPlaceCrystal(pos, true, false)) continue;
                    float selfDamage = base ? this.calculateBaseDamage(pos, MC.client3.player) : this.calculateDamage(pos.toBottomCenterPos(), MC.client3.player);
                    for (PlayerEntity target : list) {
                        if (base && ((Boolean)this.onlyBelow.getObj()).booleanValue() && pos.getY() - 0.5 > target.getY()) continue;
                        float damage = base ? this.calculateBaseDamage(pos, target) : this.calculateDamage(pos.toBottomCenterPos(), target);
                        if (base) {
                            if (this.tempDamage <= this.overrideMax.getDouble20() && damage > this.tempDamage && damage > baseDamage
                                    && !(selfDamage > this.baseMax.getDouble20())
                                    && !(damage < ItemUtil.m158(target))
                                    && (!(this.reserve.getDouble20() > 0.0) || !(selfDamage > MC.client3.player.getHealth() + MC.client3.player.getAbsorptionAmount() - this.reserve.getDouble20()))
                                    && (!((Boolean)this.baseBalance.getObj()).booleanValue() || !(damage < selfDamage))) {
                                this.displayTarget = target;
                                baseDamage = damage;
                                this.tempBasePos = pos.down();
                                this.tempPos = null;
                            }
                        } else if (damage > this.tempDamage && (damage >= baseDamage || this.tempDamage > this.overrideMax.getDouble20())
                                && this.checkSelfDamage(selfDamage, damage, target)) {
                            this.displayTarget = target;
                            this.tempPos = pos;
                            this.tempBasePos = null;
                            this.tempDamage = damage;
                        }
                    }
                }
            }
        }
        if (((Boolean)this.antiSurround.getObj()).booleanValue() && this.tempDamage <= this.antiSurroundMax.getDouble20()) {
            for (PlayerEntity target : list) {
                BlockPos footPos = BlockPos.ofFloored(target.getX(), target.getY() + 0.5, target.getZ());
                if (!BlockUtil.m57(footPos)) continue;
                for (Direction i : Direction.values()) {
                    if (i == Direction.DOWN || i == Direction.UP) continue;
                    BlockPos offsetPos = footPos.offset(i);
                    if (!BlockUtil.m57(offsetPos)) continue;
                    for (Direction direction : Direction.values()) {
                        if (direction == Direction.DOWN || direction == Direction.UP) continue;
                        BlockPos crystalPos = offsetPos.offset(direction);
                        if (!this.canPlaceCrystal(crystalPos, false, false)) continue;
                        float selfDamage = this.calculateDamage(crystalPos.toBottomCenterPos(), MC.client3.player);
                        float damage = this.calculateDamage(crystalPos.toBottomCenterPos(), target);
                        if (this.checkSelfDamage(selfDamage, damage, target) && damage > this.tempDamage) {
                            this.tempPos = crystalPos;
                            this.tempDamage = damage;
                            this.displayTarget = target;
                        }
                    }
                }
            }
        }
    }

    private boolean checkSelfDamage(float selfDamage, float damage, PlayerEntity target) {
        if (selfDamage > this.maxSelf.getDouble20()) return false;
        if (this.reserve.getDouble20() > 0.0 && selfDamage > MC.client3.player.getHealth() + MC.client3.player.getAbsorptionAmount() - this.reserve.getDouble20()) return false;
        if (damage < ItemUtil.m158(target)) {
            double threshold = this.getDamage(target);
            if (damage < threshold) return false;
            if (((Boolean)this.balance.getObj()).booleanValue()) {
                if (threshold == this.forceMin.getDouble20()) {
                    if (damage < selfDamage - 2.5f) return false;
                } else if (damage < selfDamage + this.balanceOffset.getDouble20()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean canPlaceCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
        BlockPos obsPos = pos.down();
        BlockPos boost = obsPos.up();
        BlockPos boost2 = boost.up();
        BlockState obsState = MC.client3.world.getBlockState(obsPos);
        return (obsState.isOf(Blocks.BEDROCK) || obsState.isOf(Blocks.OBSIDIAN))
                && BlockUtil.m573(obsPos) != null
                && this.noEntityBlockCrystal(boost, ignoreCrystal, ignoreItem)
                && this.noEntityBlockCrystal(boost2, ignoreCrystal, ignoreItem)
                && (MC.client3.world.isAir(boost) || MC.client3.world.getBlockState(boost).isOf(Blocks.FIRE));
    }

    private boolean noEntityBlockCrystal(BlockPos pos, boolean ignoreCrystal, boolean ignoreItem) {
        Box box = new Box(pos);
        for (Entity entity : MC.client3.world.getOtherEntities(null, box)) {
            if (!entity.isAlive()) continue;
            if (entity instanceof ItemEntity) {
                if (ignoreItem) continue;
                return false;
            }
            if (entity instanceof EndCrystalEntity) {
                if (!ignoreCrystal) return false;
                if (this.getAttackVec(entity.getPos()) == null) return false;
                if (!MC.client3.player.canSee(entity) && MC.client3.player.getEyePos().distanceTo(entity.getPos()) > this.wallRange.getDouble20()) return false;
                continue;
            }
            return false;
        }
        return true;
    }

    public boolean behindWall(BlockPos pos, Vec3d attackVec) {
        Vec3d crystalEyePos = new Vec3d((double)pos.getX() + 0.5, (double)pos.getY() + 1.7, (double)pos.getZ() + 0.5);
        HitResult result = MC.client3.world.raycast(new RaycastContext(MC.client3.player.getEyePos(), crystalEyePos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, MC.client3.player));
        return result != null && result.getType() != HitResult.Type.MISS && MC.client3.player.getEyePos().distanceTo(attackVec) > this.wallRange.getDouble20();
    }

    private boolean canTouch(BlockPos pos) {
        BlockUtil.Data data = BlockUtil.m573(pos);
        if (data == null) return false;
        return data.getVec3d5().distanceTo(MC.client3.player.getEyePos()) <= this.placeRange.getDouble20();
    }

    private void doCrystal(BlockPos pos) {
        if (this.canPlaceCrystal(pos, false, false)) {
            this.doPlace(pos, ((Boolean)this.rotate.getObj()).booleanValue() && ((Boolean)this.onPlace.getObj()).booleanValue());
        }
        this.doBreak(pos);
    }

    private void doBasePlace(BlockPos pos) {
        if (!this.baseTimer.m336(this.delay.getLong())) {
            return;
        }
        if (this.getBlock() == -1) {
            return;
        }
        BlockUtil.Data data = BlockUtil.m573(pos);
        if (data == null || MC.client3.player.getEyePos().distanceTo(data.getVec3d5()) > 6.0) {
            return;
        }
        boolean switched = Client.renderUtil3.m223(stack -> stack.isOf(Blocks.OBSIDIAN.asItem()), ((Boolean)this.inventory.getObj()).booleanValue() ? shit.module.client.ClientSetting.SwitchMode.INVENTORY : shit.module.client.ClientSetting.SwitchMode.NORMAL);
        if (!switched) {
            return;
        }
        BlockUtil.m868(pos, Hand.MAIN_HAND, data.createBlockHitResult());
        if (((Boolean)this.inventory.getObj()).booleanValue()) {
            Client.renderUtil3.m608();
        }
        this.baseTimer.m533();
    }

    private int getBlock() {
        for (int i = 0; i < 36; ++i) {
            if (MC.client3.player.getInventory().getStack(i).isOf(Blocks.OBSIDIAN.asItem())) return i;
        }
        return -1;
    }

    public boolean hasCrystal() {
        return this.autoSwap.getObj() != SwapMode.None && this.getCrystal() != -1;
    }

    private int getCrystal() {
        for (int i = 0; i < 36; ++i) {
            if (MC.client3.player.getInventory().getStack(i).isOf(Items.END_CRYSTAL)) return i;
        }
        return -1;
    }

    private void doSwap(int slot) {
        if (slot == -1) return;
        MC.client3.player.getInventory().setSelectedSlot(slot);
        MC.client3.player.networkHandler.sendPacket((Packet)new UpdateSelectedSlotC2SPacket(slot));
    }

    private boolean breakDelayPassed() {
        long now = System.currentTimeMillis();
        if (now - this.lastBreakMs < this.breakDelay.getLong()) return false;
        this.lastBreakMs = now;
        return true;
    }

    private void doBreak(EndCrystalEntity entity) {
        this.noPosTimer.m533();
        if (!((Boolean)this.breakSetting.getObj()).booleanValue() || !entity.isAlive()) return;
        if (this.displayTarget != null && this.displayTarget.hurtTime > this.waitHurt.getInt50() && !this.syncTimer.m336(this.syncTimeout.getLong())) return;
        this.lastBreakTimer.m533();
        if (this.autoSwap.getObj() != SwapMode.Silent2 && !this.switchTimer.m336(this.switchCooldown.getLong())) return;
        if (entity.age < this.minAge.getInt50()) return;
        if (!this.breakDelayPassed()) {
            if (((Boolean)this.forcePlace.getObj()).booleanValue() && this.crystalPos != null) this.doPlace(this.crystalPos, false);
            return;
        }
        if (((Boolean)this.rotate.getObj()).booleanValue() && ((Boolean)this.onBreak.getObj()).booleanValue()) {
            Vec3d attackVec = this.getAttackVec(entity.getPos());
            if (!this.faceVector(attackVec == null ? entity.getPos() : attackVec)) {
                if (((Boolean)this.forcePlace.getObj()).booleanValue() && this.crystalPos != null) this.doPlace(this.crystalPos, false);
                return;
            }
        }
        this.syncTimer.m533();
        this.syncPos = entity.getBlockPos();
        MC.client3.player.networkHandler.sendPacket((Packet)PlayerInteractEntityC2SPacket.attack((Entity)entity, MC.client3.player.isSneaking()));
        if (((Boolean)this.resetCD.getObj()).booleanValue()) MC.client3.player.resetLastAttackedTicks();
        MC.client3.player.swingHand(Hand.MAIN_HAND);
        if (((Boolean)this.breakRemove.getObj()).booleanValue()) MC.client3.world.removeEntity(entity.getId(), Entity.RemovalReason.KILLED);
        this.afterBreakActions(entity.getPos());
    }

    private void doBreak(BlockPos pos) {
        this.noPosTimer.m533();
        if (!((Boolean)this.breakSetting.getObj()).booleanValue()) return;
        if (this.displayTarget != null && this.displayTarget.hurtTime > this.waitHurt.getInt50() && !this.syncTimer.m336(this.syncTimeout.getLong())) return;
        this.lastBreakTimer.m533();
        if (this.autoSwap.getObj() != SwapMode.Silent2 && !this.switchTimer.m336(this.switchCooldown.getLong())) return;
        Box box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1);
        for (EndCrystalEntity entity : MC.client3.world.getNonSpectatingEntities(EndCrystalEntity.class, box)) {
            if (entity.age < this.minAge.getInt50() || !entity.isAlive()) continue;
            if (!this.breakDelayPassed()) {
                if (((Boolean)this.forcePlace.getObj()).booleanValue() && this.crystalPos != null) this.doPlace(this.crystalPos, false);
                return;
            }
            if (((Boolean)this.rotate.getObj()).booleanValue() && ((Boolean)this.onBreak.getObj()).booleanValue()) {
                Vec3d attackVec = this.getAttackVec(entity.getPos());
                if (!this.faceVector(attackVec == null ? entity.getPos() : attackVec)) {
                    if (((Boolean)this.forcePlace.getObj()).booleanValue() && this.crystalPos != null) this.doPlace(this.crystalPos, false);
                    return;
                }
            }
            this.syncTimer.m533();
            this.syncPos = pos;
            MC.client3.player.networkHandler.sendPacket((Packet)PlayerInteractEntityC2SPacket.attack((Entity)entity, MC.client3.player.isSneaking()));
            if (((Boolean)this.resetCD.getObj()).booleanValue()) MC.client3.player.resetLastAttackedTicks();
            MC.client3.player.swingHand(Hand.MAIN_HAND);
            if (((Boolean)this.breakRemove.getObj()).booleanValue()) MC.client3.world.removeEntity(entity.getId(), Entity.RemovalReason.KILLED);
            this.afterBreakActions(entity.getPos());
            return;
        }
        if (((Boolean)this.forcePlace.getObj()).booleanValue() && this.crystalPos != null) this.doPlace(this.crystalPos, false);
    }

    private void afterBreakActions(Vec3d brokenPos) {
        BlockPos crystalPos = this.crystalPos;
        if (crystalPos != null && this.displayTarget != null && this.lastDamage >= this.getDamage(this.displayTarget) && ((Boolean)this.afterBreak.getObj()).booleanValue()) {
            this.doPlace(crystalPos, false);
        }
    }

    private void doPlace(BlockPos pos, boolean doRotate) {
        this.noPosTimer.m533();
        if (!((Boolean)this.place.getObj()).booleanValue()) return;
        if (!MC.client3.player.getMainHandStack().isOf(Items.END_CRYSTAL) && !MC.client3.player.getOffHandStack().isOf(Items.END_CRYSTAL) && !this.hasCrystal()) return;
        if (!this.canTouch(pos.down())) return;
        if (!this.placeTimer.m336(this.placeDelay.getLong())) return;
        BlockPos obsPos = pos.down();
        BlockUtil.Data data = BlockUtil.m573(obsPos);
        if (data == null) return;
        Vec3d vec = data.getVec3d5();
        if (data.direction() != Direction.UP && data.direction() != Direction.DOWN) {
            vec = vec.add(0.0, 0.45, 0.0);
        }
        if (doRotate && !this.faceVector(vec)) return;
        this.placeTimer.m533();
        this.syncPos = pos;
        boolean mainOrOff = MC.client3.player.getMainHandStack().isOf(Items.END_CRYSTAL) || MC.client3.player.getOffHandStack().isOf(Items.END_CRYSTAL);
        Hand hand = MC.client3.player.getOffHandStack().isOf(Items.END_CRYSTAL) ? Hand.OFF_HAND : Hand.MAIN_HAND;
        int old = MC.client3.player.getInventory().getSelectedSlot();
        if (!mainOrOff) {
            int crystal = this.getCrystal();
            if (crystal == -1) return;
            this.doSwap(crystal);
            hand = Hand.MAIN_HAND;
        }
        MC.client3.interactionManager.interactBlock(MC.client3.player, hand, data.createBlockHitResult());
        MC.client3.player.swingHand(hand);
        if (!mainOrOff && this.autoSwap.getObj() != SwapMode.Inventory) {
            this.doSwap(old);
        }
        if (doRotate) {
            Client.mathUtil.m370();
        }
    }

    private boolean faceVector(Vec3d directionVec) {
        if (directionVec == null) return false;
        float[] rot = MathUtil.m547(MC.client3.player.getEyePos(), directionVec);
        Client.mathUtil.m303(rot[0], rot[1]);
        return true;
    }

    public float calculateDamage(BlockPos pos, PlayerEntity player) {
        return this.calculateDamage(new Vec3d((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5), player);
    }

    public float calculateDamage(Vec3d pos, PlayerEntity player) {
        return this.calcEnhancedDamage(pos, player);
    }

    public float calculateBaseDamage(BlockPos pos, PlayerEntity player) {
        return this.calcEnhancedDamage(new Vec3d((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5), player);
    }

    private float calcEnhancedDamage(Vec3d explosionPos, PlayerEntity target) {
        double distance = target.getPos().distanceTo(explosionPos);
        if (distance > 12.0) return 0.0f;
        float exposure = this.getEnhancedExposure(explosionPos, target);
        float rawDamage = (float)((exposure * exposure + exposure) / 2.0 * 7.0 * 6.0 + 1.0);
        float armor = target.getArmor();
        float toughness = target.getArmorToughness();
        float armorReduction = Math.max(armor / 5.0f, armor - rawDamage / (2.0f + toughness / 4.0f));
        float damageAfterArmor = rawDamage * (1.0f - Math.min(armorReduction, 20.0f) / 25.0f);
        float enchantReduction = this.getEnchantReduction(target);
        float damageAfterEnchant = damageAfterArmor * (1.0f - enchantReduction);
        return this.applyResistance(target, damageAfterEnchant);
    }

    private float getEnhancedExposure(Vec3d explosionPos, PlayerEntity target) {
        Box box = target.getBoundingBox();
        int xS = 3, yS = 4, zS = 3;
        double xStep = (box.maxX - box.minX) / xS;
        double yStep = (box.maxY - box.minY) / yS;
        double zStep = (box.maxZ - box.minZ) / zS;
        if (xStep <= 0) xStep = 0.1;
        if (yStep <= 0) yStep = 0.1;
        if (zStep <= 0) zStep = 0.1;
        int total = 0, unblocked = 0;
        for (int xi = 0; xi <= xS; xi++) {
            for (int yi = 0; yi <= yS; yi++) {
                for (int zi = 0; zi <= zS; zi++) {
                    Vec3d sample = new Vec3d(box.minX + xStep * xi, box.minY + yStep * yi, box.minZ + zStep * zi);
                    if (!this.isRayBlocked(sample, explosionPos)) unblocked++;
                    total++;
                }
            }
        }
        return total == 0 ? 0.0f : (float)unblocked / total;
    }

    private boolean isRayBlocked(Vec3d from, Vec3d to) {
        HitResult result = MC.client3.world.raycast(new RaycastContext(from, to, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, MC.client3.player));
        if (result == null || result.getType() != HitResult.Type.BLOCK) return false;
        BlockPos hitPos = result.getBlockPos();
        if (((Boolean)this.terrainIgnore.getObj()).booleanValue()) {
            BlockState state = MC.client3.world.getBlockState(hitPos);
            return !state.isOf(Blocks.BEDROCK) && !state.isOf(Blocks.OBSIDIAN);
        }
        return true;
    }

    private float getEnchantReduction(PlayerEntity target) {
        int protectionLevel = 0;
        for (EquipmentSlot equipmentSlot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack itemStack = target.getEquippedStack(equipmentSlot);
            if (itemStack.isEmpty()) continue;
            for (Object2IntMap.Entry entry : itemStack.getEnchantments().getEnchantmentEntries()) {
                RegistryEntry registryEntry = (RegistryEntry)entry.getKey();
                if (registryEntry.matchesKey(Enchantments.PROTECTION)) {
                    protectionLevel += entry.getIntValue();
                }
                if (registryEntry.matchesKey(Enchantments.BLAST_PROTECTION)) {
                    protectionLevel += entry.getIntValue() * 2;
                }
            }
        }
        return Math.min(protectionLevel * 0.04f, 0.8f);
    }

    private float applyResistance(PlayerEntity target, float damage) {
        var effect = target.getStatusEffect(StatusEffects.RESISTANCE);
        if (effect != null) {
            return Math.max(0.0f, damage * (1.0f - Math.min((effect.getAmplifier() + 1) * 0.2f, 1.0f)));
        }
        return Math.max(0.0f, damage);
    }

    private double getDamage(PlayerEntity target) {
        if (((Boolean)this.slowPlace.getObj()).booleanValue() && this.lastBreakTimer.m336(this.slowDelay.getLong())) {
            return this.slowMinDamage.getDouble20();
        } else if (((Boolean)this.lethalOverride.getObj()).booleanValue() && ItemUtil.m158(target) <= this.forceMaxHealth.getDouble20()) {
            return this.forceMin.getDouble20();
        } else {
            if (((Boolean)this.armorBreaker.getObj()).booleanValue() && ItemUtil.m749(target, this.maxDurable.getInt50())) {
                return this.armorBreakerDamage.getDouble20();
            }
            return this.minDamage.getDouble20();
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Page {
        General, Base, Misc, Rotation, Check, Calc, Render;
    }

    @Environment(value=EnvType.CLIENT)
    public static enum SwapMode {
        None, Normal, Silent, Silent2, Inventory;
    }

    @Environment(value=EnvType.CLIENT)
    public static enum TargetESP {
        Box, Fill, None;
    }
}
