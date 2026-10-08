package com.chunshui.phit.mikus_vocal_spell.effects.vocal.reincarnation;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.AttachmentRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class InventorEffect extends MagicMobEffect {
    int duration;
    private static final ResourceLocation ID = MikusVocalSpellIronsSpellsAddon.id("inventor_effect_reduce_chant");

    public InventorEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
        super.onEffectAdded(livingEntity, amplifier);
        AttributeInstance attribute = livingEntity.getAttribute(AttributeRegistry.CAST_TIME_REDUCTION);
        float spellPower = livingEntity.getPersistentData().getFloat(NBTKeyHelper.REVIVE_SPELL_POWER);
        if (spellPower == 0) return;
        if (attribute != null) {
            AttributeModifier inventorEffectReduceChant = new AttributeModifier(ID, 0.4 * spellPower, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            attribute.addTransientModifier(inventorEffectReduceChant);
        }
    }

    @Override
    public void onEffectRemoved(LivingEntity livingEntity, int amplifier) {
        AttributeInstance attribute = livingEntity.getAttribute(AttributeRegistry.CAST_TIME_REDUCTION);
        if (attribute != null)
            attribute.removeModifier(ID);
        livingEntity.getPersistentData().putFloat(NBTKeyHelper.REVIVE_SPELL_POWER, 0);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity instanceof Player clientPlayer && entity.level().isClientSide) {
            Boolean shouldSend = clientPlayer.getData(AttachmentRegistry.SHOULD_SEND_MESSAGE);
            if (shouldSend)
                clientPlayer.displayClientMessage(Component.translatable("message.mikus_vocal_spell.inventor_effect.effect").withColor(16711680),
                        true
                );
            clientPlayer.setData(AttachmentRegistry.SHOULD_SEND_MESSAGE, false);
        }

        if(entity instanceof ServerPlayer player){
                Set<UUID> summonUUIDs = SummonManager.getSummons(player);
                ServerLevel serverLevel = player.serverLevel();
                Set<LivingEntity> allTargets = summonUUIDs.stream()
                        .map(serverLevel::getEntity)
                        .filter(summonEntity -> summonEntity instanceof LivingEntity)
                        .map(summonEntity -> (LivingEntity) summonEntity)
                        .collect(Collectors.toSet());
            float spell_power = player.getPersistentData().getFloat(NBTKeyHelper.REVIVE_SPELL_POWER);
            if (spell_power == 0) return true;
            float original = (float) (1 - 0.8 * spell_power);

            float damagePercent = Math.max(original, 0.1F);

            TargetingConditions predicate = TargetingConditions.forNonCombat();
            List<Player> nearbyPlayers = serverLevel.getNearbyPlayers(predicate, player, player.getBoundingBox().inflate(8));

                allTargets.addAll(nearbyPlayers);

            allTargets.forEach(livingEntity -> livingEntity.setHealth(livingEntity.getHealth() * (1 - damagePercent)));
            }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        this.duration = duration;
        return duration >= 20 && duration % 40 == 0;
    }
}

