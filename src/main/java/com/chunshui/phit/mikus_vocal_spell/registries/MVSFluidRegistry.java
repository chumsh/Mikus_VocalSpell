package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import io.redspace.ironsspellbooks.fluids.NoopFluid;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class MVSFluidRegistry {

    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, MikusVocalSpellIronsSpellsAddon.MODID);
    private static final DeferredRegister<FluidType> FLUID_TYPE = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, MikusVocalSpellIronsSpellsAddon.MODID);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
        FLUID_TYPE.register(eventBus);
    }

    public static final DeferredHolder<FluidType, FluidType> POTION_TYPE = FLUID_TYPE.register("potion_fluid_type", () -> new FluidType(FluidType.Properties.create().descriptionId("fluid.mikus_vocal_spell.potion_fluid")));

    public static final DeferredHolder<Fluid, NoopFluid> POTION_FLUID = registerNoop("potion_fluid", POTION_TYPE::value);

    //from Irons SpellBook
    private static DeferredHolder<Fluid, NoopFluid> registerNoop(String name, Supplier<FluidType> fluidType) {
        DeferredHolder<Fluid, NoopFluid> holder = DeferredHolder.create(Registries.FLUID, MikusVocalSpellIronsSpellsAddon.id(name));
        BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(fluidType, holder::value, holder::value).bucket(() -> Items.AIR);
        FLUIDS.register(name, () -> new NoopFluid(properties));
        return holder;
    }
}
