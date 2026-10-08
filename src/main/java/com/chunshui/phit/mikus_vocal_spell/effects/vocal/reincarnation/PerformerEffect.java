package com.chunshui.phit.mikus_vocal_spell.effects.vocal.reincarnation;


import com.chunshui.phit.mikus_vocal_spell.registries.AttachmentRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PerformerEffect extends MagicMobEffect {

    public PerformerEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
        super.onEffectAdded(livingEntity, amplifier);
        if (livingEntity.level() instanceof ServerLevel serverLevel) dismissAllSummons(livingEntity, serverLevel);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player clientPlayer && livingEntity.level().isClientSide) {
            boolean shouldSend = clientPlayer.getData(AttachmentRegistry.SHOULD_SEND_MESSAGE);
            if (shouldSend)
                clientPlayer.displayClientMessage(Component.translatable("message.mikus_vocal_spell.performer_effect.effect").withColor(16711680),
                        true
                );
            clientPlayer.setData(AttachmentRegistry.SHOULD_SEND_MESSAGE, false);
        }
        return true;

    }

    private void dismissAllSummons(LivingEntity player, ServerLevel level) {
        Set<UUID> summonUUIDs = SummonManager.getSummons(player);

        List<UUID> toRemove = new ArrayList<>(summonUUIDs);

        for (UUID uuid : toRemove) {
            Entity summon = level.getEntity(uuid);
            if (summon instanceof IMagicSummon magicSummon) {
                magicSummon.onUnSummon();
            } else if (summon != null) {
                summon.discard();
            }
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick ( int duration, int amplifier){
        return true;
    }

    @Override
    public void onEffectRemoved(LivingEntity livingEntity, int amplifier) {
        livingEntity.getPersistentData().putFloat(NBTKeyHelper.REVIVE_SPELL_POWER, 0);

    }
}
