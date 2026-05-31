package com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt;

import com.chunshui.phit.mikus_vocal_spell.entity.spells.AbstractWaveRing;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEntityRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class CoreMeltRing extends AbstractWaveRing{
    public CoreMeltRing(EntityType<?> entityType, Level level) { super(entityType, level); }

    public CoreMeltRing(Level level, Entity owner, int spellLevel) {
        super(MVSEntityRegistry.CORE_MELT_RING.get(), level);
        setMaxLifeTime(400);
        setDetectionRadius(8.0F);
        setExpansionSpeed(0.4F);
        setOwner(owner);
        this.spellLevel = spellLevel;
        owner.getPersistentData().putBoolean(NBTKeyHelper.CORE_MELT_SPAWN, true);
    }

    @Override
    protected void applyEffectToEntity(LivingEntity entity, int duration, int amplifier) {
    }

    @Override
    protected void discardLogic() {
        if (this.owner != null) {
            this.owner.getPersistentData().putBoolean(NBTKeyHelper.CORE_MELT_SPAWN, false);
        }
    }

    @Override
    protected void setShowIcon() { this.isShowIcon = true; }

    @Override
    protected void setEffectAmplifier() { this.amplifier = spellLevel - 1; }
}
