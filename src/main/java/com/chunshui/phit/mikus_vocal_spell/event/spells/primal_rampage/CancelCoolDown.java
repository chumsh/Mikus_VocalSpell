package com.chunshui.phit.mikus_vocal_spell.event.spells.primal_rampage;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.AntiCancelCooldown;
import io.redspace.ironsspellbooks.api.events.SpellCooldownAddedEvent;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class CancelCoolDown {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onAddCoolDown(SpellCooldownAddedEvent.Pre event) {
        MikusVocalSpellIronsSpellsAddon.LOGGER.debug("trigger");
        if (event.getEntity().level().isClientSide)
            return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        if (!player.hasEffect(MVSEffectRegistry.PRIMAL_VANISH_EFFECT))
            return;
        if (event.getSpell() instanceof AntiCancelCooldown)
            return;
        event.setEffectiveCooldown(0);
    }
}
