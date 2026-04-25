package com.chunshui.phit.mikus_vocal_spell.client.spells;

import com.chunshui.phit.mikus_vocal_spell.registries.ItemRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSSoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ReviveAnimationHandler {
    
    public static void playReviveAnimation(int charges) {
        Minecraft mc = Minecraft.getInstance();
        
        if (mc.level == null || mc.player == null) return;
        
        Player player = mc.player;

        player.playSound(MVSSoundRegistry.EAT_APPLE.get(), 1.0f, 1.0f);
        playCustomAnimation(charges);
    }
    
    private static void playCustomAnimation(int charges) {
        // TODO: 客户端动画逻辑
        if(charges == 2){
            ItemStack apple = new ItemStack(ItemRegistry.REINCARNATION_APPLE.get());
            Minecraft.getInstance().gameRenderer.displayItemActivation(new ItemStack(apple.getItem()));
        }else if(charges == 1){
            ItemStack apple2 = new ItemStack(ItemRegistry.REINCARNATION_APPLE_TWO.get());
            Minecraft.getInstance().gameRenderer.displayItemActivation(new ItemStack(apple2.getItem()));
        }else if(charges == 0){
            ItemStack apple3 = new ItemStack(ItemRegistry.REINCARNATION_APPLE_THREE.get());
            Minecraft.getInstance().gameRenderer.displayItemActivation(new ItemStack(apple3.getItem()));
        }
    }
}
