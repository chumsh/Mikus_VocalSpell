package com.chunshui.phit.mikus_vocal_spell.tags;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BlockTags {
    public static final TagKey<Block> NEEDS_IRON_TOOL = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("minecraft", "needs_iron_tool"));
    public static final TagKey<Block> MINEABLE_WITH_PICKAXE = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("minecraft", "mineable/pickaxe"));
}
