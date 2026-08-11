package com.chunshui.phit.mikus_vocal_spell.effects.vocal.core_melt;

import com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt.PrismShardEntity;
import com.chunshui.phit.mikus_vocal_spell.network.SyncInnocenceDataPacket;
import com.chunshui.phit.mikus_vocal_spell.registries.AttachmentRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSSoundRegistry;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.entity.spells.fire_arrow.FireArrowProjectile;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
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
    public void onMobHurt(@NotNull LivingEntity livingEntity, int amplifier, @NotNull DamageSource damageSource, float amount) {
        float currentAmount = livingEntity.getData(AttachmentRegistry.TOTAL_DAMAGE);
        float newAmount = currentAmount + amount;
        livingEntity.setData(AttachmentRegistry.TOTAL_DAMAGE, newAmount);
    }

    @Override
    public void onEffectRemoved(LivingEntity entity, int pAmplifier) {
        if (entity.level().isClientSide)
            return;
        PacketDistributor.sendToAllPlayers(new SyncInnocenceDataPacket(false, 0));
        if (entity.level() instanceof ServerLevel serverLevel) {
            entity.level().playSound(null,  entity.blockPosition(), MVSSoundRegistry.BREAK.get(), entity.getSoundSource(), 0.7F, 1);
            serverLevel.sendParticles(ParticleTypes.WITCH, entity.getX(), entity.getY(), entity.getZ(), 20, 0.5, 0.5, 0.5, 0.5);
        }
        float totalDamage = entity.getData(AttachmentRegistry.TOTAL_DAMAGE);
        if (totalDamage <= 15) {
            Collection<MobEffectInstance> effects = entity.getActiveEffects();
            for (MobEffectInstance effect : effects) {
                if (effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                    entity.removeEffect(effect.getEffect());
                }
            }
        }else {
            Vec3 lookAngle = entity.getLookAngle();
            Vec3 spawnBasicDir = entity.getEyePosition().add(0, -.5, 0);
            PrismShardEntity prismShardEntity1 = new PrismShardEntity(entity.level(), entity);
            FireArrowProjectile fireArrowProjectile = new FireArrowProjectile(entity.level(), entity);
            fireArrowProjectile.setPos(spawnBasicDir);
            fireArrowProjectile.shoot(lookAngle.x, lookAngle.y, lookAngle.z, 0.8F, 0);
            prismShardEntity1.setPos(spawnBasicDir);
            prismShardEntity1.shoot(lookAngle);
            entity.level().addFreshEntity(prismShardEntity1);
            entity.level().addFreshEntity(fireArrowProjectile);
        }
    }
}
