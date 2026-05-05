package com.chunshui.phit.mikus_vocal_spell.effects.vocal.scallon;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

public class ScallionEffect extends MobEffect {
    public ScallionEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        this.addAttributeModifier(Attributes.ATTACK_SPEED, MikusVocalSpellIronsSpellsAddon.id("effect_attack_speed"), 0.1 + 0.1 * amplifier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, MikusVocalSpellIronsSpellsAddon.id("effect_knockback"), 1.0, AttributeModifier.Operation.ADD_VALUE);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onMobHurt(@NotNull LivingEntity livingEntity, int amplifier, @NotNull DamageSource damageSource, float amount) {
        if(livingEntity instanceof ServerPlayer serverPlayer) {
            if(serverPlayer.level().isClientSide) {
                return;
            }
            MagicData playerMagicData = MagicData.getPlayerMagicData(serverPlayer);
            float damage = (float) (amount * 0.5 / (((double) 1 / 9) * (amplifier + 1) + ((double) 8 / 9)));
            if (playerMagicData.isCasting()) {
                livingEntity.setHealth(livingEntity.getMaxHealth() - damage);
            }

        }

    }
}
