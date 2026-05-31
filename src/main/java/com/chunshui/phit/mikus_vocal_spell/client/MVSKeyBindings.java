package com.chunshui.phit.mikus_vocal_spell.client;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID, value = Dist.CLIENT)
public class MVSKeyBindings {
    public final static String KEY_CATEGORY = "key.category." + MikusVocalSpellIronsSpellsAddon.MODID;
    public final static String KEY_CHANGE_FORM = "key." + MikusVocalSpellIronsSpellsAddon.MODID + "change_form";

    public final static KeyMapping CHANGE_FORM = new KeyMapping(
            KEY_CHANGE_FORM,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_SHIFT,
            KEY_CATEGORY
            );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(CHANGE_FORM);
    }



}
