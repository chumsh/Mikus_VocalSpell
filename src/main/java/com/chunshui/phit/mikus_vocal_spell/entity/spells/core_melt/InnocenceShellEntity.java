package com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt;

import com.chunshui.phit.mikus_vocal_spell.registries.MVSEntityRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSSoundRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.AnimationHelper;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.ClientUtil;
import software.bernie.geckolib.util.GeckoLibUtil;


public class InnocenceShellEntity extends Entity implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public InnocenceShellEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    static final EntityDataAccessor<Integer> LIFE_TIME =
            SynchedEntityData.defineId(InnocenceShellEntity.class, EntityDataSerializers.INT);

    public InnocenceShellEntity(Level level) {
        super(MVSEntityRegistry.INNOCENCE_SHELL.get(), level);
        this.noPhysics = true;
        this.isNoGravity();
    }

    @Override
    public void tick() {
        if (this.entityData.get(LIFE_TIME) - this.tickCount <= 0) {
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(LIFE_TIME, 200);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {}

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {}

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        Player player = ClientUtil.getClientPlayer();
        controllers.add(new AnimationController<>(this, "Rotate", state -> {
            int duration = player.getPersistentData().getInt(NBTKeyHelper.INNOCENCE_DURATION);
            if (duration > 30)
                return state.setAndContinue(AnimationHelper.IN_ROTATE);
            return PlayState.STOP;
        }));
        controllers.add(new AnimationController<>(this, "Explosion", state -> {
            int duration = player.getPersistentData().getInt(NBTKeyHelper.INNOCENCE_DURATION);
            if (duration <= 30)
                return state.setAndContinue(AnimationHelper.IN_EXPLOSION);
            return PlayState.STOP;
        }).setSoundKeyframeHandler(soundHandler -> player.playSound(MVSSoundRegistry.BREAK.get(), 5, 1)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return geoCache; }

    @Override
    public boolean shouldBeSaved() { return false; }
}
