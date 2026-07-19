package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.network.SyncInnocenceDataPacket;
import com.chunshui.phit.mikus_vocal_spell.network.SyncReviveDataPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = MikusVocalSpellIronsSpellsAddon.MODID)
public class NetworkRegistry {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registries = event.registrar(MikusVocalSpellIronsSpellsAddon.MODID).versioned("1").optional();

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
    }




}
