package com.chunshui.phit.mikus_vocal_spell.datagen;

import com.chunshui.phit.mikus_vocal_spell.registries.MVSItemRegistry;
import com.chunshui.phit.mikus_vocal_spell.tags.ItemTags;
import io.redspace.ironsspellbooks.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MVSItemTagsProvider extends ItemTagsProvider {
    public MVSItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(ItemTags.VOCAL_FOCUS)
                .add(MVSItemRegistry.VOCAL_ESSENCE.get());
        this.tag(ModTags.SCHOOL_FOCUS)
                .addTag(ItemTags.VOCAL_FOCUS);

    }
}
