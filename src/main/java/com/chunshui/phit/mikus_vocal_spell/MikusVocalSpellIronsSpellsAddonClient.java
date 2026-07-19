package com.chunshui.phit.mikus_vocal_spell;

import com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt.*;
import com.chunshui.phit.mikus_vocal_spell.entity.spells.scallion_dance.ScallionRender;
import com.chunshui.phit.mikus_vocal_spell.particle.ReincarnationParticle;
import com.chunshui.phit.mikus_vocal_spell.particle.ScallionParticle;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSEntityRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSFluidRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.ParticleRegistries;
import io.redspace.ironsspellbooks.fluids.SimpleClientFluidType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
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
            modEventBus.addListener(MikusVocalSpellIronsSpellsAddonClient::registerClientExtensions);
            modEventBus.addListener(EntityRenderersEvent.AddLayers.class,MikusVocalSpellIronsSpellsAddonClient::addLayer);
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

    private static void renderRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MVSEntityRegistry.SCALLION.get(), NoopRenderer::new);
        event.registerEntityRenderer(MVSEntityRegistry.NONE_CHECK_AREA.get(), NoopRenderer::new);
        event.registerEntityRenderer(MVSEntityRegistry.SCALLION_AREA.get(), ScallionRender::new);
        event.registerEntityRenderer(MVSEntityRegistry.CORE_MELT.get(), (context) -> new CoreMeltRender(context, CoreMeltModel::new));
        event.registerEntityRenderer(MVSEntityRegistry.INNOCENCE_SHELL.get(), InnocenceShellRender::new);
        event.registerEntityRenderer(MVSEntityRegistry.CORE_MELT_RING.get(), CMRingRender::new);
        event.registerEntityRenderer(MVSEntityRegistry.CMR_AREA_EFFECT_CLOUD.get(), NoopRenderer::new);
    }

    private static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(
                new SimpleClientFluidType(
                        ResourceLocation.fromNamespaceAndPath(MikusVocalSpellIronsSpellsAddon.MODID, "block/potion_fluid")
                ),
                MVSFluidRegistry.POTION_TYPE
        );
    }

    public static void addLayer(EntityRenderersEvent.AddLayers event) {
        for (EntityType<?> type : event.getEntityTypes()) {
            var renderer = event.getRenderer(type);
            if (renderer instanceof LivingEntityRenderer living) {
                attachRenderLayers(living);
            }
        }

        event.getSkins().forEach(renderer -> {
            PlayerRenderer skin = event.getSkin(renderer);
            if (skin != null) {
                attachRenderLayers(skin);
            }
        });
    }
    private static  <T extends LivingEntity, M extends EntityModel<T>> void attachRenderLayers(LivingEntityRenderer<T, M> renderer) {
        renderer.addLayer(new InnocenceShellLayer<>(renderer, InnocenceShellEntity::new));
    }
}