package com.chunshui.phit.mikus_vocal_spell.effects.vocal.reincarnation;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class RevolutionaryEffect extends MobEffect {
    public RevolutionaryEffect(MobEffectCategory category, int color) {
        super(category, color);

        this.addAttributeModifier(AttributeRegistry.CAST_TIME_REDUCTION, MikusVocalSpellIronsSpellsAddon.id("effect_fast"), -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, MikusVocalSpellIronsSpellsAddon.id("effect_slow"), -0.80, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(AttributeRegistry.COOLDOWN_REDUCTION, MikusVocalSpellIronsSpellsAddon.id("effect_extend"), -0.90, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if(entity instanceof Player Player && entity.level().isClientSide) {
            Player.displayClientMessage(Component.translatable("message.mikus_vocal_spell.revolutionary_effect.effect").withColor(16711680), true);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration > 200 * (amplifier + 1) - 80;
    }
}
