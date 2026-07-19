package com.chunshui.phit.mikus_vocal_spell.event.spells.core_melt;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class GrowthProgress {
    @SubscribeEvent
    public static void onHurt(LivingDamageEvent.Post event) {
        if (!event.getEntity().hasEffect(MVSEffectRegistry.INVENTOR_EFFECT)) { return; }
        float damage = event.getNewDamage();
        event.getEntity().getPersistentData().putFloat(NBTKeyHelper.TOTAL_DAMAGE, damage);
    }
}
