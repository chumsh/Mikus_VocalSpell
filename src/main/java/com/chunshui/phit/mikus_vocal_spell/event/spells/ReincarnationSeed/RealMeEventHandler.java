package com.chunshui.phit.mikus_vocal_spell.event.spells.ReincarnationSeed;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.Objects;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class RealMeEventHandler {
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        Entity entity = event.getSource().getEntity();

        if (!(entity instanceof LivingEntity player && player.hasEffect(MVSEffectRegistry.REAL_ME_EFFECT))) return;

        int amplifier = Objects.requireNonNull(player.getEffect(MVSEffectRegistry.REAL_ME_EFFECT)).getAmplifier();
        int minAmplifier = Math.max(amplifier, 6);
        float levelBonus = Math.max(minAmplifier * 0.17F, 1);
        int maxValue = player.getPersistentData().getInt(NBTKeyHelper.MAX_ARMOR_VALUE);
        int armorValue = player.getArmorValue();
        float spellPower = player.getPersistentData().getFloat(NBTKeyHelper.REVIVE_SPELL_POWER);
        float adjustPower = Math.min(spellPower, 2);
        float damage = event.getOriginalDamage();
        int gap = maxValue - armorValue;

        if (maxValue <= 30) {
            float minModify = Math.max(gap * 0.055F * adjustPower, 0.8F);
            float maxModify = Math.min(minModify, 1.4F);
            float newDamage = damage * maxModify * levelBonus;
            event.setNewDamage(newDamage);
             if (maxModify == 1.4F)
                player.removeEffect(MVSEffectRegistry.REAL_ME_EFFECT);
        } else {
            int adjustGap = Math.min(gap, 80);
            float minModify = Math.max(adjustGap * 0.03F * adjustPower, 0.8F);
            float maxModify = Math.min(minModify, 1.6F);
            float newDamage = damage * maxModify * levelBonus;
            event.setNewDamage(newDamage);
            if (maxModify == 1.6F)
                player.removeEffect(MVSEffectRegistry.REAL_ME_EFFECT);
        }
    }
}
