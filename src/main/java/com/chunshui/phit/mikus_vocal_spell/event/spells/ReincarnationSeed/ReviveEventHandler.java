package com.chunshui.phit.mikus_vocal_spell.event.spells.ReincarnationSeed;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.network.SyncReviveHandler;
import com.chunshui.phit.mikus_vocal_spell.registries.AttachmentRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class ReviveEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingDeath(LivingDeathEvent event) {

        if (event.getEntity() instanceof ServerPlayer player) {

            boolean messiahCanceled = player.getData(AttachmentRegistry.ENSURE_CANCEL_REVIVE);

            if (messiahCanceled) {
                player.setData(AttachmentRegistry.ENSURE_CANCEL_REVIVE, false);
                return;
            }

            int chance = player.getData(AttachmentRegistry.REVIVE_CHANCE);
            if (chance > 0 && player.hasEffect(MVSEffectRegistry.REVIVE_BUFF)) {
                int newChance = chance - 1;
                player.setData(AttachmentRegistry.REVIVE_CHANCE, newChance);
                PacketDistributor.sendToPlayer(player, new SyncReviveHandler(newChance));
                event.setCanceled(true);
                messageHandler(player, chance);
                player.setHealth(player.getMaxHealth());
                player.invulnerableTime = 20;
            }
        }
    }

    private static void messageHandler(ServerPlayer player, int remainingChance) {
        if (remainingChance > 0) {
            player.sendSystemMessage(
                    Component.translatable("death.mikus_vocal_spell.revived").withColor(16711680));
        } else {
            player.sendSystemMessage(
                    Component.translatable("death.mikus_vocal_spell.revived.last").withColor(16711680));
        }
    }
}
