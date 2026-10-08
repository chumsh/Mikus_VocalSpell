package com.chunshui.phit.mikus_vocal_spell.effects.vocal.reincarnation;

import com.chunshui.phit.mikus_vocal_spell.registries.AttachmentRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.VocalSpellRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdventurerEffect extends MagicMobEffect {
    public AdventurerEffect(MobEffectCategory category, int color) {
        super(category, color);
    }


    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity instanceof Player player && entity.level().isClientSide()) {
            boolean shouldSend = player.getData(AttachmentRegistry.SHOULD_SEND_MESSAGE);
            if (shouldSend) {
                player.displayClientMessage(
                        net.minecraft.network.chat.Component.translatable("message.mikus_vocal_spell.adventurer_effect.effect").withColor(16711680),
                        true
                );
                player.setData(AttachmentRegistry.SHOULD_SEND_MESSAGE, false);
            }
        }
        if(entity.level() instanceof ServerLevel serverLevel) {
            float damagePercent = 0.05f;

            List<Player> nearbyPlayers = serverLevel.getNearbyPlayers(TargetingConditions.forNonCombat(), entity, entity.getBoundingBox().inflate(6));

            for (LivingEntity target : nearbyPlayers) {
                float damageAmount = target.getHealth() * damagePercent;
                DamageSources.applyDamage(target, damageAmount, VocalSpellRegistry.REINCARNATION_SEED.get().getDamageSource(entity));
            }
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration >= 20 && duration % 40 == 0;
    }

    @Override
    public void onEffectRemoved(LivingEntity livingEntity, int amplifier) {
        livingEntity.getPersistentData().putFloat(NBTKeyHelper.ADVENTURES_MODIFY, 0);
    }
}