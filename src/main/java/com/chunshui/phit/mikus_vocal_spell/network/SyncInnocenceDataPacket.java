package com.chunshui.phit.mikus_vocal_spell.network;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import io.netty.buffer.ByteBuf;
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

    public static final Type<SyncInnocenceDataPacket> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(
            MikusVocalSpellIronsSpellsAddon.MODID, "sync_innocence_data"));


    public SyncInnocenceDataPacket(boolean hasInnocence, int duration/*, int id*/) {
        this.hasInnocence = hasInnocence;
        this.duration = duration;
        //this.id = id;
    }

    private  SyncInnocenceDataPacket(ByteBuf buf) {
        this.hasInnocence = buf.readBoolean();
        this.duration = buf.readInt();
    }

    public void write(ByteBuf buf) {
        buf.writeBoolean(hasInnocence);
        buf.writeInt(duration);
    }

    public static final StreamCodec<ByteBuf, SyncInnocenceDataPacket> STREAM_CODEC = StreamCodec.ofMember(
            SyncInnocenceDataPacket::write,
            SyncInnocenceDataPacket::new
    );

    //TODO::未来尝试将适用范围扩展至非玩家实体
    public static void handle(SyncInnocenceDataPacket packet, IPayloadContext context) {
//        MikusVocalSpellIronsSpellsAddon.LOGGER.debug("Innocence Data Packet Received");
        context.enqueueWork(() -> {
//            MikusVocalSpellIronsSpellsAddon.LOGGER.info("Innocence Data Packet Received");
            context.player().getPersistentData().putBoolean(NBTKeyHelper.HAS_INNOCENCE, packet.hasInnocence);
            context.player().getPersistentData().putInt(NBTKeyHelper.INNOCENCE_DURATION, packet.duration);
//            Entity entity= context.player().level().getEntity(packet.id());
//            if (entity == null)
//                return;
//            if (entity instanceof Player)
//                return;
//            if (!(entity instanceof LivingEntity))
//                return;
//            entity.getPersistentData().putInt(NBTKeyHelper.INNOCENCE_DURATION, packet.duration());
//            entity.getPersistentData().putBoolean(NBTKeyHelper.HAS_INNOCENCE, packet.hasInnocence());
    });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
