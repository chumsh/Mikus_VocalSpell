package com.chunshui.phit.mikus_vocal_spell.registries;


import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.spells.vocal.*;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.redspace.ironsspellbooks.api.registry.SpellRegistry.SPELL_REGISTRY_KEY;

public class VocalSpellRegistry {


    private static final DeferredRegister<AbstractSpell> VOCAL_SPELLS = DeferredRegister.create(SPELL_REGISTRY_KEY, MikusVocalSpellIronsSpellsAddon.MODID);



    public static void register(IEventBus eventBus) {
        VOCAL_SPELLS.register(eventBus);
    }

    private static Supplier<AbstractSpell> registerSpell(AbstractSpell spell) {
        return VOCAL_SPELLS.register(spell.getSpellName(), () -> spell);
    }

    //VocalSpells注册

    public static final Supplier<AbstractSpell> REINCARNATION_SEED = registerSpell(new ReincarnationSeed());

    public static final Supplier<AbstractSpell> SCALLION_DANCE = registerSpell(new ScallionDance());

    public static final Supplier<AbstractSpell> MANA_MUNCH_MELODY = registerSpell(new ManaMunchMelody());

    public static final Supplier<AbstractSpell> CORE_MELT = registerSpell(new CoreMelt());

    public static final Supplier<AbstractSpell> PRIMAL_RAMPAGE = registerSpell(new PrimalRampage());
}
