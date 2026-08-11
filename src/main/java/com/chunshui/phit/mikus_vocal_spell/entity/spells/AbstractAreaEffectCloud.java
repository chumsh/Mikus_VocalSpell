package com.chunshui.phit.mikus_vocal_spell.entity.spells;

import com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt.CoreMeltRing;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public abstract class AbstractAreaEffectCloud extends Entity {
    public int timeCounter;
    protected Entity master;
    protected AABB aabb;
    protected int spellLevel;
    protected int duration;
    protected int amplifier;
    protected final int INTERVAL = 20;
    public AbstractAreaEffectCloud(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    protected static final EntityDataAccessor<Integer> MAX_LIFE_TIME =
            SynchedEntityData.defineId(AbstractAreaEffectCloud.class, EntityDataSerializers.INT);

    public void checkEntitiesInArea() {
        List<LivingEntity> entities;
        entities = level().getEntitiesOfClass(LivingEntity.class, aabb);
        if (entities.isEmpty())
            return;
        for (LivingEntity entity : entities) {
            if (!entity.getPersistentData().getBoolean(NBTKeyHelper.AREA_EFFECT_FLAG))
                applyEffectToEntity(entity);
            if (CoreMeltRing.getLocalMaxLifeTime() - timeCounter < 20) {
                entity.getPersistentData().putBoolean(NBTKeyHelper.AREA_EFFECT_FLAG, false);
            }
        }
    }
    protected abstract void setEffectDuration();

    protected void setMaxLifeTime(int maxLifeTime) {
        entityData.set(MAX_LIFE_TIME, maxLifeTime);
    }

    protected  abstract void setAmplifier();

    protected void applyEffectToEntity(Entity entity) {
        LivingEntity livingEntity;
        if (entity instanceof LivingEntity)
            livingEntity = (LivingEntity) entity;
        else
            return;
        if (isPvP()) {
            if (level().isClientSide)
                return;
            if (entity != master) {
                addEffect(livingEntity);
            }
        }else if (master instanceof Player && ! (entity instanceof Player)){
                addEffect(livingEntity);
        }else if (!(entity instanceof Player))
            addEffect(livingEntity);
        else
            addEffect(livingEntity);
        }
    protected boolean isPvP() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null)
            return false;
        return server.isPvpAllowed();
    }

    public void setMaster(UUID master) {
        if (level().isClientSide)
            return;
        if(level() instanceof ServerLevel level)
            this.master = level.getEntity(master);
    }

    protected abstract void addEffect(LivingEntity entity);

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(MAX_LIFE_TIME, -1);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {}

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {}
    @Override
    public boolean shouldBeSaved(){ return false; }
}