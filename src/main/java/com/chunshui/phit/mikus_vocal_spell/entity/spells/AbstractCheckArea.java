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

public abstract class AbstractCheckArea extends Entity implements TraceableEntity {

    private static final int CHECK_INTERVAL = 10;
    private static final EntityDataAccessor<Double> DETECTION_RADIUS = SynchedEntityData.defineId(AbstractCheckArea.class, EntityDataSerializers.DOUBLE);
    private static final EntityDataAccessor<Integer> MAX_LIFE_TIME = SynchedEntityData.defineId(AbstractCheckArea.class, EntityDataSerializers.INT);

    @Nullable
    protected Entity owner;
    @Nullable
    private UUID ownerUUID;

    private int tickCounter = 0;
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
        setMaxLifeTime( -1 );
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

        if (getMaxLifeTime() > 0 && this.tickCount > getMaxLifeTime()) {
            discardLogic();
            this.discard();
        }
    }

    private void checkEntitiesInArea() {
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

    protected void setEffectDuration() {
        this.duration = 20;
    }

    protected void setEffectAmplifier() {
        this.amplifier = 0;
    }

    protected void setShowIcon() {
        this.isShowIcon = false;
    }

    protected void setSpawnParticle() {
        this.isSpawnParticle = false;
    }

    protected void setRadius() {
        setDetectionRadius(1.0D);
    }

    protected void discardLogic() {}

    public void setMaxLifeTime(int maxLifeTime) {
        this.entityData.set(MAX_LIFE_TIME, maxLifeTime);
    }

    public int getMaxLifeTime() {
        return this.entityData.get(MAX_LIFE_TIME);
    }

    public void setDetectionRadius(double radius) {
        this.entityData.set(DETECTION_RADIUS, radius);
    }

    public double getDetectionRadius() {
        return this.entityData.get(DETECTION_RADIUS);
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
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(DETECTION_RADIUS, 1.0D);
        builder.define(MAX_LIFE_TIME, -1);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {
        if (compound.contains("DetectionRadius")) {
            setDetectionRadius(compound.getDouble("DetectionRadius"));
        }
        if (compound.contains("MaxLifeTime")) {
            setMaxLifeTime(compound.getInt("MaxLifeTime"));
        }
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {
        compound.putDouble("DetectionRadius", getDetectionRadius());
        compound.putInt("MaxLifeTime", getMaxLifeTime());
    }
}
