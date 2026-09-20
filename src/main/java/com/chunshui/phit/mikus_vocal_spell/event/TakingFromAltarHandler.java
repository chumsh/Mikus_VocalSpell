package com.chunshui.phit.mikus_vocal_spell.event;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.block.block_entity.EchoAltarBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class TakingFromAltarHandler {
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        BlockPos pos = event.getPos();
        if (event.getLevel().getBlockEntity(pos) instanceof EchoAltarBE) {
            if (event.getEntity().getMainHandItem().getItem() instanceof BlockItem) {
                return;
            }
            event.setUseBlock(TriState.TRUE);
        }
    }
}
