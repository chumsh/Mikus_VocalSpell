package com.chunshui.phit.mikus_vocal_spell.effects.vocal.reincarnation;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class AdventurerEffect extends MobEffect {
    public AdventurerEffect(MobEffectCategory category, int color) {
        super(category, color);
    }


    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player && entity.level().isClientSide()) {
            player.displayClientMessage(
                    net.minecraft.network.chat.Component.translatable("message.mikus_vocal_spell.adventurer_effect.effect").withColor(16711680),
                    true
            );
        }
        if(entity instanceof ServerPlayer serverPlayer) {
            float damagePercent = 0.3f;
            DamageSource damageSource = serverPlayer.damageSources().magic();
            ServerLevel serverLevel = serverPlayer.serverLevel();

            List<Player> nearbyPlayers = serverLevel.getEntitiesOfClass(
                    Player.class,
                    entity.getBoundingBox().inflate(8.0),
                    p -> p != null && p.isAlive()
            );

            for (LivingEntity target : nearbyPlayers) {
                float damageAmount = target.getMaxHealth() * damagePercent;
                target.hurt(damageSource, damageAmount);
            }
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration >= 20 && duration % 40 == 0;
    }
}