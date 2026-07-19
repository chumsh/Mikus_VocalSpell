package com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.entity.spells.AbstractWaveRing;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEntityRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;


public class CoreMeltRing extends AbstractWaveRing{
    private static final int MAX_lIFE_TIME = 400;
    public CoreMeltRing(EntityType<?> entityType, Level level) { super(entityType, level); }
    public CoreMeltRing(Level level, Entity owner, int spellLevel) {
        super(MVSEntityRegistry.CORE_MELT_RING.get(), level);
        setMaxLifeTime(400);
        setDetectionRadius(8.0F);
        setExpansionSpeed(0.4F);
        setOwner(owner);
        setCooldownTime(20);
        this.spellLevel = spellLevel;
        owner.getPersistentData().putBoolean(NBTKeyHelper.CORE_MELT_SPAWN, true);
    }

    public static int getLocalMaxLifeTime() { return MAX_lIFE_TIME; }

    @Override
    protected void applyEffectToEntity(LivingEntity entity, int duration, int amplifier) {
//        if (! (entity instanceof Player)) {
            entity.addEffect(new MobEffectInstance(
                    MobEffects.WEAKNESS,
                    duration,
                    amplifier));
            entity.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN,
                    duration,
                    amplifier));
            entity.addEffect(new MobEffectInstance(
                    MobEffects.POISON,
                    duration,
                    amplifier));
//        }
    }

    @Override
    public void tick() {
        super.tick();

        CMRAreaEffectCloud.timeCounter = tickCount;

        if(level().isClientSide)
            return;

        if (tickCount % this.cooldownTime == 0) {
            CMRAreaEffectCloud cmrAreaEffectCloud = new CMRAreaEffectCloud(level(), spellLevel);
            cmrAreaEffectCloud.setPos(this.position());
            if (owner != null) {
                cmrAreaEffectCloud.setMaster(owner.getUUID());
            }
            level().addFreshEntity(cmrAreaEffectCloud);
        }
    }
    @Override
    public void discardLogic() {
        if (this.owner != null) {
            this.owner.getPersistentData().putBoolean(NBTKeyHelper.CORE_MELT_SPAWN, false);
            CMRAreaEffectCloud.timeCounter = 0;
        }else
            MikusVocalSpellIronsSpellsAddon.LOGGER.warn("CoreMeltRingHasNoOwner");
    }

    @Override
    protected void setShowIcon() { this.isShowIcon = true; }

    @Override
    protected void setEffectDuration() {
        this.duration = this.getMaxLifeTime();
    }

    @Override
    protected void setEffectAmplifier() { this.amplifier = spellLevel - 1; }
}
