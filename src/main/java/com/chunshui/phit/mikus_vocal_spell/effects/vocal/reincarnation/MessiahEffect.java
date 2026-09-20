package com.chunshui.phit.mikus_vocal_spell.effects.vocal.reincarnation;

import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.server.component.generated.ReviveCapabilityManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class MessiahEffect extends MobEffect {
    int sendTime = 0;
    public MessiahEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player clientPlayer && livingEntity.level().isClientSide) {
            sendTime++;
            if (sendTime > 1) return true;
            clientPlayer.displayClientMessage(Component.translatable("message.mikus_vocal_spell.revolutionary_effect.effect").withColor(16711680),
                    true
            );
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onMobHurt(@NotNull LivingEntity livingEntity, int amplifier, @NotNull DamageSource damageSource, float amount) {
        if(livingEntity instanceof ServerPlayer serverPlayer) {
            if (!serverPlayer.hasEffect(MVSEffectRegistry.MESSIAH_EFFECT)) return;
            boolean messiahCanceled = ReviveCapabilityManager.getReviveCapability(serverPlayer).hasCanceled();
            double randomChance = serverPlayer.level().getRandom().nextDouble();
            if(!messiahCanceled && randomChance <= 1) {
                ReviveCapabilityManager.getReviveCapability(serverPlayer).ensureCanceled(true);
            }

        }
    }
}