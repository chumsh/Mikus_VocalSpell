package com.chunshui.phit.mikus_vocal_spell.event.spells;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.client.MVSKeyBindings;
import com.chunshui.phit.mikus_vocal_spell.utils.ConvertibleSpell;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

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

        cachedManager = event.getManager();
        updateConvertibleState();
    }

    private static void updateConvertibleState() {
        if (cachedManager == null) {
            return;
        }

        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        int index = cachedManager.getCurrentSelection().index;
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
    public static void onPlayerInput(ClientTickEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        updateConvertibleState();

        if (!isConvertible) {
            player.getPersistentData().putInt(NBTKeyHelper.FORM_INDEX, 0);
            return;
        }

        int index = player.getPersistentData().getInt(NBTKeyHelper.FORM_INDEX);
        if (index >= currentConvertibleSpell.getChangeableTime()) {
            player.getPersistentData().putInt(NBTKeyHelper.FORM_INDEX, 0);
        }

        if (MVSKeyBindings.CHANGE_FORM.consumeClick()) {
            int currentFormIndex = player.getPersistentData().getInt(NBTKeyHelper.FORM_INDEX) + 1;
            player.getPersistentData().putInt(NBTKeyHelper.FORM_INDEX, currentFormIndex);
            currentMessageKey = currentConvertibleSpell.getMessageKey(currentFormIndex);
            player.displayClientMessage(Component.translatable("message.mikus_vocal_spell.change_spell_form.base").append(Component.translatable(currentMessageKey)).withColor(currentColor), true);
        }
    }
}
