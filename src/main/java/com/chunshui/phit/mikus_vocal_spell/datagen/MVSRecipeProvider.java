package com.chunshui.phit.mikus_vocal_spell.datagen;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.recipe.EchoAltarRecipe;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSFluidRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSItemRegistry;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.BrewAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.EmptyAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MVSRecipeProvider extends RecipeProvider {
    public MVSRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        new EmptyAlchemistCauldronRecipe.Builder()
                .withInput(MVSItemRegistry.EMPTY_MANA_POTION.get())
                .withReturnItem(MVSItemRegistry.MANA_POTION.get())
                .withFluid(new FluidStack(MVSFluidRegistry.POTION_FLUID.get(), 250))
                .withSound(SoundEvents.BUCKET_FILL)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "alchemist_cauldron/return_magic_potion"));

        BrewAlchemistCauldronRecipe.builder()
                .withInput(new FluidStack(Fluids.WATER, 1000))
                .withReagent(ItemRegistry.ARCANE_ESSENCE.get())
                .withReagent(MVSItemRegistry.VOCAL_ESSENCE.get())
                .withResult(new FluidStack(MVSFluidRegistry.POTION_FLUID.get(), 250))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "alchemist_cauldron/create_magic_potion"));

        new EchoAltarRecipe.Builder()
                .addItem(ItemRegistry.ARCANE_ESSENCE.get())
                .addItem(MVSItemRegistry.VOCAL_FLOWER.get())
                .setResult(new ItemStack(MVSItemRegistry.VOCAL_ESSENCE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MikusVocalSpellIronsSpellsAddon.MODID, "echo_altar/vocal_essence"));
    }



}
