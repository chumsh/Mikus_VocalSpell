package com.chunshui.phit.mikus_vocal_spell.effects.vocal.reincarnation;


import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class PerformerEffect extends MobEffect {
    int sendTime;

    public PerformerEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player clientPlayer && livingEntity.level().isClientSide) {
        sendTime++;
            if (sendTime > 1) return true;
            clientPlayer.displayClientMessage(Component.translatable("message.mikus_vocal_spell.performer_effect.effect").withColor(16711680),
                    true
            );
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
