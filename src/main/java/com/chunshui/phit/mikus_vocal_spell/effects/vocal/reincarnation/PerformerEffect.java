package com.chunshui.phit.mikus_vocal_spell.effects.vocal.reincarnation;


import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PerformerEffect extends MobEffect {

    public PerformerEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player && entity.level().isClientSide()) {
            player.displayClientMessage(
                net.minecraft.network.chat.Component.translatable("message.mikus_vocal_spell.performer_effect.effect").withColor(16711680),
                true
            );
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration > 200 * (amplifier + 1) - 80;
    }
}
