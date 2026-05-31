package com.chunshui.phit.mikus_vocal_spell.event.spells;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;


@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class CheckUnuseClear {
    /*------------------------------------------清除NBT数据-------------------------------------------------------------*/
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        event.getEntity().getPersistentData().putBoolean(NBTKeyHelper.SCALLION_SPAWN, false);
        event.getEntity().getPersistentData().putBoolean(NBTKeyHelper.CORE_MELT_SPAWN, false);
    }
}