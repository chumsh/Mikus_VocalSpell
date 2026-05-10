package com.chunshui.phit.mikus_vocal_spell;

import com.chunshui.phit.mikus_vocal_spell.network.SyncReviveDataPacket;
import com.chunshui.phit.mikus_vocal_spell.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

@Mod(MikusVocalSpellIronsSpellsAddon.MODID)
public class MikusVocalSpellIronsSpellsAddon {
    public static final String MODID = "mikus_vocal_spell";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MikusVocalSpellIronsSpellsAddon(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(MikusVocalSpellIronsSpellsAddon::registerPayloadHandlers);

        MVSItemRegistry.register(modEventBus);
        MVSAttributeRegistry.register(modEventBus);
        MVSSchoolRegistry.register(modEventBus);
        VocalSpellRegistry.register(modEventBus);
        MVSEffectRegistry.register(modEventBus);
        MVSSoundRegistry.register(modEventBus);
        ParticleRegistries.register(modEventBus);
        MVSEntityRegistry.register(modEventBus);
        MVSCreativeTab.register(modEventBus);
        MVSFluidRegistry.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Common setup complete!");
    }
    
    private static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(MODID).versioned("1").optional();
        
        registrar.playToClient(
            SyncReviveDataPacket.TYPE,
            SyncReviveDataPacket.STREAM_CODEC,
            new DirectionalPayloadHandler<>(SyncReviveDataPacket::handle, SyncReviveDataPacket::handle)
        );
        
        LOGGER.info("Network packets registered successfully!");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }



    public static ResourceLocation id(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath(MikusVocalSpellIronsSpellsAddon.MODID, path);
    }
}
