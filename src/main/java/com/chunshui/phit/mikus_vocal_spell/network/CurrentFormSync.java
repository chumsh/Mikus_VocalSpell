package com.chunshui.phit.mikus_vocal_spell.network;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.AttachmentRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public record CurrentFormSync(int currentForm) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CurrentFormSync> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(
                    MikusVocalSpellIronsSpellsAddon.MODID, "current_forms_sync"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, CurrentFormSync> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    CurrentFormSync::currentForm,
                    CurrentFormSync::new
            );

    public static void handler(CurrentFormSync data, IPayloadContext context) {
        context.enqueueWork (() -> {
            int form = data.currentForm();
            context.player().setData(AttachmentRegistry.CURRENT_FORM, form);
        });
    }

    public static void nullHandler(CurrentFormSync data, IPayloadContext context) {}

}
