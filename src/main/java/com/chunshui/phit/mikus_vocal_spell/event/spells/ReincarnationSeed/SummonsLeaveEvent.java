package com.chunshui.phit.mikus_vocal_spell.event.spells.ReincarnationSeed;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class SummonsLeaveEvent {

    private static final int CHECK_INTERVAL = 20;

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel) event.getLevel();
        long gameTime = serverLevel.getGameTime();

        if (gameTime % CHECK_INTERVAL != 0) {
            return;
        }

        for (Player player : serverLevel.players()) {
            if (!player.isAlive()) {
                continue;
            }

            if (!(player.hasEffect(MVSEffectRegistry.PERFORMER_EFFECT) || player.hasEffect(MVSEffectRegistry.ADVENTURER_EFFECT))) {
                continue;
            }
            dismissAllSummons(player, serverLevel);
        }
    }


    public static void dismissAllSummons(Player player, ServerLevel level) {
        Set<UUID> summonUUIDs = SummonManager.getSummons(player);

        List<UUID> toRemove = new ArrayList<>(summonUUIDs);

        for (UUID uuid : toRemove) {
            Entity summon = level.getEntity(uuid);
            if (summon instanceof IMagicSummon magicSummon) {
                magicSummon.onUnSummon();
            } else if (summon != null) {
                summon.discard();
            }
        }
    }

}