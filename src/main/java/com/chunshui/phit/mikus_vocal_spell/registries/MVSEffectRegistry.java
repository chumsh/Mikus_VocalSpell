package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.effects.vocal.reincarnation.*;
import com.chunshui.phit.mikus_vocal_spell.effects.vocal.scallon.ScallionEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MVSEffectRegistry {
    private static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, MikusVocalSpellIronsSpellsAddon.MODID);

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }

    //声之法术效果
    /*Reincarnation*/
    public static final DeferredHolder<MobEffect, MobEffect> REVIVE_BUFF =
            EFFECTS.register("revive_buff", () -> new ReincarnationEffect(
                    MobEffectCategory.BENEFICIAL,
                    0x000000
            ));
    public static final DeferredHolder<MobEffect, MobEffect> PERFORMER_EFFECT =
            EFFECTS.register("performer_effect", () -> new PerformerEffect(
                    MobEffectCategory.HARMFUL,
                    0x000000
            ));
    public static final DeferredHolder<MobEffect, MobEffect>  INVENTOR_EFFECT =
            EFFECTS.register("inventor_effect", () -> new InventorEffect(
                    MobEffectCategory.HARMFUL,
                0x000000
            ));
    public static final DeferredHolder<MobEffect, MobEffect>  MESSIAH_EFFECT =
            EFFECTS.register("messiah_effect", () -> new MessiahEffect(
            MobEffectCategory.HARMFUL,
            0x000000
    ));
    public static final DeferredHolder<MobEffect, MobEffect> REVOLUTIONARY_EFFECT =
            EFFECTS.register("revolutionary_effect", () -> new RevolutionaryEffect(
            MobEffectCategory.HARMFUL,
            0x000000
    ));

    public static final DeferredHolder<MobEffect, MobEffect> ADVENTURER_EFFECT =
            EFFECTS.register("adventurer_effect", () -> new AdventurerEffect(
            MobEffectCategory.HARMFUL,
            0x000000
    ));

    /*Scallion*/
    public static final DeferredHolder<MobEffect, MobEffect> SCALLION_EFFECT =
            EFFECTS.register("scallion_effect", ()-> new ScallionEffect(
            MobEffectCategory.BENEFICIAL,
            0x000000
    ));
}
