package com.chunshui.phit.mikus_vocal_spell.entity.spells;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
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

public abstract class AbstractCheckArea extends Entity implements TraceableEntity{

    /*一个用于为特定区域添加自定义效果的抽象类*/
    protected int spellLevel = 0;
    protected static final int CHECK_INTERVAL = 20;
    protected static final EntityDataAccessor<Float> DETECTION_RADIUS =
            SynchedEntityData.defineId(AbstractCheckArea.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Integer> MAX_LIFE_TIME =
            SynchedEntityData.defineId(AbstractCheckArea.class, EntityDataSerializers.INT);

    @Nullable
    protected Entity owner;
    @Nullable
    private UUID ownerUUID;

    public int duration;
    public int amplifier;
    public boolean isShowIcon;
    public boolean isSpawnParticle;

    public AbstractCheckArea(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.setNoGravity(true);
        setEffectDuration();
        setShowIcon();
        setEffectAmplifier();
        setSpawnParticle();
        setRadius();
        setMaxLifeTime(-1);
    }

    @Override
    public void tick() {
        super.tick();

        if (getMaxLifeTime() > 0 && this.tickCount > getMaxLifeTime()) {
            discardLogic();
            this.discard();
        }

        if (this.tickCount % CHECK_INTERVAL == 0) {
            if (!(this instanceof AbstractWaveRing))
                checkEntitiesInArea();
        }
    }
    /**
     * 检测区域内的实体并施加效果
     */
    protected void checkEntitiesInArea() {
        MikusVocalSpellIronsSpellsAddon.LOGGER.info("AbstractCheckArea.location{}", this.position());
        double radius = getDetectionRadius();
        double radiusSq = radius * radius;

        List<LivingEntity> entities = level().getEntitiesOfClass(
                LivingEntity.class,
                this.getBoundingBox().inflate(radius),
                entity -> entity.distanceToSqr(this) <= radiusSq
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
        setDetectionRadius(1.0F);
    }

    /*销毁逻辑*/
    protected void discardLogic() {}

    /*设置最大生命周期*/
    protected void setMaxLifeTime(int maxLifeTime) {
        this.entityData.set(MAX_LIFE_TIME, maxLifeTime);
    }

    /*设置检测半径*/
    protected void setDetectionRadius(float radius) {
        this.entityData.set(DETECTION_RADIUS, radius);
    }

    /*客户端获取检测半径*/
    public float getDetectionRadius() {
        return this.entityData.get(DETECTION_RADIUS);
    }

    /*客户端获取生命周期*/
    public int getMaxLifeTime() {
        return entityData.get(MAX_LIFE_TIME);
    }

    public void setOwner(@Nullable Entity entity) {
        this.owner = entity;
        if (entity != null) {
            this.ownerUUID = entity.getUUID();
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
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(DETECTION_RADIUS, 1.0F);
        builder.define(MAX_LIFE_TIME, -1);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {}

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {}

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 64 * 64;
    }

    @Override
    public boolean shouldBeSaved(){ return false; }
}