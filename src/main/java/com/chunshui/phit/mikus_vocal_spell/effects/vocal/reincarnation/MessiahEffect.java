package com.chunshui.phit.mikus_vocal_spell.effects.vocal.reincarnation;

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

    int duration;
    public MessiahEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {

        if (entity instanceof Player player && entity.level().isClientSide) {
            if (player.getPersistentData().getBoolean("messiah_cancel_revive")) {
                player.displayClientMessage(Component.translatable("message.mikus_vocal_spell.messiah_effect.effect").withColor(16711680),
                        true
                );
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        this.duration = duration;
        return duration % 20 == 0;
    }

    @Override
    public void onMobHurt(@NotNull LivingEntity livingEntity, int amplifier, @NotNull DamageSource damageSource, float amount) {
        if(livingEntity instanceof ServerPlayer serverPlayer) {
            boolean messiahCanceled = ReviveCapabilityManager.getReviveCapability(serverPlayer).hasCanceled();
           // MikusVocalSpellIronsSpellsAddon.LOGGER.info("OnMobHurt's messiahCanceled: {}",messiahCanceled);
            double randomChance = serverPlayer.level().getRandom().nextDouble();
            if(!messiahCanceled && randomChance < 0.25) {
                ReviveCapabilityManager.getReviveCapability(serverPlayer).ensureCanceled(true);
            }

        }
    }
}