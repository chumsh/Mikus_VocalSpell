package com.chunshui.phit.mikus_vocal_spell.entity.spells.scallion_dance;

import com.chunshui.phit.mikus_vocal_spell.entity.spells.AbstractCheckArea;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEntityRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ScallionEffectArea extends AbstractCheckArea {
    public ScallionEffectArea(EntityType<?> entityType, Level level) { super(entityType, level); }

    public ScallionEffectArea(Level level, Entity owner, int spellLevel) {
        super(MVSEntityRegistry.SCALLION_AREA.get(), level);
        setMaxLifeTime(400);
        setDetectionRadius(7.0F);
        this.setOwner(owner);
        this.spellLevel = spellLevel;
        owner.getPersistentData().putBoolean(NBTKeyHelper.SCALLION_SPAWN, true);
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
    protected void setShowIcon() { this.isShowIcon = true; }

    @Override
    protected void setEffectAmplifier() { this.amplifier = spellLevel - 1; }

    @Override
    public void discardLogic() {
        if (this.owner != null) {
            this.owner.getPersistentData().putBoolean(NBTKeyHelper.SCALLION_SPAWN, false);
        }
    }
}