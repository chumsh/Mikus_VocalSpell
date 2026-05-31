package com.chunshui.phit.mikus_vocal_spell.entity.spells;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractWaveRing extends AbstractCheckArea{
    private float expansionSpeed = 0.5F;

    private static final EntityDataAccessor<Float> EXPANSION_SPEED =
            SynchedEntityData.defineId(AbstractWaveRing.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> CREATION_TICK =
            SynchedEntityData.defineId(AbstractWaveRing.class, EntityDataSerializers.LONG);

    public AbstractWaveRing(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

        if (!level().isClientSide) {
            if (key == null) {
                entityData.set(EXPANSION_SPEED, getExpansionSpeed());
                entityData.set(CREATION_TICK, level().getGameTime());
            }
        }
    }

    public float getRadius(float particleTicks) { return calculateCurrentRadius(particleTicks); }
    
    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(EXPANSION_SPEED, 0.5F);
        builder.define(CREATION_TICK, 0L);
    }

    private float calculateCurrentRadius(float particleTicks) {
        float speed = entityData.get(EXPANSION_SPEED);
        float origin = getOriginalRadius();
        float max = entityData.get(DETECTION_RADIUS);
        float age = this.tickCount + particleTicks;
        int eachTimeCost = (int) (max / speed);
        int time = (int) (age / eachTimeCost);

        return origin + speed * (age -  time * eachTimeCost);
    }

    public float getOriginalRadius() { return 0; }

    public float getExpansionSpeed() { return expansionSpeed; }

    public void setExpansionSpeed(float expansionSpeed) { this.expansionSpeed = expansionSpeed; }
}
