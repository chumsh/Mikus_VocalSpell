package com.chunshui.phit.mikus_vocal_spell.item;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEffectRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSItemRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSSoundRegistry;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ManaPotion extends Item {

    public ManaPotion(Properties properties) {
        super(properties);
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            boolean has_melody = player.getPersistentData().getBoolean(NBTKeyHelper.HAS_MELODY);
            MikusVocalSpellIronsSpellsAddon.LOGGER.info("has_melody = :{}", has_melody);
            int mana_change = player.getPersistentData().getInt(NBTKeyHelper.MANA_CHANGE);
            if (mana_change < 15 || has_melody) {
                MagicData magicData = MagicData.getPlayerMagicData(player);
                magicData.addMana(20);
                if (!has_melody) {
                    player.getPersistentData().putInt(NBTKeyHelper.MANA_CHANGE, mana_change + 1);
                }
                itemStack.shrink(1);
                ItemStack empty_mana_potion = new ItemStack(MVSItemRegistry.EMPTY_MANA_POTION);
                player.getInventory().add(empty_mana_potion);
                if (mana_change == 5 && !has_melody) {
                    player.addEffect(new MobEffectInstance(
                            MVSEffectRegistry.MANA_DAZE_EFFECT,
                            200,
                            0,
                            false,
                            true,
                            true
                    ));
                } else if (mana_change == 10 && !has_melody) {
                    player.addEffect(new MobEffectInstance(
                            MVSEffectRegistry.MANA_DAZE_EFFECT,
                            200,
                            1,
                            false,
                            true,
                            true
                    ));
                }
            }
            if (mana_change < 15 || has_melody) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), MVSSoundRegistry.DRINK.get(), player.getSoundSource(), 1.0f, 1.0f);
            }
            if (!has_melody) {
                if (mana_change >= 15) {
                    player.addEffect(new MobEffectInstance(
                            MVSEffectRegistry.MANA_DAZE_EFFECT,
                        200,
                        2,
                        false,
                        true,
                        true
                    ));
                    return InteractionResultHolder.fail(itemStack);
                }
            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}