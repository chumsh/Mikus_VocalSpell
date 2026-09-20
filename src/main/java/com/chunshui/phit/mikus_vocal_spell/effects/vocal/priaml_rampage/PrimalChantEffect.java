package com.chunshui.phit.mikus_vocal_spell.effects.vocal.priaml_rampage;

import com.chunshui.phit.mikus_vocal_spell.event.spells.primal_rampage.CancelCooldown;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class PrimalChantEffect extends MagicMobEffect {
    public PrimalChantEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
        super.onEffectAdded(livingEntity, amplifier);
        if (!(livingEntity instanceof Player player)) return;
        Objects.requireNonNull(player.getAttribute(AttributeRegistry.CAST_TIME_REDUCTION)).removeModifier(CancelCooldown.ID);
    }

    @Override
    public void onEffectRemoved(LivingEntity livingEntity, int pAmplifier) {
        super.onEffectRemoved(livingEntity, pAmplifier);
        if (!(livingEntity instanceof Player player)) return;
        Objects.requireNonNull(player.getAttribute(AttributeRegistry.CAST_TIME_REDUCTION)).removeModifier(CancelCooldown.ID);
    }

}
