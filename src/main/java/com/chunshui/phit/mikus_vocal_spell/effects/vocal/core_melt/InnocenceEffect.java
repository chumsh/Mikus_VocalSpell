package com.chunshui.phit.mikus_vocal_spell.effects.vocal.core_melt;

import com.chunshui.phit.mikus_vocal_spell.network.SyncInnocenceDataPacket;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Objects;

public class InnocenceEffect extends MagicMobEffect {
    public InnocenceEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int pAmplifier) {
        if (entity.level().isClientSide)
            return true;
        int duration = Objects.requireNonNull(entity.getEffect(MVSEffectRegistry.INNOCENCE_EFFECT)).getDuration();
        if (entity instanceof ServerPlayer serverPlayer)
            PacketDistributor.sendToPlayer(serverPlayer, new SyncInnocenceDataPacket(true, duration));
        return true;
    }
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
    @Override
    public void onEffectRemoved(LivingEntity entity, int pAmplifier) {
        if (entity instanceof ServerPlayer serverPlayer)
            PacketDistributor.sendToPlayer(serverPlayer, new SyncInnocenceDataPacket(false, 0));
    }
}
