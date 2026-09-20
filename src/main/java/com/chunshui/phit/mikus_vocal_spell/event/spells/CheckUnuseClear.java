package com.chunshui.phit.mikus_vocal_spell.event.spells;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.event.spells.primal_rampage.CancelCooldown;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;


@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class CheckUnuseClear {
    static boolean needClear = false;
    /*------------------------------------------清除NBT数据-------------------------------------------------------------*/
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        needClear = true;
        event.getEntity().getPersistentData().putBoolean(NBTKeyHelper.SCALLION_SPAWN, false);
        event.getEntity().getPersistentData().putBoolean(NBTKeyHelper.CORE_MELT_SPAWN, false);
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        event.getEntity().getPersistentData().putBoolean(NBTKeyHelper.AREA_EFFECT_FLAG, true);
        if (needClear) {
            event.getEntity().getPersistentData().putBoolean(NBTKeyHelper.AREA_EFFECT_FLAG, false);
        }
    }
    /*------------------------------------------清除属性修改-------------------------------------------------------------*/
    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        AttributeInstance attribute = player.getAttribute(AttributeRegistry.CAST_TIME_REDUCTION);
        if (attribute != null) {
            attribute.removeModifier(CancelCooldown.ID);
        }

    }
}