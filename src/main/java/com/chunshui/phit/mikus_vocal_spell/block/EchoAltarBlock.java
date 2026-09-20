package com.chunshui.phit.mikus_vocal_spell.block;

import com.chunshui.phit.mikus_vocal_spell.block.block_entity.EchoAltarBE;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSBlockRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EchoAltarBlock extends BaseEntityBlock {
    public EchoAltarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new EchoAltarBE(blockPos, blockState);
    }

    public @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult blockHitResult) {
        if (level.getBlockEntity(pos) instanceof EchoAltarBE blockEntity) {
            return blockEntity.handleUse(stack, player, hand);
        }

        return super.useItemOn(stack, state, level, pos, player, hand, blockHitResult);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return createTickerHelper(type, MVSBlockRegistry.ECHO_ALTAR_BE.get(), EchoAltarBE::tick);
    }


    @Override
    protected @NotNull MapCodec<EchoAltarBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public static final MapCodec<EchoAltarBlock> CODEC = simpleCodec(EchoAltarBlock::new);
}
