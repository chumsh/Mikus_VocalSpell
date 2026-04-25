package com.chunshui.phit.mikus_vocal_spell.datagen;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSDamageType;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSUpgradeOrbTypeRegistry;
import io.redspace.ironsspellbooks.registries.FeatureRegistry;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;


import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MVSRegistryDataGenerator extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS, FeatureRegistry::bootstrapBiomeModifier)
            .add(Registries.CONFIGURED_FEATURE, FeatureRegistry::bootstrapConfiguredFeature)
            .add(Registries.DAMAGE_TYPE, MVSDamageType::bootstrap)
            .add(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY, MVSUpgradeOrbTypeRegistry::bootStrap);
    public MVSRegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of("minecraft", MikusVocalSpellIronsSpellsAddon.MODID));
    }
}
