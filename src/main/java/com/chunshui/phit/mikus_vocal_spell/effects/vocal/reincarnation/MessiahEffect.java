package com.chunshui.phit.mikus_vocal_spell.effects.vocal.reincarnation;

import com.chunshui.phit.mikus_vocal_spell.registries.AttachmentRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.MVSUtils;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class MessiahEffect extends MagicMobEffect {
    public MessiahEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player clientPlayer && livingEntity.level().isClientSide) {
            boolean shouldSend = clientPlayer.getData(AttachmentRegistry.SHOULD_SEND_MESSAGE);
            if (shouldSend) {
                boolean flag = clientPlayer.getData(AttachmentRegistry.ENSURE_CANCEL_REVIVE);
                if (flag) {
                    clientPlayer.displayClientMessage(Component.translatable("message.mikus_vocal_spell.messiah_effect.effect").withColor(16711680), true);
                    clientPlayer.setData(AttachmentRegistry.SHOULD_SEND_MESSAGE, false);
                }
            }
        }
        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            Set<UUID> summons = SummonManager.getSummons(livingEntity);
            Set<LivingEntity> targets = summons.stream().
                    map(serverLevel::getEntity).
                    filter(entity -> entity instanceof LivingEntity).
                    map(entity -> (LivingEntity)entity).collect(Collectors.toSet());

            if (livingEntity instanceof Player && !MVSUtils.isPvP(livingEntity)) {
                List<Player> otherPlayers = serverLevel.getNearbyPlayers(TargetingConditions.forNonCombat(), livingEntity, livingEntity.getBoundingBox().inflate(5));
                targets.addAll(otherPlayers);
            }
            float spell_power = livingEntity.getPersistentData().getFloat(NBTKeyHelper.REVIVE_SPELL_POWER);
            targets.forEach(target -> target.setHealth((float) (target.getHealth() * (1 + 0.05 * spell_power))));
        }
        return true;
    }

    @Override
    public void onEffectRemoved(LivingEntity livingEntity, int amplifier) {
        livingEntity.getPersistentData().putFloat(NBTKeyHelper.REVIVE_SPELL_POWER, 0);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 40 == 0;
    }

    @Override
    public void onMobHurt(@NotNull LivingEntity livingEntity, int amplifier, @NotNull DamageSource damageSource, float amount) {
        if(livingEntity instanceof ServerPlayer serverPlayer) {
            if (!serverPlayer.hasEffect(MVSEffectRegistry.MESSIAH_EFFECT)) return;
            boolean messiahCanceled = serverPlayer.getData(AttachmentRegistry.ENSURE_CANCEL_REVIVE);
            double randomChance = serverPlayer.level().getRandom().nextDouble();
            if(!messiahCanceled && randomChance <= 0.3) {
                serverPlayer.setData(AttachmentRegistry.ENSURE_CANCEL_REVIVE, true);
            }
        }
    }
}