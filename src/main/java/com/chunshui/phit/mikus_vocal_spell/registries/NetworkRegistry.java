package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.network.CurrentFormSync;
import com.chunshui.phit.mikus_vocal_spell.network.SyncInnocenceDataPacket;
import com.chunshui.phit.mikus_vocal_spell.network.SyncReviveDataPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class NetworkRegistry {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registries = event.registrar(MikusVocalSpellIronsSpellsAddon.MODID);

        registries.playToClient(
            SyncReviveDataPacket.TYPE,
            SyncReviveDataPacket.STREAM_CODEC,
            SyncReviveDataPacket::handle
        );

        registries.playToClient(
            SyncInnocenceDataPacket.TYPE,
            SyncInnocenceDataPacket.STREAM_CODEC,
            SyncInnocenceDataPacket::handle
        );

        registries.playBidirectional(
                CurrentFormSync.TYPE,
                CurrentFormSync.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        CurrentFormSync::nullHandler,
                        CurrentFormSync::handler
                )
        );
    }




}
