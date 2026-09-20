package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.recipe.EchoAltarRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RecipeRegistries {
    //----------------Recipe-Types--------------------
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MikusVocalSpellIronsSpellsAddon.MODID);

    public static final Supplier<RecipeType<EchoAltarRecipe>> ECHO_ALTAR_TYPE = RECIPE_TYPES.register(
            "echo_altar_recipe_type",
            () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(
                    MikusVocalSpellIronsSpellsAddon.MODID, "echo_altar_recipe_type"
            )));

    //---------------RecipeSerializer-----------------
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MikusVocalSpellIronsSpellsAddon.MODID);

    public static final Supplier<RecipeSerializer<EchoAltarRecipe>> ECHO_ALTAR_RECIPE_SERIALIZER = SERIALIZERS.register(
            "echo_altar", EchoAltarRecipe.Serializer::new
    );

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        SERIALIZERS.register(eventBus);
    }
}
