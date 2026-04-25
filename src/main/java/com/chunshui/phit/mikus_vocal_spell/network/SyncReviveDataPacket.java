package com.chunshui.phit.mikus_vocal_spell.network;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.client.spells.ReviveAnimationHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncReviveDataPacket implements CustomPacketPayload {
    
    public static final Type<SyncReviveDataPacket> TYPE = 
        new Type<>(ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID, "sync_revive_data"));
    
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncReviveDataPacket> STREAM_CODEC =
        StreamCodec.ofMember(SyncReviveDataPacket::write, SyncReviveDataPacket::new);
    
    private final int charges;
    private final boolean isActive;
    
    public SyncReviveDataPacket(int charges, boolean isActive) {
        this.charges = charges;
        this.isActive = isActive;

    }
    
    public SyncReviveDataPacket(RegistryFriendlyByteBuf buf) {
        this.charges = buf.readInt();
        this.isActive = buf.readBoolean();

    }
    
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(charges);
        buf.writeBoolean(isActive);
    }
    
    public static void handle(SyncReviveDataPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.flow().getReceptionSide().isClient()) {
                ReviveAnimationHandler.playReviveAnimation(
                    packet.charges
                );
            }
        });
    }
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
