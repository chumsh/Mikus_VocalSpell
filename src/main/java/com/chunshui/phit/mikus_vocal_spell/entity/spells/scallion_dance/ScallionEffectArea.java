package com.chunshui.phit.mikus_vocal_spell.entity.spells.scallion_dance;

import com.chunshui.phit.mikus_vocal_spell.entity.spells.AbstractCheckArea;
import com.chunshui.phit.mikus_vocal_spell.registries.EntityRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class ScallionEffectArea extends AbstractCheckArea {

    public static final String SPAWN = "scallion_spawned";

    public ScallionEffectArea(EntityType<?> entityType, Level level) { super(entityType, level); }

    public ScallionEffectArea(Level level, Entity owner) {
        super(EntityRegistry.SCALLION_AREA.get(), level);
        setMaxLifeTime(400);
        this.setOwner(owner);

        if (getOwner() != null) {
            getOwner().getPersistentData().putBoolean(SPAWN, true);
        }
    }


    @Override
    protected void applyEffectToEntity(LivingEntity entity, int duration, int amplifier) {
        if(entity instanceof ServerPlayer serverPlayer) {
            serverPlayer.addEffect(new MobEffectInstance(
                    MVSEffectRegistry.SCALLION_EFFECT,
                    duration,
                    amplifier,
                    true,
                    this.isShowIcon
            ));
        }
    }

    @Override
    protected void setRadius() { this.detectionRadius = 10; }

    @Override
    protected void setShowIcon() { this.isShowIcon = true; }


    @Override
    protected void discardLogic() {
        Objects.requireNonNull(getOwner()).getPersistentData().putBoolean(SPAWN, false);
        Objects.requireNonNull(getOwner()).getPersistentData().putInt("casting_count", 0);
    }
}
