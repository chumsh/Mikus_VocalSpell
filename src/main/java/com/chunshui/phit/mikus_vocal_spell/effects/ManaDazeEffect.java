package com.chunshui.phit.mikus_vocal_spell.effects;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

public class ManaDazeEffect extends MagicMobEffect {
    int tickDuration = -1;
    public ManaDazeEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, MikusVocalSpellIronsSpellsAddon.id("effect_slow"), -.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        if (amplifier == 0) {
            this.addAttributeModifier(Attributes.ATTACK_SPEED, MikusVocalSpellIronsSpellsAddon.id("effect_attack_slow"), -.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        } else if (amplifier == 1) {
            this.addAttributeModifier(Attributes.ATTACK_SPEED, MikusVocalSpellIronsSpellsAddon.id("effect_attack_slow"), -.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            this.addAttributeModifier(Attributes.ATTACK_KNOCKBACK, MikusVocalSpellIronsSpellsAddon.id("effect_knockback"), -0.3, AttributeModifier.Operation.ADD_VALUE);
        } else if(amplifier == 2) {
            this.addAttributeModifier(Attributes.ATTACK_SPEED, MikusVocalSpellIronsSpellsAddon.id("effect_attack_slow"), -.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            this.addAttributeModifier(Attributes.ATTACK_KNOCKBACK, MikusVocalSpellIronsSpellsAddon.id("effect_knockback"), -0.3, AttributeModifier.Operation.ADD_VALUE);
            this.addAttributeModifier(AttributeRegistry.CAST_TIME_REDUCTION, MikusVocalSpellIronsSpellsAddon.id("effect_casting_slow"), -.8, AttributeModifier.Operation.ADD_VALUE);
            if(!(tickDuration == -1)) {
                livingEntity.addEffect(new MobEffectInstance(
                        MobEffects.CONFUSION,
                        tickDuration,
                        2,
                        false,
                        false,
                        false
                        ));
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
       if (amplifier == 2) {
           this.tickDuration = duration;
       }
       return true;
    }

    @Override
    public void onEffectRemoved(LivingEntity livingEntity, int amplifier) {
        livingEntity.getPersistentData().putInt(NBTKeyHelper.MANA_CHANGE, 0);
    }
}
