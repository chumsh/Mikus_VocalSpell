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
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class ConvertibleSpellEvent {
    private static boolean isConvertible;
    private static String currentMessageKey;
    private static int currentColor;
    private static ConvertibleSpell currentConvertibleSpell;
    private static SpellSelectionManager cachedManager;

    //SpellSelectionEvent似乎存在一些问题，故采取缓存manager进行处理
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onSpellSelected(SpellSelectionManager.SpellSelectionEvent event) {

        Player player = event.getEntity();
        cachedManager = event.getManager();
        updateConvertibleState(player);
    }

    private static void updateConvertibleState(Player player) {
        if (cachedManager == null) {
            return;
        }

        if (player == null) {
            MikusVocalSpellIronsSpellsAddon.LOGGER.info("player is null");
            return;
        }

        int index = cachedManager.getCurrentSelection().index;
        if (index < 0) {
            index = 0;
        }
        AbstractSpell spell = cachedManager.getSpellData(index).getSpell();

        if (spell instanceof ConvertibleSpell convertibleSpell) {
            isConvertible = true;
            currentConvertibleSpell = convertibleSpell;
            currentColor = convertibleSpell.getColor();
        } else {
            isConvertible = false;
            currentConvertibleSpell = null;
            currentMessageKey = "";
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerInput(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        updateConvertibleState(player);

        if (!isConvertible) {
            MVSUtils.resetCurrentForm(player);
            return;
        }

        if (MVSKeyBindings.CHANGE_FORM.consumeClick()) {
            MVSUtils.updateCurrentForm(player);
            int currentIndex = MVSUtils.getCurrentForm(player);
            MikusVocalSpellIronsSpellsAddon.LOGGER.debug("time: {}", currentConvertibleSpell.getChangeableTime());
            if (currentIndex > currentConvertibleSpell.getChangeableTime()) {
                MVSUtils.resetCurrentForm(player);
            }
            int syncIndex = MVSUtils.getCurrentForm(player);
            PacketDistributor.sendToAllPlayers(new CurrentFormSync(syncIndex));
            currentMessageKey = currentConvertibleSpell.getMessageKey(syncIndex);
            player.displayClientMessage(Component.translatable("message.mikus_vocal_spell.change_spell_form.base").append(Component.translatable(currentMessageKey)).withColor(currentColor), true);
        }
    }
}