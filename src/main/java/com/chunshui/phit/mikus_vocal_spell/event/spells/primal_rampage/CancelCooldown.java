package com.chunshui.phit.mikus_vocal_spell.event.spells.primal_rampage;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.AntiCancelChanting;
import com.chunshui.phit.mikus_vocal_spell.utils.AntiCancelCooldown;
import io.redspace.ironsspellbooks.api.events.SpellCooldownAddedEvent;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class CancelCooldown {
    private static AbstractSpell cachedSpell;
    public static final ResourceLocation ID = MikusVocalSpellIronsSpellsAddon.id("effect_primal_chant");
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onAddCoolDown(SpellCooldownAddedEvent.Pre event) {
//        MikusVocalSpellIronsSpellsAddon.LOGGER.debug("trigger");
        if (event.getEntity().level().isClientSide)
            return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        if (!player.hasEffect(MVSEffectRegistry.PRIMAL_VANISH_EFFECT))
            return;
        if (event.getSpell() instanceof AntiCancelCooldown)
            return;
        event.setEffectiveCooldown(0);
    }
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        if (!player.hasEffect(MVSEffectRegistry.PRIMAL_CHANT_EFFECT)) {
            cachedSpell = null;
            return;
        }

        AttributeInstance attribute = player.getAttribute(AttributeRegistry.CAST_TIME_REDUCTION);
        if (attribute == null) return;

        AbstractSpell spell = new SpellSelectionManager(player).getSelectedSpellData().getSpell();

        AttributeModifier modifier = new AttributeModifier(
                ID,
                100D,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        if (cachedSpell != spell) {
            cachedSpell = spell;

            if (cachedSpell instanceof AntiCancelChanting
                            || cachedSpell.getCastType() == CastType.CONTINUOUS
                            || cachedSpell.getCastType() == CastType.INSTANT
            ) {
                attribute.removeModifier(modifier);
            } else attribute.addTransientModifier(modifier);
        }
    }
}
