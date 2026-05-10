package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MVSCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MikusVocalSpellIronsSpellsAddon.MODID);

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }

    public static final Supplier<CreativeModeTab> MVS_CREATIVE_MODE_TAB = TABS.register(
            "mvs_creative_mode_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(MVSItemRegistry.VOCAL_UPGRADE_ORB.get()) )
                    .title(Component.translatable("itemgroup.mikus_vocal_spell.main"))
                    .displayItems(((parameters, output) -> {
                        output.accept(MVSItemRegistry.VOCAL_UPGRADE_ORB.get());
                        output.accept(MVSItemRegistry.MIKU_STAFF.get());
                        output.accept(MVSItemRegistry.MANA_POTION.get());
                        output.accept(MVSItemRegistry.EMPTY_MANA_POTION.get());
                        output.accept(MVSItemRegistry.VOCAL_ESSENCE.get());
                    }))
                    .build()
            );
    }
