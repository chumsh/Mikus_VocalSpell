package com.chunshui.phit.mikus_vocal_spell.effects.vocal.mana_munch_melody;

import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class MelodyEffect extends MagicMobEffect {
    public MelodyEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
        livingEntity.getPersistentData().putBoolean(NBTKeyHelper.HAS_MELODY, true);
    }

    @Override
    public void onEffectRemoved(LivingEntity livingEntity, int amplifier) {
        livingEntity.getPersistentData().putBoolean(NBTKeyHelper.HAS_MELODY, false);
    }

}
