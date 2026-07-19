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
        if (tickCount % INTERVAL == 0) {
            aabb = this.getBoundingBox().inflate(4);
            checkEntitiesInArea();

            discard();
        }

//        MikusVocalSpellIronsSpellsAddon.LOGGER.debug("CMRAreaEffectCloud.tick():{}Side:{}", this.position(), level().isClientSide);
    }

    @Override
    protected void addEffect(LivingEntity entity) {
        boolean flag = entity.getPersistentData().getBoolean(NBTKeyHelper.AREA_EFFECT_FLAG);
        if (flag)
            entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 400, spellLevel, false, true));
        entity.getPersistentData().putBoolean(NBTKeyHelper.AREA_EFFECT_FLAG, true);
    }

    @Override
    protected void setAmplifier() {}

    @Override
    public void setEffectDuration() {
        this.duration = 400;
    }
}
