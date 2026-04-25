package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MVSSoundRegistry {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MikusVocalSpellIronsSpellsAddon.MODID);

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

    // 注册音效 - 使用你需要的音效名称
    public static final DeferredHolder<SoundEvent, SoundEvent> VOCAL_SPELL_CAST =
            SOUND_EVENTS.register("vocal_spell_cast", () ->
                    SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(
                            MikusVocalSpellIronsSpellsAddon.MODID, "vocal_spell_cast"))
            );

    public static final DeferredHolder<SoundEvent, SoundEvent> EAT_APPLE =
            SOUND_EVENTS.register("eat_apple", () ->
                    SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(
                            MikusVocalSpellIronsSpellsAddon.MODID, "eat_apple"))
            );


}
