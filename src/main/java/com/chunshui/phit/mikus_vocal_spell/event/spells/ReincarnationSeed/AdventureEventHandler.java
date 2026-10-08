package com.chunshui.phit.mikus_vocal_spell.event.spells.ReincarnationSeed;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class AdventureEventHandler {
    @SubscribeEvent
    public static void onPlayerDamage(LivingDamageEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity player) {
            if (!player.hasEffect(MVSEffectRegistry.ADVENTURER_EFFECT)) return;
            if (event.getSource().getEntity() instanceof LivingEntity livingEntity) {
                float gap1 = livingEntity.getMaxHealth() - player.getMaxHealth();
                float gap2 = player.getMaxHealth() - player.getHealth();
                if (gap1 > player.getMaxHealth() * 3) {
                    float amount = gap2 * gap1 * 0.001F;
                    float minModify = Math.max(amount, 0.9F);
                    float maxModify = Math.min(minModify, 1.3F);
                    player.getPersistentData().putFloat(NBTKeyHelper.ADVENTURES_MODIFY, maxModify);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        Entity entity = event.getSource().getEntity();
        if (entity instanceof LivingEntity player && player.hasEffect(MVSEffectRegistry.ADVENTURER_EFFECT)) {
            float amount = player.getPersistentData().getFloat(NBTKeyHelper.ADVENTURES_MODIFY);
            if (amount <= 0) return;
            event.setNewDamage(event.getOriginalDamage() * amount);
            player.getPersistentData().putFloat(NBTKeyHelper.ADVENTURES_MODIFY, 0);
        }
    }
}
