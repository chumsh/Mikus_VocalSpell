package com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt;

import com.chunshui.phit.mikus_vocal_spell.registries.MVSEntityRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSSoundRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.AnimationHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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

public class CoreMeltEntity extends Entity implements GeoEntity{
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Integer> LIFE_TIME =
            SynchedEntityData.defineId(CoreMeltEntity.class, EntityDataSerializers.INT);

    public CoreMeltEntity(EntityType<?> entityType, Level level) { super(entityType, level); }

    public CoreMeltEntity(Level level) {
       super(MVSEntityRegistry.CORE_MELT.get(), level);
       this.noPhysics = true;
       this.isNoGravity();
    }


    @Override
    public void tick() {
        super.tick();

        if(!level().isClientSide) {
            if(this.tickCount >= entityData.get(LIFE_TIME)) {
                if (level() instanceof ServerLevel serverLevel)
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, this.position().x, this.position().y + 0.5, this.position().z, 50, 0.3, 0.3, 0.3, 0.3);
                discard();
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(LIFE_TIME, 400);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {}

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {}

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Start", state -> {
            if (this.tickCount < 10)
                return state.setAndContinue(AnimationHelper.CM_START);

            return PlayState.STOP;
        }).setSoundKeyframeHandler(state -> {
            Player player = ClientUtil.getClientPlayer();

            if(!(player == null)) {
                player.playSound(MVSSoundRegistry.FALL_DOWN.get(), 5, 1);
            }
        }));

        controllers.add(new AnimationController<>(this, "End", state -> {
            if (entityData.get(LIFE_TIME) - this.tickCount <= 0.08 * 20)
                return state.setAndContinue(AnimationHelper.CM_END);

            return PlayState.STOP;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public boolean shouldBeSaved() { return false; }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance > 2 && distance < 64 * 64;
    }

}
