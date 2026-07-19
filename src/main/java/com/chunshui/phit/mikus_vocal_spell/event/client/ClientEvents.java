package com.chunshui.phit.mikus_vocal_spell.event.client;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.client.ClientRenderers;
import com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt.InnocenceShellRender;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEntityRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class ClientEvents {
    @SubscribeEvent
    public static void onAddRender(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MVSEntityRegistry.INNOCENCE_SHELL.get(),(EntityRendererProvider.Context context) ->{
            if (ClientRenderers.INNOCENCE_SHELL_RENDER == null) {
                ClientRenderers.INNOCENCE_SHELL_RENDER = new InnocenceShellRender(context);
            }
            return ClientRenderers.INNOCENCE_SHELL_RENDER;
        });
    }
}
