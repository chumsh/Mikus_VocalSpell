
package com.chunshui.phit.mikus_vocal_spell.server.component.generated;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class ReviveCapabilityManager {

    private static final Map<ReviveCapabilityKey, ReviveCapability> CAPABILITY_CACHE = new ConcurrentHashMap<>();

    private static final String TAG_KEY = "mikus_vocal_spell_revive";

    public static ReviveCapability getReviveCapability(ServerPlayer player) {
        ReviveCapabilityKey key = new ReviveCapabilityKey(player);
        return CAPABILITY_CACHE.computeIfAbsent(key,
                k -> new ReviveCapability(player));
    }

    public static void activateRevive(ServerPlayer player, int charges) {
        ReviveCapability capability = getReviveCapability(player);
        capability.activate(charges);

        MikusVocalSpellIronsSpellsAddon.LOGGER.info(
                "Activated revive for {} with {} charges | NBT: active={}, charges={}",
                player.getName().getString(),
                charges,
                player.getPersistentData().getCompound(TAG_KEY).getBoolean("active"),
                player.getPersistentData().getCompound(TAG_KEY).getInt("charges")
        );
    }

    public static boolean consumeReviveCharge(ServerPlayer player) {
        ReviveCapability capability = getReviveCapability(player);
        boolean consumed = capability.consumeCharge();

        if (consumed) {
            CompoundTag tag = player.getPersistentData().getCompound(TAG_KEY);
            MikusVocalSpellIronsSpellsAddon.LOGGER.info(
                    "Player {} used revive charge. Remaining: {} cache's cancellation: {} | NBT: active={}, charges={}, canceled={}",
                    player.getName().getString(),
                    capability.getCharges(),
                    capability.hasCanceled(),
                    tag.getBoolean("active"),
                    tag.getInt("charges"),
                    tag.getBoolean("messiah_cancel_revive")
            );
        }

        return consumed;
    }

    public static boolean hasReviveCharges(ServerPlayer player) {
        ReviveCapability capability = getReviveCapability(player);
        return capability.isActive() && capability.hasCharges();
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        ServerPlayer oldPlayer = (ServerPlayer) event.getOriginal();
        ServerPlayer newPlayer = (ServerPlayer) event.getEntity();

        CompoundTag oldTag = oldPlayer.getPersistentData().getCompound(TAG_KEY);
        if (oldPlayer.getPersistentData().contains(TAG_KEY)) {
            newPlayer.getPersistentData().put(TAG_KEY, oldTag.copy());

            MikusVocalSpellIronsSpellsAddon.LOGGER.info(
                    "Cloned revive data from {} to {}: {} charges",
                    oldPlayer.getName().getString(),
                    newPlayer.getName().getString(),
                    oldTag.getInt("charges")
            );
        }

        ReviveCapabilityKey oldKey = new ReviveCapabilityKey(oldPlayer);

        CAPABILITY_CACHE.remove(oldKey);

        MikusVocalSpellIronsSpellsAddon.LOGGER.debug(
                "Cleared capability cache for player clone: {}",
                newPlayer.getName().getString()
        );
    }

    public static void refreshRevive(ServerPlayer player) {
        CompoundTag tag = player.getPersistentData().getCompound(TAG_KEY);
        tag.putBoolean("active", false);
        tag.putInt("charges", 0);
        player.getPersistentData().put(TAG_KEY, tag);
        boolean messiahCanceled = player.getPersistentData().getBoolean("messiah_cancel_revive");
        if (messiahCanceled) {
            player.getPersistentData().putBoolean("messiah_cancel_revive", false);
        }
        MikusVocalSpellIronsSpellsAddon.LOGGER.info(
                "Refresh revive for {}",
                player.getName().getString()
        );
    }

    private static class ReviveCapabilityKey {
        private final ServerPlayer serverPlayer;

        public ReviveCapabilityKey(ServerPlayer player) {
            this.serverPlayer = player;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReviveCapabilityKey paramKey = (ReviveCapabilityKey) o;
            return java.util.Objects.equals(serverPlayer, paramKey.serverPlayer);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(serverPlayer);
        }
    }
}
