package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
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

    /*VocalSpell */
    // InnocenceEffect
    public static final Supplier<AttachmentType<Float>> TOTAL_DAMAGE = TYPES.register("total_damage", () -> AttachmentType.builder(() -> 0F).build());
    public static final Supplier<AttachmentType<Integer>> CORE_MELT_LEVEL  = TYPES.register("core_melt_level", () -> AttachmentType.builder(() -> 0).build());
}
