package com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt;

import com.chunshui.phit.mikus_vocal_spell.entity.spells.AbstractAreaEffectCloud;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEntityRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class CMRAreaEffectCloud extends AbstractAreaEffectCloud {
    public CMRAreaEffectCloud(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public CMRAreaEffectCloud(Level level, int spellLevel) {
        super(MVSEntityRegistry.CMR_AREA_EFFECT_CLOUD.get(), level);
        this.setMaxLifeTime(20);
        this.spellLevel = spellLevel;
    }

    @Override
    public void tick() {
        super.tick();
        aabb = this.getBoundingBox().inflate(tickCount * 0.4);
        checkEntitiesInArea();
        if (tickCount >= 20)
            discard();
//        MikusVocalSpellIronsSpellsAddon.LOGGER.debug("CMRAreaEffectCloud.tick():{}Side:{}", this.position(), level().isClientSide);
    }

    @Override
    protected void addEffect(LivingEntity entity) {
        boolean flag = entity.getPersistentData().getBoolean(NBTKeyHelper.AREA_EFFECT_FLAG);
        if (!flag) {
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 400, spellLevel - 1, false, true));
            entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 400, spellLevel - 1, false, true));
            entity.getPersistentData().putBoolean(NBTKeyHelper.AREA_EFFECT_FLAG, true);
        }
    }
    @Override
    protected void setAmplifier() {}

    @Override
    public void setEffectDuration() {
        this.duration = 400;
    }
}
