package com.chunshui.phit.mikus_vocal_spell.event.spells;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.entity.spells.AbstractCheckArea;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.List;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class CheckUnuseClear {
    /*------------------------------------------清除NBT数据-------------------------------------------------------------*/
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        event.getEntity().getPersistentData().putBoolean(NBTKeyHelper.SCALLION_SPAWN, false);
    }

    /*------------------------------------------清除区域实体-------------------------------------------------------------*/
    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        Level level = (Level) event.getLevel();
        // 只在服务端执行清理逻辑
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            List<? extends AbstractCheckArea> areas = serverLevel.getEntities(
                    EntityTypeTest.forClass(AbstractCheckArea.class),
                    CheckUnuseClear::shouldRemove
            );

            for (AbstractCheckArea area : areas) { 
                area.discard(); 
            }
        }
    }

    private static boolean shouldRemove(AbstractCheckArea area) { return true; }
}