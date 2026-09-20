package com.chunshui.phit.mikus_vocal_spell.event.spells.ReincarnationSeed;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class PerformerEventHandler {
    @SubscribeEvent
    public static void onPlayerAttack(LivingDamageEvent.Pre event) {
        Entity entity = event.getSource().getEntity();
        if (!(entity instanceof Player player)) return;
        if (!player.hasEffect(MVSEffectRegistry.PERFORMER_EFFECT)) return;
        float newDamage = event.getNewDamage();
        float baseDamage = player.getPersistentData().getFloat(NBTKeyHelper.PERFORMER_DAMAGE);
        float totalDamage = newDamage + baseDamage;
        player.getPersistentData().putFloat(NBTKeyHelper.PERFORMER_DAMAGE, totalDamage);
        MagicData magicData = MagicData.getPlayerMagicData(player);
        int castingSpellLevel = magicData.getCastingSpellLevel();
        float mana = magicData.getMana();
        if (totalDamage > castingSpellLevel * mana * 0.05 && castingSpellLevel> 0) {
            event.setNewDamage((float) (newDamage + totalDamage * 0.3));
            player.getPersistentData().putFloat(NBTKeyHelper.PERFORMER_DAMAGE, 0);
        } else if (totalDamage > mana * 0.2) {
            event.setNewDamage((float) (newDamage + totalDamage * 0.3));
            player.getPersistentData().putFloat(NBTKeyHelper.PERFORMER_DAMAGE, 0);
        }
    }
}
