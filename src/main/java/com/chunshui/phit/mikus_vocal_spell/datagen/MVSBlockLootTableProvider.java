package com.chunshui.phit.mikus_vocal_spell.datagen;

import com.chunshui.phit.mikus_vocal_spell.registries.MVSBlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class MVSBlockLootTableProvider extends BlockLootSubProvider {

    public MVSBlockLootTableProvider(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected void generate() {
        dropSelf(MVSBlockRegistry.ECHO_ALTAR_BLOCK.get());
        dropSelf(MVSBlockRegistry.VOCAL_COLUMN_BLOCK.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return MVSBlockRegistry.BLOCKS.getEntries()
                .stream()
                .map(e -> (Block) e.value())
                .toList();
    }}
