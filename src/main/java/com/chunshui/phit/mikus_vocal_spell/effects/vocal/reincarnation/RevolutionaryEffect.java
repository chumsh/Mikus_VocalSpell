package com.chunshui.phit.mikus_vocal_spell.effects.vocal.reincarnation;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.AttachmentRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class RevolutionaryEffect extends MagicMobEffect {
    public static final ResourceLocation CAST_ID = MikusVocalSpellIronsSpellsAddon.id("effect_chant_extend");
    public static final ResourceLocation COOLDOWN_ID = MikusVocalSpellIronsSpellsAddon.id("effect_cooldown_extend");
    public static final ResourceLocation SUMMON_ID = MikusVocalSpellIronsSpellsAddon.id("summon_attack_damage");

    public RevolutionaryEffect(MobEffectCategory category, int color) {
        super(category, color);

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, MikusVocalSpellIronsSpellsAddon.id("effect_slow"), -0.80, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
        super.onEffectAdded(livingEntity, amplifier);
        float spellPower = livingEntity.getPersistentData().getFloat(NBTKeyHelper.REVIVE_SPELL_POWER);
        if (spellPower == 0) return;
        float testAmount = Math.min(-0.35F + 0.5F * spellPower, -0.1F);
        float adjustAmount = Math.max(testAmount, -0.3F);
        addAttributeModifier(AttributeRegistry.CAST_TIME_REDUCTION, CAST_ID, adjustAmount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(AttributeRegistry.COOLDOWN_REDUCTION, COOLDOWN_ID, adjustAmount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        modifySummonAttribute(livingEntity, spellPower, true);
    }

    private void modifySummonAttribute(LivingEntity livingEntity, float spellPower, boolean isAdding) {
        if (!(livingEntity.level() instanceof ServerLevel serverLevel)) return;
        Set<UUID> summons = SummonManager.getSummons(livingEntity);
        Set<LivingEntity> targets = summons.stream().map(serverLevel::getEntity)
                .filter(entity -> entity instanceof LivingEntity thisEntity && thisEntity instanceof IMagicSummon)
                .map(entity -> (LivingEntity) entity)
                .collect(Collectors.toSet());
        float amount = 0.3F * spellPower;
        targets.forEach(currentEntity -> {
            AttributeInstance attribute = currentEntity.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attribute == null) return;
            AttributeModifier modifier = new AttributeModifier(SUMMON_ID, amount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            if (isAdding)
                attribute.addTransientModifier(modifier);
            else attribute.removeModifier(SUMMON_ID);
        });
    }


    @Override
    public void onEffectRemoved(LivingEntity livingEntity, int amplifier) {
        livingEntity.getPersistentData().putFloat(NBTKeyHelper.REVIVE_SPELL_POWER, 0);
        AttributeInstance attribute1 = livingEntity.getAttribute(AttributeRegistry.CAST_TIME_REDUCTION);
        if (attribute1 != null) attribute1.removeModifier(CAST_ID);
        AttributeInstance attribute2 = livingEntity.getAttribute(AttributeRegistry.COOLDOWN_REDUCTION);
        if (attribute2 != null) attribute2.removeModifier(RevolutionaryEffect.COOLDOWN_ID);
        modifySummonAttribute(livingEntity, 0, false);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity instanceof Player clientPlayer && entity.level().isClientSide) {
            boolean shouldSend = clientPlayer.getData(AttachmentRegistry.SHOULD_SEND_MESSAGE);
            if (shouldSend)
                clientPlayer.displayClientMessage(Component.translatable("message.mikus_vocal_spell.inventor_effect.effect").withColor(16711680),
                        true
                );
            clientPlayer.setData(AttachmentRegistry.SHOULD_SEND_MESSAGE, false);
        }
        return true;
    }


    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

}