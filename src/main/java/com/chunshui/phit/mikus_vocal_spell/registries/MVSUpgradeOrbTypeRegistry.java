package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import static io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY;

public class MVSUpgradeOrbTypeRegistry {


    public static ResourceKey<UpgradeOrbType> VOCAL_SPELL_POWER = ResourceKey.create(UPGRADE_ORB_REGISTRY_KEY, MikusVocalSpellIronsSpellsAddon.id("vocal_power"));

    public static void bootStrap(BootstrapContext<UpgradeOrbType> bootStrap){
        bootStrap.register(VOCAL_SPELL_POWER,
            new UpgradeOrbType(MVSAttributeRegistry.VOCAL_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, MVSItemRegistry.VOCAL_UPGRADE_ORB));
    }
}
