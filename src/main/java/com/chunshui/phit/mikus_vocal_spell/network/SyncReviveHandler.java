package com.chunshui.phit.mikus_vocal_spell.network;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSItemRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSSoundRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

//TODO:以后会优化为最简化形式
public record SyncReviveHandler(int chance) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncReviveHandler> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(MikusVocalSpellIronsSpellsAddon.MODID, "sync_revive_handler"));

    public static final StreamCodec<ByteBuf, SyncReviveHandler> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SyncReviveHandler::chance,
            SyncReviveHandler::new
    );

    public static void clientHandler(SyncReviveHandler data, IPayloadContext context) {
        context.enqueueWork(() -> playReviveAnimation(data));

    }

    private static void playReviveAnimation(SyncReviveHandler data) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        player.playSound(MVSSoundRegistry.EAT_APPLE.get(), 1.0f, 1.0f);
        int chance = data.chance();
        if (chance == 2) {
            Minecraft.getInstance().gameRenderer.displayItemActivation(MVSItemRegistry.REINCARNATION_APPLE.get().getDefaultInstance());
        } else if (chance == 1) {
            Minecraft.getInstance().gameRenderer.displayItemActivation(MVSItemRegistry.REINCARNATION_APPLE_TWO.get().getDefaultInstance());
        } else if (chance == 0) {
            Minecraft.getInstance().gameRenderer.displayItemActivation(MVSItemRegistry.REINCARNATION_APPLE_THREE.get().getDefaultInstance());
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
