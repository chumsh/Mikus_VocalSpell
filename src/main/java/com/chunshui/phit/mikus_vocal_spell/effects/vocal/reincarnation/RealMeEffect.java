package com.chunshui.phit.mikus_vocal_spell.effects.vocal.reincarnation;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

public class RealMeEffect extends MagicMobEffect {
    private static final ResourceLocation ID = MikusVocalSpellIronsSpellsAddon.id("real_me_modify_armor");
    public RealMeEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onEffectAdded(@NotNull LivingEntity livingEntity, int amplifier) {
        super.onEffectAdded(livingEntity, amplifier);

        int armorValue = livingEntity.getArmorValue();
        livingEntity.getPersistentData().putInt(NBTKeyHelper.MAX_ARMOR_VALUE, armorValue);

    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        int time = livingEntity.getPersistentData().getInt(NBTKeyHelper.REALME_MODIFY_TIME);
        livingEntity.getPersistentData().putInt(NBTKeyHelper.REALME_MODIFY_TIME, time + 1);
        time = livingEntity.getPersistentData().getInt(NBTKeyHelper.REALME_MODIFY_TIME);
        int maxValue = livingEntity.getPersistentData().getInt(NBTKeyHelper.MAX_ARMOR_VALUE);
        int currentValue = livingEntity.getArmorValue();
        if (currentValue > 0) {
            AttributeInstance attribute = livingEntity.getAttribute(Attributes.ARMOR);
            AttributeModifier modifier;
            if (maxValue <= 30) {
                modifier = new AttributeModifier(ID, -3 * time, AttributeModifier.Operation.ADD_VALUE);
            } else {
                modifier = new AttributeModifier(ID, -2 * time, AttributeModifier.Operation.ADD_VALUE);
            }
            if (attribute != null) attribute.addOrUpdateTransientModifier(modifier);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 60 == 0;
    }

    @Override
    public void onEffectRemoved(LivingEntity livingEntity, int amplifier) {
        AttributeInstance attribute = livingEntity.getAttribute(Attributes.ARMOR);
        if (attribute != null) attribute.removeModifier(ID);
        livingEntity.getPersistentData().putInt(NBTKeyHelper.REALME_MODIFY_TIME, 0);
    }
}
