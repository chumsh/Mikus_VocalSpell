package com.chunshui.phit.mikus_vocal_spell.datagen;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSFluidRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSItemRegistry;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.BrewAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.EmptyAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MVSIronRecipeProvider extends RecipeProvider {
    public MVSIronRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        new EmptyAlchemistCauldronRecipe.Builder()
                .withInput(MVSItemRegistry.EMPTY_MANA_POTION.get())
                .withReturnItem(MVSItemRegistry.MANA_POTION.get())
                .withFluid(new net.neoforged.neoforge.fluids.FluidStack(MVSFluidRegistry.POTION_FLUID.get(), 250))
                .withSound(SoundEvents.BUCKET_FILL)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MikusVocalSpellIronsSpellsAddon.MODID, "alchemist_cauldron/empty_recipe"));

        BrewAlchemistCauldronRecipe.builder()
                .withInput(new FluidStack(Fluids.WATER, 1000))
                .withReagent(ItemRegistry.ARCANE_ESSENCE.get())
                .withReagent(ItemRegistry.ARCANE_ESSENCE.get())
                .withReagent(MVSItemRegistry.VOCAL_ESSENCE.get())
                .withResult(new FluidStack(MVSFluidRegistry.POTION_FLUID.get(), 250))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MikusVocalSpellIronsSpellsAddon.MODID, "alchemist_cauldron/brew_recipe"));
    }



}
