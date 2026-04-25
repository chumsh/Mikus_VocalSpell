package com.chunshui.phit.mikus_vocal_spell.item.weapon.aigenerated;

import com.chunshui.phit.mikus_vocal_spell.server.component.generated.ReviveCapability;
import com.chunshui.phit.mikus_vocal_spell.server.component.generated.ReviveCapabilityManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 重生护身符 - 右键使用赋予玩家三次重生机会
 */
public class ReviveTalismanItem extends Item {
    
    public ReviveTalismanItem() {
        super(new Properties().stacksTo(1));
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            // 激活重生能力
            ReviveCapabilityManager.activateRevive(serverPlayer, ReviveCapability.MAX_CHARGES);
            
            // 发送提示消息
            serverPlayer.sendSystemMessage(
                Component.translatable("item.mikus_vocal_spell.revive_talisman.use")
                    .append(Component.literal(" ❤️ x" + ReviveCapability.MAX_CHARGES))
            );
            
            // 播放音效
            serverPlayer.playSound(net.minecraft.sounds.SoundEvents.TOTEM_USE, 1.0f, 1.0f);
            
            // 消耗物品
            ItemStack stack = player.getItemInHand(hand);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }
        
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
