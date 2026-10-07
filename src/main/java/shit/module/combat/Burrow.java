/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.block.BlockState
 *  net.minecraft.block.Blocks
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityPose
 *  net.minecraft.entity.ExperienceOrbEntity
 *  net.minecraft.entity.ItemEntity
 *  net.minecraft.entity.decoration.EndCrystalEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.entity.projectile.ArrowEntity
 *  net.minecraft.entity.projectile.thrown.ExperienceBottleEntity
 *  net.minecraft.item.BlockItem
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$PositionAndOnGround
 *  net.minecraft.sound.SoundCategory
 *  net.minecraft.sound.SoundEvents
 *  net.minecraft.util.Hand
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package shit.module.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import shit.Client;
import shit.event.Event2;
import shit.event.EventHandler;
import shit.misc.Helper7;
import shit.misc.MathUtil;
import shit.module.Category;
import shit.module.Module;
import shit.module.client.ClientSetting;
import shit.module.combat.ZealotCrystalPlus;
import shit.setting.BooleanSetting;
import shit.setting.EnumSetting;
import shit.setting.NumberSetting;
import shit.util.BlockUtil;
import shit.util.MC;

@Environment(value=EnvType.CLIENT)
public class Burrow
extends Module {
    public static Burrow INSTANCE;
    private final EnumSetting rotateMode;
    private final EnumSetting lagMode;
    private final EnumSetting moveLagMode;
    private final BooleanSetting disable;
    private final BooleanSetting jumpDisable;
    private final NumberSetting delay;
    private final NumberSetting webTime;
    private final BooleanSetting enderChest;
    private final NumberSetting cuiCanHeight;
    private final BooleanSetting antiLag;
    private final BooleanSetting singleBlock;
    private final BooleanSetting detectMining;
    private final BooleanSetting headFill;
    private final BooleanSetting usingPause;
    private final BooleanSetting down;
    private final BooleanSetting noSelfPos;
    private final BooleanSetting sound;
    private final NumberSetting blocksPer;
    private final NumberSetting offset;
    private final BooleanSetting break_;
    private final BooleanSetting wait;
    private final BooleanSetting fakeMove;
    private final BooleanSetting allowCenter;
    private final NumberSetting preCorrect;
    private final NumberSetting moveDis;
    private final NumberSetting moveDis2;
    private final NumberSetting correct;
    private final NumberSetting yOffset;
    private final EnumSetting switchMode;
    private final NumberSetting smartXZ;
    private final NumberSetting smartUp;
    private final NumberSetting smartDown;
    private final NumberSetting smartDistance;
    private int count79;
    private final List list16;
    private Vec3d vec3d10;
    private final Helper7 helper745;
    private final Helper7 helper732;

        public Burrow() {
        super("Burrow", "Clips into obsidian (\u5361\u9ed1\u66dc\u77f3).", Category.COMBAT);
        this.rotateMode = (EnumSetting)this.m28(new EnumSetting("RotateMode", RotateMode.BYPASS));
        this.lagMode = (EnumSetting)this.m28(new EnumSetting("LagMode", LagMode.Alien));
        this.moveLagMode = (EnumSetting)this.m28(new EnumSetting("MoveLagMode", LagMode.Smart));
        this.disable = (BooleanSetting)this.m28(new BooleanSetting("Disable", true));
        this.jumpDisable = (BooleanSetting)this.m28(new BooleanSetting("JumpDisable", true));
        this.delay = (NumberSetting)this.m28(new NumberSetting("Delay", 500.0, 0.0, 1000.0, 1.0));
        this.webTime = (NumberSetting)this.m28(new NumberSetting("WebTime", 0.0, 0.0, 500.0));
        this.enderChest = (BooleanSetting)this.m28(new BooleanSetting("EnderChest", true));
        this.cuiCanHeight = (NumberSetting)this.m28(new NumberSetting("CuiCanHeight", 4.0, -10.0, 10.0, 0.1));
        this.antiLag = (BooleanSetting)this.m28(new BooleanSetting("AntiLag", false));
        this.singleBlock = (BooleanSetting)this.m28(new BooleanSetting("SingleBlock", false));
        this.detectMining = (BooleanSetting)this.m28(new BooleanSetting("DetectMining", false));
        this.headFill = (BooleanSetting)this.m28(new BooleanSetting("HeadFill", false));
        this.usingPause = (BooleanSetting)this.m28(new BooleanSetting("UsingPause", false));
        this.down = (BooleanSetting)this.m28(new BooleanSetting("Down", true));
        this.noSelfPos = (BooleanSetting)this.m28(new BooleanSetting("NoSelfPos", false));
        this.sound = (BooleanSetting)this.m28(new BooleanSetting("Sound", true));
        this.blocksPer = (NumberSetting)this.m28(new NumberSetting("BlocksPer", 4.0, 1.0, 4.0, 1.0));
        this.offset = (NumberSetting)this.m28(new NumberSetting("Offset", 0.3, 0.0, 0.5, 0.01));
        this.break_ = (BooleanSetting)this.m28(new BooleanSetting("Break", true));
        this.wait = (BooleanSetting)this.m28(new BooleanSetting("Wait", true));
        this.fakeMove = (BooleanSetting)this.m28(new BooleanSetting("FakeMove", true));
        this.allowCenter = (BooleanSetting)this.m28(new BooleanSetting("AllowCenter", true));
        this.preCorrect = (NumberSetting)this.m28(new NumberSetting("PreCorrect", 0.25, 0.0, 1.0, 0.001));
        this.moveDis = (NumberSetting)this.m28(new NumberSetting("MoveDis", 0.25, 0.0, 1.0, 0.001));
        this.moveDis2 = (NumberSetting)this.m28(new NumberSetting("MoveDis2", 0.25, 0.0, 1.0, 0.001));
        this.correct = (NumberSetting)this.m28(new NumberSetting("Correct", 0.25, 0.0, 1.0, 0.001));
        this.yOffset = (NumberSetting)this.m28(new NumberSetting("YOffset", 0.01, 0.0, 1.0, 0.001));
        this.switchMode = (EnumSetting)this.m28(new EnumSetting("SwitchMode", SwitchMode.DEFAULT));
        this.smartXZ = (NumberSetting)this.m28(new NumberSetting("SmartXZ", 3.0, 0.0, 10.0, 0.1));
        this.smartUp = (NumberSetting)this.m28(new NumberSetting("SmartUp", 3.0, 0.0, 10.0, 0.1));
        this.smartDown = (NumberSetting)this.m28(new NumberSetting("SmartDown", 3.0, 0.0, 10.0, 0.1));
        this.smartDistance = (NumberSetting)this.m28(new NumberSetting("SmartDistance", 2.0, 0.0, 10.0, 0.1));
        this.count79 = 0;
        this.list16 = new ArrayList();
        this.vec3d10 = Vec3d.ZERO;
        this.helper745 = new Helper7();
        this.helper732 = new Helper7();
    }

    @Override
    public void m709() {
        Client.mathUtil.m370();
        Client.renderUtil3.m608();
    }

    @EventHandler
    private void setEvent2Inner13(Event2.Event2Inner event2Inner) {
        boolean bl;
        Object object;
        if (Module.isSet37()) {
            return;
        }
        if (!((Boolean)this.disable.getObj()).booleanValue() && ((Boolean)this.jumpDisable.getObj()).booleanValue() && MC.client3.player.input.playerInput.jump()) {
            this.setFlag3(false);
            return;
        }
        if (this.isSet119()) {
            this.helper732.m533();
            return;
        }
        if (((Boolean)this.usingPause.getObj()).booleanValue() && MC.client3.player.isUsingItem()) {
            return;
        }
        if (!this.helper732.m432((Double)this.webTime.getObj())) {
            return;
        }
        if (!((Boolean)this.disable.getObj()).booleanValue() && !this.helper745.m432((Double)this.delay.getObj())) {
            return;
        }
        if (!MC.client3.player.isOnGround()) {
            return;
        }
        if (((Boolean)this.antiLag.getObj()).booleanValue() && (((BlockState)(object = MC.client3.world.getBlockState(MC.client3.player.getBlockPos().down()))).isAir() || ((BlockState)object).isReplaceable())) {
            return;
        }
        if (((Boolean)this.singleBlock.getObj()).booleanValue() && this.isSet97()) {
            if (((Boolean)this.disable.getObj()).booleanValue()) {
                this.setFlag3(false);
            }
            return;
        }
        object = this.getSwitchMode3();
        if (!Client.renderUtil3.m223((java.util.function.Predicate<ItemStack>)this::m718, object)) {
            this.setFlag3(false);
            return;
        }
        boolean bl2 = object == ClientSetting.SwitchMode.SILENT || object == ClientSetting.SwitchMode.INVENTORY;
        this.count79 = 0;
        this.list16.clear();
        double d = MC.client3.player.getX();
        double d2 = MC.client3.player.getY();
        double d3 = MC.client3.player.getZ();
        double d4 = (Boolean)this.singleBlock.getObj() != false ? 0.0 : (Double)this.offset.getObj();
        BlockPos blockPos = BlockPos.ofFloored((double)(d + d4), (double)(d2 + 0.5), (double)(d3 + d4));
        BlockPos blockPos2 = BlockPos.ofFloored((double)(d - d4), (double)(d2 + 0.5), (double)(d3 + d4));
        BlockPos blockPos3 = BlockPos.ofFloored((double)(d + d4), (double)(d2 + 0.5), (double)(d3 - d4));
        BlockPos blockPos4 = BlockPos.ofFloored((double)(d - d4), (double)(d2 + 0.5), (double)(d3 - d4));
        BlockPos blockPos5 = BlockPos.ofFloored((double)(d + d4), (double)(d2 + 1.5), (double)(d3 + d4));
        BlockPos blockPos6 = BlockPos.ofFloored((double)(d - d4), (double)(d2 + 1.5), (double)(d3 + d4));
        BlockPos blockPos7 = BlockPos.ofFloored((double)(d + d4), (double)(d2 + 1.5), (double)(d3 - d4));
        BlockPos blockPos8 = BlockPos.ofFloored((double)(d - d4), (double)(d2 + 1.5), (double)(d3 - d4));
        BlockPos blockPos9 = BlockPos.ofFloored((double)(d + d4), (double)(d2 - 1.0), (double)(d3 + d4));
        BlockPos blockPos10 = BlockPos.ofFloored((double)(d - d4), (double)(d2 - 1.0), (double)(d3 + d4));
        BlockPos blockPos11 = BlockPos.ofFloored((double)(d + d4), (double)(d2 - 1.0), (double)(d3 - d4));
        BlockPos blockPos12 = BlockPos.ofFloored((double)(d - d4), (double)(d2 - 1.0), (double)(d3 - d4));
        BlockPos blockPos13 = MC.client3.player.getBlockPos();
        boolean bl3 = false;
        if (!(this.m1026(blockPos) || this.m1026(blockPos2) || this.m1026(blockPos3) || this.m1026(blockPos4))) {
            boolean bl4;
            bl = (Boolean)this.headFill.getObj() == false || !this.m1026(blockPos5) && !this.m1026(blockPos6) && !this.m1026(blockPos7) && !this.m1026(blockPos8);
            boolean bl5 = bl4 = (Boolean)this.down.getObj() == false || !this.m1026(blockPos9) && !this.m1026(blockPos10) && !this.m1026(blockPos11) && !this.m1026(blockPos12);
            if (bl) {
                if (bl4) {
                    if (!((Boolean)this.wait.getObj()).booleanValue() && ((Boolean)this.disable.getObj()).booleanValue()) {
                        this.setFlag3(false);
                    }
                    if (bl2) {
                        Client.renderUtil3.m608();
                    }
                    return;
                }
            } else {
                bl3 = true;
            }
        }
        this.setObj24(blockPos);
        this.setObj24(blockPos2);
        this.setObj24(blockPos3);
        this.setObj24(blockPos4);
        bl = false;
        BlockPos blockPos14 = blockPos13.up(2);
        if (!(bl3 || MC.client3.player.getPose() == EntityPose.SWIMMING || this.m1053(blockPos14) || this.m1053(blockPos14.east()) || this.m1053(blockPos14.west()) || this.m1053(blockPos14.south()) || this.m1053(blockPos14.north()) || this.m1053(blockPos14.east().south()) || this.m1053(blockPos14.west().south()) || this.m1053(blockPos14.east().north()) || this.m1053(blockPos14.west().north()))) {
            this.m92(d, d2 + 0.4199999868869781, d3, false);
            this.m92(d, d2 + 0.7531999805212017, d3, false);
            this.m92(d, d2 + 0.9999957640154541, d3, false);
            this.m92(d, d2 + 1.1661092609382138, d3, false);
            this.vec3d10 = new Vec3d(d, d2 + 1.1661092609382138, d3);
        } else {
            bl = true;
            if (!((Boolean)this.fakeMove.getObj()).booleanValue()) {
                if (!((Boolean)this.wait.getObj()).booleanValue() && ((Boolean)this.disable.getObj()).booleanValue()) {
                    this.setFlag3(false);
                }
                if (bl2) {
                    Client.renderUtil3.m608();
                }
                return;
            }
            if (!this.isSet86()) {
                if (bl2) {
                    Client.renderUtil3.m608();
                }
                return;
            }
        }
        this.helper745.m533();
        Type type = this.getType4();
        if (type == Type.BYPASS) {
            if (bl) {
                Vec3d vec3d = new Vec3d(this.vec3d10.x, this.vec3d10.y + (double)MC.client3.player.getEyeHeight(MC.client3.player.getPose()), this.vec3d10.z);
                float[] fArray = MathUtil.m547(vec3d, MC.client3.player.getEntityPos());
                Client.mathUtil.m303(fArray[0], fArray[1]);
            } else {
                Client.mathUtil.m303(MC.client3.player.getYaw(), 90.0f);
            }
        }
        int n = 0;
        n += this.m848(blockPos13, (Object)type) ? 1 : 0;
        n += this.m848(blockPos, (Object)type) ? 1 : 0;
        n += this.m848(blockPos2, (Object)type) ? 1 : 0;
        n += this.m848(blockPos3, (Object)type) ? 1 : 0;
        n += this.m848(blockPos4, (Object)type) ? 1 : 0;
        if (((Boolean)this.down.getObj()).booleanValue()) {
            n += this.m848(blockPos9, (Object)type) ? 1 : 0;
            n += this.m848(blockPos10, (Object)type) ? 1 : 0;
            n += this.m848(blockPos11, (Object)type) ? 1 : 0;
            n += this.m848(blockPos12, (Object)type) ? 1 : 0;
        }
        if (((Boolean)this.headFill.getObj()).booleanValue() && bl) {
            n += this.m848(blockPos5, (Object)type) ? 1 : 0;
            n += this.m848(blockPos6, (Object)type) ? 1 : 0;
            n += this.m848(blockPos7, (Object)type) ? 1 : 0;
            n += this.m848(blockPos8, (Object)type) ? 1 : 0;
        }
        if (n > 0) {
            this.m632((Object)(bl ? (LagMode)((Object)this.moveLagMode.getObj()) : (LagMode)((Object)this.lagMode.getObj())), d, d2, d3);
            if (bl2) {
                Client.renderUtil3.m608();
            }
            if (((Boolean)this.disable.getObj()).booleanValue()) {
                this.setFlag3(false);
            }
        }
    }

    private void m632(Object object, double d, double d2, double d3) {
        LagMode lagMode = (LagMode)((Object)object);
        double d4 = d;
        double d5 = d2;
        double d6 = d3;
        Object var16_9 = null;
        switch (lagMode.ordinal()) {
            case 0: {
                this.m92(d4, d5 + 2.3400880035762786, d6, false);
                if (null == null) break;
            }
            case 1: {
                this.m92(d4, d5 - (Double)this.cuiCanHeight.getObj(), d6, false);
                if (null == null) break;
            }
            case 3: {
                this.m92(d4, -7.0, d6, false);
                if (null == null) break;
            }
            case 2: {
                this.m775(d4, d5, d6);
                break;
            }
        }
    }

    private void m775(double d, double d2, double d3) {
        block4: {
            double d4 = d;
            double d5 = d2;
            double d6 = d3;
            double d7 = (Double)this.smartXZ.getObj();
            double d8 = (Double)this.smartUp.getObj();
            double d9 = (Double)this.smartDown.getObj();
            double d10 = (Double)this.smartDistance.getObj();
            double d11 = Double.MAX_VALUE;
            Object var14_12 = null;
            BlockPos blockPos = null;
            for (double d12 = d4 - d7; d12 < d4 + d7; d12 += 1.0) {
                for (double d13 = d6 - d7; d13 < d6 + d7; d13 += 1.0) {
                    for (double d14 = d5 - d9; d14 < d5 + d8; d14 += 1.0) {
                        BlockPos blockPos2 = BlockPos.ofFloored((double)d12, (double)d14, (double)d13);
                        if (!this.m1048(blockPos2)) continue;
                        Vec3d vec3d = Vec3d.ofCenter((Vec3i)blockPos2).add(0.0, -0.5, 0.0);
                        double d15 = vec3d.distanceTo(MC.client3.player.getEntityPos());
                        if (d15 < d10) continue;
                        double d16 = MC.client3.player.squaredDistanceTo(vec3d.x, vec3d.y, vec3d.z);
                        if (blockPos != null) {
                            if (!(d16 < d11)) continue;
                        }
                        blockPos = blockPos2;
                        d11 = d16;
                        if (null == null) continue;
                    }
                    if (null == null) continue;
                }
                if (null == null) continue;
            }
            if (blockPos == null) break block4;
            this.m92((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5, false);
        }
    }

    private boolean m848(Object object, Object object2) {
        BlockPos blockPos = (BlockPos)object;
        Type type = (Type)((Object)object2);
        Object var6_5 = null;
        if (!this.m1026(blockPos)) {
            return false;
        }
        if (this.list16.contains(blockPos)) {
            return false;
        }
        if (this.count79 >= this.blocksPer.getInt50()) {
            return false;
        }
        Direction direction = this.m124(blockPos);
        if (direction == null) {
            return false;
        }
        BlockPos blockPos2 = blockPos.offset(direction);
        Direction direction2 = direction.getOpposite();
        Vec3d vec3d = Vec3d.ofCenter((Vec3i)blockPos2).add((double)direction2.getOffsetX() * 0.5, (double)direction2.getOffsetY() * 0.5, (double)direction2.getOffsetZ() * 0.5);
        if (((Boolean)this.sound.getObj()).booleanValue()) {
            MC.client3.world.playSoundClient((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0f, 0.8f, false);
        }
        Vec3d vec3d2 = new Vec3d(this.vec3d10.x, this.vec3d10.y + (double)MC.client3.player.getEyeHeight(MC.client3.player.getPose()), this.vec3d10.z);
        float[] fArray = MathUtil.m547(vec3d2, vec3d);
        BlockHitResult blockHitResult = new BlockHitResult(vec3d, direction2, blockPos2, false);
        switch (type.ordinal()) {
            case 1: {
                Client.mathUtil.m303(fArray[0], fArray[1]);
                if (null == null) break;
            }
            case 3: {
                Client.mathUtil.m468(fArray[0], fArray[1]);
                break;
            }
        }
        BlockUtil.m868(blockPos, Hand.MAIN_HAND, blockHitResult);
        if (type == Type.NORMAL) {
            Client.mathUtil.m844();
        }
        if (type == Type.type) {
            Client.mathUtil.m2();
        }
        this.list16.add(blockPos);
        ++this.count79;
        return true;
    }

    private boolean isSet86() {
        block22: {
            double[] dArray = new double[]{1.0, 0.0, -1.0};
            Object var2_2 = null;
            ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
            BlockPos blockPos = MC.client3.player.getBlockPos();
            for (double d : dArray) {
                for (double d2 : dArray) {
                    arrayList.add(BlockPos.ofFloored((double)(MC.client3.player.getX() + d), (double)MC.client3.player.getY(), (double)(MC.client3.player.getZ() + d2)));
                    if (null == null) continue;
                }
                if (null == null) continue;
            }
            Iterator<BlockPos> iterator = arrayList.iterator();
            while (iterator.hasNext()) {
                block20: {
                    BlockPos blockPos2;
                    block21: {
                        blockPos2 = (BlockPos)iterator.next();
                        if (!this.m76(blockPos2)) break block20;
                        if (this.m102(blockPos2)) break block20;
                        if (!((Boolean)this.headFill.getObj()).booleanValue()) break block21;
                        if (this.m102(blockPos2.up())) break block20;
                    }
                    this.setObj5(blockPos2);
                    return true;
                }
                if (null == null) continue;
            }
            ArrayList<BlockPos> object = new ArrayList<BlockPos>();
            for (BlockPos blockPos3 : arrayList) {
                if (!blockPos.equals((Object)blockPos3)) {
                    if (this.m76(blockPos3)) {
                        if (this.m1048(blockPos3)) {
                            object.add(blockPos3);
                        }
                    }
                }
                if (null == null) continue;
                break;
            }
            if (!object.isEmpty()) {
                this.setObj5(this.m792(object));
                return true;
            }
            ArrayList arrayList2 = new ArrayList();
            for (BlockPos blockPos4 : arrayList) {
                if (!blockPos.equals((Object)blockPos4)) {
                    if (this.m76(blockPos4)) {
                        arrayList2.add(blockPos4);
                    }
                }
                if (null == null) continue;
                break;
            }
            if (!arrayList2.isEmpty()) {
                this.setObj5(this.m792(arrayList2));
                return true;
            }
            if (((Boolean)this.allowCenter.getObj()).booleanValue()) {
                List<BlockPos> list = List.of(BlockPos.ofFloored((double)(MC.client3.player.getX() + 1.0), (double)MC.client3.player.getY(), (double)MC.client3.player.getZ()), BlockPos.ofFloored((double)(MC.client3.player.getX() - 1.0), (double)MC.client3.player.getY(), (double)MC.client3.player.getZ()), BlockPos.ofFloored((double)MC.client3.player.getX(), (double)MC.client3.player.getY(), (double)(MC.client3.player.getZ() - 1.0)), BlockPos.ofFloored((double)MC.client3.player.getX(), (double)MC.client3.player.getY(), (double)(MC.client3.player.getZ() + 1.0)));
                Iterator iterator9 = list.iterator();
                while (iterator9.hasNext()) {
                    BlockPos blockPos5 = (BlockPos)iterator9.next();
                    if (this.m1048(blockPos5)) {
                        this.setObj5(blockPos5);
                        return true;
                    }
                    if (null == null) continue;
                    break;
                }
            }
            if (((Boolean)this.wait.getObj()).booleanValue()) break block22;
            if (((Boolean)this.disable.getObj()).booleanValue()) {
                this.setFlag3(false);
            }
        }
        return false;
    }

    private void setObj5(Object object) {
        double d;
        double d2;
        BlockPos blockPos = (BlockPos)object;
        double d3 = (double)blockPos.getX() + 0.5;
        double d4 = (double)blockPos.getZ() + 0.5;
        double d5 = MC.client3.player.getX();
        double d6 = MC.client3.player.getZ();
        Object var4_7 = null;
        double d7 = MC.client3.player.getY() + (Double)this.yOffset.getObj();
        double d8 = (Double)this.preCorrect.getObj();
        if (d8 > 0.0) {
            d2 = Math.abs(d5 - d3);
            d = Math.abs(d6 - d4);
            if (d2 >= d8) {
                this.m92(d5 += d5 > d3 ? -d8 : d8, d7, d6, false);
            }
            if (d >= d8) {
                this.m92(d5, d7, d6 += d6 > d4 ? -d8 : d8, false);
            }
        }
        d8 = (Double)this.moveDis.getObj();
        if (d8 > 0.0) {
            d2 = Math.abs(d5 - d3);
            while (d2 > d8) {
                this.m92(d5 += d5 > d3 ? -d8 : d8, d7, d6, false);
                d2 = Math.abs(d5 - d3);
                if (null == null) continue;
            }
            d = Math.abs(d6 - d4);
            while (d > d8) {
                this.m92(d5, d7, d6 += d6 > d4 ? -d8 : d8, false);
                d = Math.abs(d6 - d4);
                if (null == null) continue;
            }
        }
        d8 = (Double)this.moveDis2.getObj();
        if (d8 > 0.0) {
            d2 = Math.abs(d5 - d3);
            while (d2 > d8) {
                this.m92(d5 += d5 > d3 ? -d8 : d8, d7, d6, false);
                d2 = Math.abs(d5 - d3);
                if (null == null) continue;
            }
            d = Math.abs(d6 - d4);
            while (d > d8) {
                this.m92(d5, d7, d6 += d6 > d4 ? -d8 : d8, false);
                d = Math.abs(d6 - d4);
                if (null == null) continue;
            }
        }
        d8 = (Double)this.correct.getObj();
        if (d8 > 0.0) {
            d2 = Math.abs(d5 - d3);
            d = Math.abs(d6 - d4);
            if (d2 >= d8) {
                this.m92(d5 += d5 > d3 ? -d8 : d8, d7, d6, false);
            }
            if (d >= d8) {
                this.m92(d5, d7, d6 += d6 > d4 ? -d8 : d8, false);
            }
        }
        this.vec3d10 = new Vec3d(d5, d7, d6);
    }

    private void m92(double d, double d2, double d3, boolean bl) {
        double d4 = d;
        double d5 = d2;
        double d6 = d3;
        boolean bl2 = bl;
        MC.client3.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(d4, d5, d6, bl2, false));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean m1048(Object object) {
        BlockPos blockPos = (BlockPos)object;
        Object var4_3 = null;
        if (!MC.client3.world.getBlockState(blockPos).isAir()) return false;
        if (!MC.client3.world.getBlockState(blockPos.up()).isAir()) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean m102(Object object) {
        BlockPos blockPos = (BlockPos)object;
        BlockState blockState = MC.client3.world.getBlockState(blockPos);
        Object var4_4 = null;
        if (blockState.isAir()) return true;
        if (!blockState.isReplaceable()) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean m922(Object object) {
        BlockPos blockPos = (BlockPos)object;
        BlockState blockState = MC.client3.world.getBlockState(blockPos);
        Object var4_4 = null;
        if (blockState.isAir()) return false;
        if (blockState.isReplaceable()) return false;
        if (!blockState.getFluidState().isEmpty()) return false;
        return true;
    }

    private Direction m124(Object object) {
        BlockPos blockPos = (BlockPos)object;
        Direction[] directionArray = Direction.values();
        int n = directionArray.length;
        Object var4_6 = null;
        for (int i = 0; i < n; ++i) {
            Direction direction = directionArray[i];
            if (!this.m922(blockPos.offset(direction))) continue;
            return direction;
        }
        return null;
    }

    private boolean m1026(Object object) {
        BlockPos blockPos = (BlockPos)object;
        Object var4_3 = null;
        if (((Boolean)this.noSelfPos.getObj()).booleanValue()) {
            if (blockPos.equals((Object)MC.client3.player.getBlockPos())) {
                return false;
            }
        }
        if (this.m124(blockPos) == null) {
            return false;
        }
        if (!this.m102(blockPos)) {
            return false;
        }
        return !this.m163(blockPos);
    }

    private boolean m76(Object object) {
        BlockPos blockPos = (BlockPos)object;
        return MC.client3.player.getBoundingBox().intersects(new Box(blockPos));
    }

    /*
     * Unable to fully structure code
     */
    private boolean m1053(Object var1_1) {
        BlockPos blockPos = (BlockPos)var1_1;
        BlockState blockState = MC.client3.world.getBlockState(blockPos);
        boolean bl = !blockState.isAir() && !blockState.isReplaceable() && blockState.getFluidState().isEmpty();
        boolean bl2 = blockState.isOf(Blocks.COBWEB);
        return (bl || bl2) && this.m76(blockPos.down(2));
    }

    private boolean isSet119() {
        block3: {
            block2: {
                Object var2_1 = null;
                if (MC.client3.player == null) break block2;
                if (MC.client3.world != null) break block3;
            }
            return false;
        }
        return MC.client3.world.getBlockState(MC.client3.player.getBlockPos()).isOf(Blocks.COBWEB);
    }

    private boolean isSet97() {
        block3: {
            block2: {
                Object var2_1 = null;
                if (MC.client3.player == null) break block2;
                if (MC.client3.world != null) break block3;
            }
            return false;
        }
        return !MC.client3.world.isSpaceEmpty((Entity)MC.client3.player, MC.client3.player.getBoundingBox());
    }

    private boolean m163(Object object) {
        BlockPos blockPos = (BlockPos)object;
        Iterator iterator = MC.client3.world.getOtherEntities(null, new Box(blockPos)).iterator();
        Object var4_4 = null;
        while (iterator.hasNext()) {
            Entity entity = (Entity)iterator.next();
            if (entity == MC.client3.player) continue;
            if (!entity.isAlive() || entity instanceof ItemEntity) continue;
            if (entity instanceof ExperienceOrbEntity) continue;
            if (entity instanceof ExperienceBottleEntity) continue;
            if (entity instanceof ArrowEntity) {
                if (null == null) continue;
            }
            if (entity instanceof EndCrystalEntity) {
                if (((Boolean)this.break_.getObj()).booleanValue()) continue;
            }
            return true;
        }
        return false;
    }

    private void setObj24(Object object) {
        BlockPos blockPos = (BlockPos)object;
        Iterator iterator = MC.client3.world.getOtherEntities(null, new Box(blockPos)).iterator();
        Object var4_4 = null;
        while (iterator.hasNext()) {
            Entity entity = (Entity)iterator.next();
            if (!(entity instanceof EndCrystalEntity) || !entity.isAlive()) continue;
            MC.client3.interactionManager.attackEntity((PlayerEntity)MC.client3.player, entity);
            MC.client3.player.swingHand(Hand.MAIN_HAND);
            if (null == null) continue;
        }
    }

    private BlockPos m792(Object object) {
        List list = (List)object;
        BlockPos blockPos = null;
        Object var4_4 = null;
        double d = Double.MAX_VALUE;
        Vec3d vec3d = MC.client3.player.getEntityPos();
        for (Object listElem : list) {
            BlockPos blockPos2 = (BlockPos)listElem;
            block4: {
                double d2;
                block3: {
                    d2 = vec3d.distanceTo(Vec3d.ofCenter((Vec3i)blockPos2).add(0.0, -0.5, 0.0));
                    if (blockPos == null) break block3;
                    if (!(d2 < d)) break block4;
                }
                blockPos = blockPos2;
                d = d2;
            }
            if (null == null) continue;
        }
        return blockPos;
    }

    private boolean m718(ItemStack itemStack) {
        BlockItem blockItem;
        block3: {
            block2: {
                Item item = itemStack.getItem();
                Object var2_3 = null;
                if (!(item instanceof BlockItem)) break block2;
                blockItem = (BlockItem)item;
                if (null == null) break block3;
            }
            return false;
        }
        return blockItem.getBlock() == Blocks.OBSIDIAN || (Boolean)this.enderChest.getObj() != false && blockItem.getBlock() == Blocks.ENDER_CHEST;
    }

    private Type getType4() {
        return switch (((RotateMode)((Object)this.rotateMode.getObj())).ordinal()) {
            default -> throw new MatchException(null, null);
            case 2 -> Type.NONE;
            case 1, 4, 5 -> Type.NORMAL;
            case 6 -> Type.type;
            case 0, 3 -> Type.BYPASS;
        };
    }

    private ClientSetting.SwitchMode getSwitchMode3() {
        Object var2_1 = null;
        if (this.switchMode.getObj() == SwitchMode.DEFAULT) {
            return ClientSetting.INSTANCE != null ? (ClientSetting.SwitchMode)((Object)ClientSetting.INSTANCE.switchMode.getObj()) : ClientSetting.SwitchMode.NONE;
        }
        return switch (((SwitchMode)((Object)this.switchMode.getObj())).ordinal()) {
            case 1 -> ClientSetting.SwitchMode.NONE;
            case 2 -> ClientSetting.SwitchMode.NORMAL;
            case 3 -> ClientSetting.SwitchMode.SILENT;
            case 4 -> ClientSetting.SwitchMode.INVENTORY;
            default -> ClientSetting.SwitchMode.NONE;
        };
    }

    @Environment(value=EnvType.CLIENT)
    public static enum SwitchMode {
        DEFAULT,
        NONE,
        NORMAL,
        SILENT,
        INVENTORY;


        private static SwitchMode[] getSwitchModeArray5() {
            return new SwitchMode[]{DEFAULT, NONE, NORMAL, SILENT, INVENTORY};
        }
    }

    @Environment(value=EnvType.CLIENT)
    static enum Type {
        BYPASS,
        NORMAL,
        NONE,
        type;


        private static Type[] getTypeArray() {
            return new Type[]{BYPASS, NORMAL, NONE, type};
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum LagMode {
        Alien,
        CuiCan,
        Smart,
        ToVoid2;


        private static LagMode[] getLagModeArray() {
            return new LagMode[]{Alien, CuiCan, Smart, ToVoid2};
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum RotateMode {
        BYPASS,
        NORMAL,
        NONE,
        DEFAULT,
        SMOOTH,
        ONTICK,
        rotateMode6;


        private static RotateMode[] getRotateModeArray6() {
            return new RotateMode[]{BYPASS, NORMAL, NONE, DEFAULT, SMOOTH, ONTICK, rotateMode6};
        }
    }
}
