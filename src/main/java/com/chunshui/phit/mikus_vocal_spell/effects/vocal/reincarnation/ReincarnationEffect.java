package com.chunshui.phit.mikus_vocal_spell.effects.vocal.reincarnation;

import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.server.component.generated.ReviveCapability;
import com.chunshui.phit.mikus_vocal_spell.server.component.generated.ReviveCapabilityManager;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;

public class ReincarnationEffect extends MagicMobEffect {
    public ReincarnationEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    int duration;

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof ServerPlayer player) {
            ReviveCapability capability = ReviveCapabilityManager.getReviveCapability(player);
            Holder<MobEffect> effectHolder =
                    MVSEffectRegistry.REVIVE_BUFF;

            if (capability.getCharges() <= 0) {
                player.removeEffect(effectHolder);
                return false;
            }
            int remainingDuration = Objects.requireNonNull(player.getEffect(effectHolder)).getDuration();

            if(capability.getCharges() == 2){
                switch (amplifier){
                    case 0 ->
                        player.addEffect(new MobEffectInstance(MVSEffectRegistry.PERFORMER_EFFECT, remainingDuration, 0, false, true));

                    case 1 ->
                        player.addEffect(new MobEffectInstance(MVSEffectRegistry.INVENTOR_EFFECT, remainingDuration, 0, false, true));

                    case 2 ->
                        player.addEffect(new MobEffectInstance(MVSEffectRegistry.MESSIAH_EFFECT, remainingDuration, 0, false, true));
                    case 3 ->
                        player.addEffect(new MobEffectInstance(MVSEffectRegistry.REVOLUTIONARY_EFFECT, remainingDuration, 0, false, true));
                    case 4 ->
                        player.addEffect(new MobEffectInstance(MVSEffectRegistry.ADVENTURER_EFFECT, remainingDuration, 0, false, true));
                }
            }
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        this.duration = duration;
        return duration % 10 == 0 || duration <= 5;
    }

    @Override
    public void onEffectRemoved(LivingEntity livingEntity, int amplifier){
        if(livingEntity instanceof ServerPlayer player){
            ReviveCapabilityManager.refreshRevive(player);
            
            if (!player.level().isClientSide) {
                player.getServer().execute(() -> {
                    player.removeEffect(MVSEffectRegistry.PERFORMER_EFFECT);
                    player.removeEffect(MVSEffectRegistry.INVENTOR_EFFECT);
                    player.removeEffect(MVSEffectRegistry.MESSIAH_EFFECT);
                    player.removeEffect(MVSEffectRegistry.REVOLUTIONARY_EFFECT);
                    player.removeEffect(MVSEffectRegistry.ADVENTURER_EFFECT);
                });
            }

        }
    }
}