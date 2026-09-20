package com.chunshui.phit.mikus_vocal_spell.datagen;

import com.chunshui.phit.mikus_vocal_spell.registries.MVSBlockRegistry;
import com.chunshui.phit.mikus_vocal_spell.tags.BlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class MVSBlockTagsProvider extends BlockTagsProvider {
    public MVSBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(MVSBlockRegistry.VOCAL_COLUMN_BLOCK.get());
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(MVSBlockRegistry.VOCAL_COLUMN_BLOCK.get());
    }
}
