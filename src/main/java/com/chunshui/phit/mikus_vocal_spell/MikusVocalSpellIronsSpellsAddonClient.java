package com.chunshui.phit.mikus_vocal_spell;

import com.chunshui.phit.mikus_vocal_spell.entity.spells.scallion_dance.ScallionRender;
import com.chunshui.phit.mikus_vocal_spell.particle.ReincarnationParticle;
import com.chunshui.phit.mikus_vocal_spell.particle.ScallionParticle;
import com.chunshui.phit.mikus_vocal_spell.registries.EntityRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.ParticleRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = MikusVocalSpellIronsSpellsAddon.MODID, dist = Dist.CLIENT)
public class MikusVocalSpellIronsSpellsAddonClient {
    public MikusVocalSpellIronsSpellsAddonClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        
        IEventBus modEventBus = container.getEventBus();
        if (modEventBus != null) {
            modEventBus.addListener(MikusVocalSpellIronsSpellsAddonClient::registerParticles);
            modEventBus.addListener(MikusVocalSpellIronsSpellsAddonClient::renderRegister);
        }
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        MikusVocalSpellIronsSpellsAddon.LOGGER.info("HELLO FROM CLIENT SETUP");
        MikusVocalSpellIronsSpellsAddon.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    private static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistries.REINCARNATION_PARTICLE.get(), ReincarnationParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistries.SCALLION_PARTICLE.get(), ScallionParticle.Provider::new);
    }
    private static void renderRegister(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(EntityRegistry.SCALLION.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SCALLION_AREA.get(), ScallionRender::new);
    }
}
