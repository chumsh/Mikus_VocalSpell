package com.chunshui.phit.mikus_vocal_spell.entity.spells;

import com.chunshui.phit.mikus_vocal_spell.registries.MVSEntityRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class NoneCheckArea extends AbstractCheckArea{
    private MobEffectInstance  effectInstance;
    private MobEffectInstance effectInstance1;
    private MobEffectInstance effectInstance2;
    private int counter = 0;

    public NoneCheckArea(Level level, MobEffectInstance effectInstance, MobEffectInstance effectInstance1,  MobEffectInstance effectInstance2) {
        super(MVSEntityRegistry.NONE_CHECK_AREA.get(), level);
        setMaxLifeTime(200);
        setDetectionRadius(8.0F);
        this.effectInstance = effectInstance;
        this.effectInstance1 = effectInstance1;
        this.effectInstance2 = effectInstance2;
    }

    public NoneCheckArea(Level level, MobEffectInstance effectInstance) {
        super(MVSEntityRegistry.NONE_CHECK_AREA.get(), level);
        setMaxLifeTime(200);
        setDetectionRadius(8.0F);
        this.effectInstance = effectInstance;
    }

    public NoneCheckArea(Level level, MobEffectInstance effectInstance, MobEffectInstance effectInstance1) {
        super(MVSEntityRegistry.NONE_CHECK_AREA.get(), level);
        setMaxLifeTime(duration);
        setDetectionRadius(8.0F);
        this.effectInstance = effectInstance;
        this.effectInstance1 = effectInstance1;
    }

    public NoneCheckArea(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void applyEffectToEntity(LivingEntity entity, int duration, int amplifier) {
        counter++;
        if(counter != 0)
            return;
        entity.addEffect(new MobEffectInstance(
                    effectInstance.getEffect(),
                    duration,
                    amplifier
        ));
        entity.addEffect(new MobEffectInstance(
                effectInstance1.getEffect(),
                duration,
                amplifier
        ));
        entity.addEffect(new MobEffectInstance(
                effectInstance2.getEffect(),
                duration,
                amplifier
        ));
    }
    @Override
    public void setRadius() {
        setDetectionRadius(12.0F);
    }

   public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setAmplifier(int amplifier) {
        this.amplifier = amplifier;
    }
}
