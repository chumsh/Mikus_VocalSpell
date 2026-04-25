package com.chunshui.phit.mikus_vocal_spell.datagen;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSDamageType;
import net.minecraft.core.HolderLookup;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MVSDamageTypeTagGenerator extends TagsProvider<DamageType> {

    public MVSDamageTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, MikusVocalSpellIronsSpellsAddon.MODID, existingFileHelper);
    }

    private static TagKey<DamageType> create() {
        return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MikusVocalSpellIronsSpellsAddon.MODID, "vocal_magic"));
    }

    public static final TagKey<DamageType> VOCAL_MAGIC = create();


    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(VOCAL_MAGIC).add(MVSDamageType.VOCAL_MAGIC);
    }
}
