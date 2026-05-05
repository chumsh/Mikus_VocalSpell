package com.chunshui.phit.mikus_vocal_spell.entity.spells;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.neoforged.neoforge.entity.PartEntity;
import org.jetbrains.annotations.NotNull;

public class ConePart extends PartEntity<AbstractGroundProjectile> {

    public final AbstractGroundProjectile parentEntity;
    public final String name;
    private final EntityDimensions size;

    public ConePart(AbstractGroundProjectile parent, String name, float scaleX, float scaleY) {
        super(parent);
        this.size = EntityDimensions.scalable(scaleX, scaleY);
        this.refreshDimensions();
        this.name = name;
        this.parentEntity = parent;

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {

    }

    @Override
    public boolean is(@NotNull Entity entity) {
        return this == entity || this.parentEntity == entity;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.size;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }
}
