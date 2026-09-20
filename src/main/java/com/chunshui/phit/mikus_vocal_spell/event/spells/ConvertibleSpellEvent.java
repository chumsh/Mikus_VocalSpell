package com.chunshui.phit.mikus_vocal_spell.event.spells;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.client.MVSKeyBindings;
import com.chunshui.phit.mikus_vocal_spell.network.CurrentFormSync;
import com.chunshui.phit.mikus_vocal_spell.utils.ConvertibleSpell;
import com.chunshui.phit.mikus_vocal_spell.utils.MVSUtils;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;


@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID, value = Dist.CLIENT)
public class ConvertibleSpellEvent {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerInput(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide)
            return;

        if (MVSKeyBindings.CHANGE_FORM.consumeClick()) {
            SpellSelectionManager manager = new SpellSelectionManager(player);
            AbstractSpell spell = manager.getSelectedSpellData().getSpell();
            if (!(spell instanceof ConvertibleSpell currentConvertibleSpell)) return;
            MVSUtils.updateCurrentForm(player);
            int currentIndex = MVSUtils.getCurrentForm(player);
            int currentColor = currentConvertibleSpell.getColor();
            if (currentIndex > currentConvertibleSpell.getChangeableTime()) {
                MVSUtils.resetCurrentForm(player);
            }
            int syncIndex = MVSUtils.getCurrentForm(player);
            PacketDistributor.sendToServer(new CurrentFormSync(syncIndex));
            String currentMessageKey = currentConvertibleSpell.getMessageKey(syncIndex);
            player.displayClientMessage(Component.translatable("message.mikus_vocal_spell.change_spell_form.base").append(Component.translatable(currentMessageKey)).withColor(currentColor), true);
        }
    }
}