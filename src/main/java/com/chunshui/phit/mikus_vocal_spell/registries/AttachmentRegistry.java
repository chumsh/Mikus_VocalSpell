package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AttachmentRegistry {
    private static final DeferredRegister<AttachmentType<?>> TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MikusVocalSpellIronsSpellsAddon.MODID);

    public static void register(IEventBus bus) {
        TYPES.register(bus);
    }

    /*ConvertibleSpellData*/
    public static final Supplier<AttachmentType<Integer>> CURRENT_FORM = TYPES.register("current_form", () -> AttachmentType.builder(() -> 1) .build());

    /*VocalSpell */
    // InnocenceEffect
    public static final Supplier<AttachmentType<Float>> TOTAL_DAMAGE = TYPES.register("total_damage", () -> AttachmentType.builder(() -> 0F).build());
    public static final Supplier<AttachmentType<Integer>> CORE_MELT_LEVEL  = TYPES.register("core_melt_level", () -> AttachmentType.builder(() -> 0).build());
    // Reincarnation
    public static final Supplier<AttachmentType<Boolean>> ENSURE_CANCEL_REVIVE = TYPES.register("ensure_cancel_revive", () -> AttachmentType.builder(() -> false).sync(ByteBufCodecs.BOOL).serialize(Codec.BOOL).build());
    public static final Supplier<AttachmentType<Integer>> REVIVE_CHANCE = TYPES.register("revive_chance", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());
    public static final Supplier<AttachmentType<Boolean>> SHOULD_SEND_MESSAGE = TYPES.register("should_send_message", () -> AttachmentType.builder(() -> true).serialize(Codec.BOOL).build());
    //NomNomSong
    public static final Supplier<AttachmentType<Integer>> CURRENT_CHARGE_OF_LUO = TYPES.register("current_charge_of_luo", () -> AttachmentType.builder(() -> -1).build());
    public static final Supplier<AttachmentType<Float>> NOM_NOM_SONG_BASE_DAMAGE = TYPES.register("nom_nom_song_base_damage", () -> AttachmentType.builder(() -> 0F).build());
}
