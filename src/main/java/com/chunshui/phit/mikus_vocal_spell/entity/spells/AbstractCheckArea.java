package com.chunshui.phit.mikus_vocal_spell.entity.spells;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public abstract class AbstractCheckArea extends Entity implements TraceableEntity {

    /*一个用于为特定区域添加自定义效果的抽象类*/
    private static final int CHECK_INTERVAL = 10;
    @Nullable
    protected Entity owner;
    @Nullable
    private UUID ownerUUID;

    private int tickCounter = 0;
    public int duration;
    public int amplifier;
    public boolean isShowIcon;
    public boolean isSpawnParticle;
    protected final EntityDataAccesstor<Double> detectionRadius;
    protected final EntityDataAccesstor<Integer> maxLifeTime = -1;

    SynchedEntityData.defineId(AbstractCheckArea.class, EntityDataSerializers.INT);
SynchedEntityData.defineId(AbstractCheckArea.class, EntityDataSerializers.DOUBLE);

    public AbstractCheckArea(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.setNoGravity(true);
        setEffectDuration();
        setShowIcon();
        setEffectAmplifier();
        setSpawnParticle();
        setRadius();
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            return;
        }

        tickCounter++;
        if (tickCounter >= CHECK_INTERVAL) {
            tickCounter = 0;
            checkEntitiesInArea();
        }
        if (maxLifeTime > 0 && this.tickCount > maxLifeTime) {
            discardLogic();
            this.discard();
        }
    }

    /**
     * 检测区域内的实体并施加效果
     */
    private void checkEntitiesInArea() {

        double RadiusSq = detectionRadius * detectionRadius;

        List<LivingEntity> entities = level().getEntitiesOfClass(
                LivingEntity.class,
                this.getBoundingBox().inflate(detectionRadius),
                entity -> entity.distanceToSqr(this) <= RadiusSq
        );

        for (LivingEntity entity : entities) {
            applyEffectToEntity(entity, duration, amplifier);
        }
    }

    protected abstract void applyEffectToEntity(LivingEntity entity, int duration, int amplifier);

    /*获取效果时间*/
    protected void setEffectDuration() {
        this.duration = 20;
    }

    /* 获取效果等级 */
    protected void setEffectAmplifier() {
        this.amplifier = 0;
    }

    /*是否显示效果图标*/
    protected void setShowIcon() {
        this.isShowIcon = false;
    }

    protected void setSpawnParticle() {
        this.isSpawnParticle = false;
    }

    protected void setRadius() {
        this.detectionRadius = 1;
    }

    protected void discardLogic() {}

    public void setMaxLifeTime(int maxLifeTime) {
        this.maxLifeTime = maxLifeTime;
    }

    public void setOwner(@Nullable Entity entity) {
        this.owner = entity;
        if (entity != null) {
            this.ownerUUID = entity.getUUID();
            MikusVocalSpellIronsSpellsAddon.LOGGER.info(
                    "setOwner called: owner={}, UUID={}, areaUUID={}",
                    entity.getName().getString(),
                    this.ownerUUID,
                    this.getUUID()
            );
        } else {
            MikusVocalSpellIronsSpellsAddon.LOGGER.warn("setOwner called with null entity for area {}", this.getUUID());
        }
    }

    @Override
    public @Nullable Entity getOwner() {
        Entity result = null;
        
        if (this.owner != null && !this.owner.isRemoved()) {
            result = this.owner;
        } else if (this.ownerUUID != null && this.level() instanceof ServerLevel serverlevel) {
            Entity restoredOwner = serverlevel.getEntity(this.ownerUUID);
            if (restoredOwner != null) {
                this.owner = restoredOwner;
                result = restoredOwner;
                MikusVocalSpellIronsSpellsAddon.LOGGER.info(
                    "Restored owner from UUID: {}",
                    restoredOwner.getName().getString()
                );
            } else {
                MikusVocalSpellIronsSpellsAddon.LOGGER.warn(
                    "Failed to restore owner from UUID {} for area {}",
                    ownerUUID,
                    this.getUUID()
                );
            }
        } else {
            if (this.owner != null) {
                MikusVocalSpellIronsSpellsAddon.LOGGER.warn(
                    "Owner exists but is removed: {}",
                    this.owner.getName().getString()
                );
            }
        }
        return result;
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {}

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {}

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {}  
}
