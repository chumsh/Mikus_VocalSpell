package com.chunshui.phit.mikus_vocal_spell.event.spells;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.entity.spells.AbstractCheckArea;
import com.chunshui.phit.mikus_vocal_spell.entity.spells.scallion_dance.ScallionEffectArea;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.List;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class CheckUnuseClear {

    private static final int CHECK_INTERVAL = 20;

    /*------------------------------------------清除实体NBT-------------------------------------------------------------*/
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        event.getEntity().getPersistentData().putBoolean(ScallionEffectArea.SPAWN, false);
        event.getEntity().getPersistentData().putInt("casting_count", 0);
    }
/*------------------------------------------清除区域特效实体-------------------------------------------------------------*/
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {

        if (event.getLevel().isClientSide) {
            return;
        }

        long gameTime = event.getLevel().getGameTime();
        if (!(gameTime % CHECK_INTERVAL == 0)) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel) event.getLevel();
        List<? extends AbstractCheckArea> areas = serverLevel.getEntities(
                EntityTypeTest.forClass(AbstractCheckArea.class),
                CheckUnuseClear::shouldRemove
                );

        for (AbstractCheckArea area : areas) { area.discard(); }
    }

    private static boolean shouldRemove(AbstractCheckArea area) {
        if (area.getOwner() == null) {
            return area.tickCount > area.maxLifeTime;
        }
        return false;
    }
}