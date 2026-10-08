package com.chunshui.phit.mikus_vocal_spell.recipe;

import com.chunshui.phit.mikus_vocal_spell.registries.RecipeRegistry;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record EchoAltarRecipe(NonNullList<Ingredient> inputItem, ItemStack result) implements Recipe<EchoAltarRecipe.EchoAltarInput> {

    @Override
    public boolean matches(@NotNull EchoAltarInput echoAltarInput, @NotNull Level level) {
        return isSameItemStacks(echoAltarInput);
    }

    private boolean isSameItemStacks(EchoAltarInput echoAltarInput) {
        boolean isSame = false;
        if (this.inputItem.size() != echoAltarInput.stacks.size()) return false;
        for (int i = 0; i < this.inputItem.size(); i++) {
            isSame = inputItem.get(i).test(echoAltarInput.stacks.get(i));
            if (!isSame)
                break;
        }
        return isSame;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull EchoAltarInput echoAltarInput, HolderLookup.@NotNull Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int length) {
        return width * length >= 1;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider provider) {
        return this.result;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return inputItem();
    }



    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.ECHO_ALTAR_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeRegistry.ECHO_ALTAR_TYPE.get();
    }

    public record EchoAltarInput(NonNullList<ItemStack> stacks) implements RecipeInput {

        @Override
        public @NotNull ItemStack getItem(int slot) {
            return this.stacks().get(slot);
        }


        @Override
        public int size() {
            return this.stacks.size();
        }
    }

    public static class Serializer implements RecipeSerializer<EchoAltarRecipe> {

        public static final MapCodec<EchoAltarRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.listOf().fieldOf("input_items").forGetter(EchoAltarRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(EchoAltarRecipe::result)
        ).apply(instance, (inputItems, result) -> new EchoAltarRecipe(NonNullList.copyOf(inputItems), result)));

        public static final StreamCodec<RegistryFriendlyByteBuf, EchoAltarRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), EchoAltarRecipe::inputItem,
                        ItemStack.STREAM_CODEC, EchoAltarRecipe::result,
                        (inputItems, result) -> new EchoAltarRecipe(NonNullList.copyOf(inputItems), result)
                );

        @Override
        public @NotNull MapCodec<EchoAltarRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, EchoAltarRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static class Builder implements RecipeBuilder {
        private final NonNullList<Ingredient> inputItems = NonNullList.withSize(4, Ingredient.EMPTY);
        private ItemStack result;

        public Builder addItem(Item item) {
            for (int i = 0; i < this.inputItems.size(); i++) {
                if (inputItems.get(i).isEmpty()) {
                    inputItems.set(i, Ingredient.of(item));
                    break;
                }
            }
            return this;
        }

        public Builder addItemWithTag(TagKey<Item> tag) {
            for (int i = 0; i < this.inputItems.size(); i++) {
                if (inputItems.get(i).isEmpty()) {
                    inputItems.set(i, Ingredient.of(tag));
                    break;
                }
            }
            return this;
        }

        public Builder setResult(ItemStack result) {
            this.result = result;
            return this;
        }

        @Override
        public @NotNull RecipeBuilder unlockedBy(@NotNull String s, @NotNull Criterion<?> criterion) {
            return this;
        }

        @Override
        public @NotNull RecipeBuilder group(@Nullable String s) {
            return this;
        }

        @Override
        public @NotNull Item getResult() {
            return this.result.getItem();
        }

        @Override
        public void save(@NotNull RecipeOutput recipeOutput, @NotNull ResourceLocation id) {
            recipeOutput.accept(id, new EchoAltarRecipe(this.inputItems, this.result), null);
        }
    }
}
