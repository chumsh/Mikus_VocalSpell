package com.chunshui.phit.mikus_vocal_spell.registries;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.chunshui.phit.mikus_vocal_spell.block.EchoAltarBlock;
import com.chunshui.phit.mikus_vocal_spell.block.block_entity.EchoAltarBE;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MVSBlockRegistry {
    //-----------------Block-------------------
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MikusVocalSpellIronsSpellsAddon.MODID);
    public static final DeferredBlock<Block> VOCAL_COLUMN_BLOCK = BLOCKS.register(
            "vocal_column_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .destroyTime(2)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.BAMBOO)
                    .lightLevel(state -> 5)
            ));
    public static final DeferredBlock<Block> ECHO_ALTAR_BLOCK = BLOCKS.register(
            "echo_altar_block",
            () -> new EchoAltarBlock(BlockBehaviour.Properties.of()
                    .lightLevel(state -> 7)
                    .destroyTime(2)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.BAMBOO)
            )
    );

    //-----------------BlockEntity--------------
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MikusVocalSpellIronsSpellsAddon.MODID);

    public static final Supplier<BlockEntityType<EchoAltarBE>> ECHO_ALTAR_BE = BLOCK_ENTITIES.register(
            "echo_altar_block_entity",
            () -> BlockEntityType.Builder.of(
                    EchoAltarBE::new,
                    ECHO_ALTAR_BLOCK.get()
            ).build(null)
    );

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}
