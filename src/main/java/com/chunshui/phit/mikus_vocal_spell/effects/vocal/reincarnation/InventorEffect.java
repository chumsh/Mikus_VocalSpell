package com.chunshui.phit.mikus_vocal_spell.effects.vocal.reincarnation;

import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class InventorEffect extends MobEffect {
    int duration;

    public InventorEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player && entity.level().isClientSide()) {
            if (duration > (amplifier + 1) * 200 - 80) {
                player.displayClientMessage(
                        net.minecraft.network.chat.Component.translatable("message.mikus_vocal_spell.inventor_effect.effect").withColor(16711680),
                        true
                );
            }
        }
        if(entity instanceof ServerPlayer player){
                Set<UUID> summonUUIDs = SummonManager.getSummons(player);
                ServerLevel serverLevel = player.serverLevel();
                Set<LivingEntity> livingSummons = summonUUIDs.stream()
                        .map(serverLevel::getEntity)
                        .filter(summonEntity -> summonEntity instanceof LivingEntity)
                        .map(summonEntity -> (LivingEntity) summonEntity)
                        .collect(Collectors.toSet());
                float damagePercent = 0.1F;

                DamageSource damageSource = entity.damageSources().magic();

                List<LivingEntity> nearbyEntities = serverLevel.getEntitiesOfClass(
                        LivingEntity.class,
                        entity.getBoundingBox().inflate(8.0),
                        e -> e != null && e.isAlive()
                );

                Set<LivingEntity> handledEntity = new HashSet<>(livingSummons);
                handledEntity.retainAll(nearbyEntities);

                List<Player> nearbyPlayers = serverLevel.getEntitiesOfClass(
                        Player.class,
                        entity.getBoundingBox().inflate(8.0),
                        p -> p != null && p.isAlive()
                );

                Set<LivingEntity> allTargets = new HashSet<>(handledEntity);
                allTargets.addAll(nearbyPlayers);

                for (LivingEntity target : allTargets) {
                    float damageAmount = target.getMaxHealth() * damagePercent;
                    target.hurt(damageSource, damageAmount);
                }
            }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        this.duration = duration;
        return duration >= 20 && duration % 40 == 0;
    }
}

