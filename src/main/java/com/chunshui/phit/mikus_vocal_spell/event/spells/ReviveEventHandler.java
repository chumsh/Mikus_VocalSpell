package com.chunshui.phit.mikus_vocal_spell.event.spells;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.network.SyncReviveDataPacket;
import com.chunshui.phit.mikus_vocal_spell.server.component.generated.ReviveCapability;
import com.chunshui.phit.mikus_vocal_spell.server.component.generated.ReviveCapabilityManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class ReviveEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        ReviveCapability capability = ReviveCapabilityManager.getReviveCapability(player);
        boolean messiahCanceled = capability.hasCanceled();
        
        if (messiahCanceled) {
            ReviveCapabilityManager.refreshRevive(player);
            return;
        }

        if (!ReviveCapabilityManager.hasReviveCharges(player)) {
            return;
        }

        if (ReviveCapabilityManager.consumeReviveCharge(player)) {
            event.setCanceled(true);
            performInstantRevive(player);
        }

    }

    private static void performInstantRevive(ServerPlayer player) {
        player.setHealth(player.getMaxHealth());
        player.invulnerableTime = 20;

        int remainingCharges = ReviveCapabilityManager.getReviveCapability(player).getCharges();
        
        PacketDistributor.sendToPlayer(player, new SyncReviveDataPacket(
                remainingCharges,
                true
        ));

        if (remainingCharges > 0) {
            player.sendSystemMessage(
                    Component.translatable("death.mikus_vocal_spell.revived").withColor(16711680));
        } else {
            player.sendSystemMessage(
                    Component.translatable("death.mikus_vocal_spell.revived.last").withColor(16711680));
        }
    }
}
