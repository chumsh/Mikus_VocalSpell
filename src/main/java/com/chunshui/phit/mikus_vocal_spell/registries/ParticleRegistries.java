package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ParticleRegistries {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, MikusVocalSpellIronsSpellsAddon.MODID);

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }

    public static final Supplier<SimpleParticleType> REINCARNATION_PARTICLE = PARTICLE_TYPES.register("reincarnation", () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> SCALLION_PARTICLE = PARTICLE_TYPES.register("scallion", () -> new SimpleParticleType(false));

}
