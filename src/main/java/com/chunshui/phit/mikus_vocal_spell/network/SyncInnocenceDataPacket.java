package com.chunshui.phit.mikus_vocal_spell.network;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

//官方简写形式无效，尝试常规方法
//TODO::未来尝试把数据包简化为最简形式
public class SyncInnocenceDataPacket implements CustomPacketPayload {
    private boolean hasInnocence;
    private int duration;
    private boolean shouldRelease;

    public static final Type<SyncInnocenceDataPacket> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID, "sync_innocence_data"));


    public SyncInnocenceDataPacket(boolean hasInnocence, int duration/*, int id*/) {
        this.hasInnocence = hasInnocence;
        this.duration = duration;
        //this.id = id;
    }

    private  SyncInnocenceDataPacket(FriendlyByteBuf buf) {
        this.hasInnocence = buf.readBoolean();
        this.duration = buf.readInt();
        this.shouldRelease = buf.readBoolean();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(hasInnocence);
        buf.writeInt(duration);
        buf.writeBoolean(shouldRelease);
    }

    public static final StreamCodec<FriendlyByteBuf, SyncInnocenceDataPacket> STREAM_CODEC = StreamCodec.ofMember(
            SyncInnocenceDataPacket::write,
            SyncInnocenceDataPacket::new
    );

    //TODO::未来尝试将适用范围扩展至非玩家实体
    public static void handle(SyncInnocenceDataPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            context.player().getPersistentData().putBoolean(NBTKeyHelper.HAS_INNOCENCE, packet.hasInnocence);
            context.player().getPersistentData().putInt(NBTKeyHelper.INNOCENCE_DURATION, packet.duration);
    });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
