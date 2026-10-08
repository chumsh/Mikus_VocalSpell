package com.chunshui.phit.mikus_vocal_spell.block.block_entity;

import com.chunshui.phit.mikus_vocal_spell.recipe.EchoAltarRecipe;
import com.chunshui.phit.mikus_vocal_spell.registries.MVSBlockRegistry;
import com.chunshui.phit.mikus_vocal_spell.registries.RecipeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Collections;
import java.util.Optional;

public class EchoAltarBE extends BlockEntity implements GeoBlockEntity, WorldlyContainer {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final NonNullList<ItemStack> inputItems = NonNullList.withSize(4, ItemStack.EMPTY);

    public EchoAltarBE(BlockPos pos, BlockState blockState) {
        super(MVSBlockRegistry.ECHO_ALTAR_BE.get(), pos, blockState);
    }

    public NonNullList<ItemStack> getInputItems() {
        return inputItems;
    }

    //--------------------------------------------使用逻辑-----------------------------------------------------------------------------------
    public ItemInteractionResult handleUse(ItemStack itemStack, Player player, InteractionHand hand) {

        if (canBeConsumed(itemStack, player)) {
            if (this.level != null && !this.level.isClientSide) {
                for (int i = 0; i < inputItems.size(); i++) {
                    if (inputItems.get(i).isEmpty()) {
                        ItemStack input = itemStack.split(1);
                        inputItems.set(i, input);
                        setChanged();
                        break;
                    }
                }
                ItemStack output = tryCrafting();
                if (!output.isEmpty()) {
                    for (int i = 0; i < inputItems.size(); i++) {
                        if (inputItems.get(i).isEmpty()) {
                            inputItems.set(i, output);
                            break;
                        }
                    }
                    setChanged();
                }
            }
            return ItemInteractionResult.SUCCESS;
        }

        if (itemStack.isEmpty() || player.isCrouching()) {  //TODO: 无法保证手持方块时正确使用
            return handleTake(player, hand);
        }

        return ItemInteractionResult.CONSUME;
    }

    private boolean canBeConsumed(ItemStack itemStack, Player player) {
        return !itemStack.isEmpty() && !player.isCrouching();
    }

    public ItemInteractionResult handleTake(Player player, InteractionHand hand) {
        if (this.level != null && !this.level.isClientSide) {
            for (int i = inputItems.size() - 1; i >= 0; i--) {
                if (!inputItems.get(i).isEmpty()) {
                    ItemStack take = inputItems.get(i).split(1);
                    if (player.getMainHandItem().isEmpty())
                        player.setItemInHand(hand, take);
                    else if (!player.getInventory().add(take))
                        player.drop(take, false);
                    setChanged();
                    break;
                }
            }
        }
        return ItemInteractionResult.CONSUME;
    }

    private ItemStack tryCrafting() {
        if (!(this.level instanceof ServerLevel serverLevel))
            return ItemStack.EMPTY;
        RecipeManager recipes = serverLevel.getRecipeManager();
        EchoAltarRecipe.EchoAltarInput input = new EchoAltarRecipe.EchoAltarInput(this.inputItems);
        Optional<RecipeHolder<EchoAltarRecipe>> recipeMap = recipes.getRecipeFor(RecipeRegistry.ECHO_ALTAR_TYPE.get(), input, serverLevel);

        ItemStack result = recipeMap.map(RecipeHolder::value).map(map -> map.assemble(input, level.registryAccess())).orElse(ItemStack.EMPTY);
        if (!result.isEmpty()) clearContent();
        return result;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EchoAltarBE blockEntity) {

    }


    //--------------------------------------------同步与持久化-----------------------------------------------------------------------------------
    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public void onDataPacket(@NotNull Connection net, @NotNull ClientboundBlockEntityDataPacket pkt, HolderLookup.@NotNull Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);

        handleUpdateTag(pkt.getTag(), lookupProvider);
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookupProvider) {
        loadAdditional(tag, lookupProvider);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return false;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        clearContent();
        ContainerHelper.loadAllItems(tag, this.inputItems, registries);
        super.loadAdditional(tag, registries);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        ContainerHelper.saveAllItems(tag, this.inputItems, true, registries);
        super.saveAdditional(tag, registries);

    }
//--------------------------------------------Geckolib接口-----------------------------------------------------------------------------------
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

//--------------------------------------------容器接口-----------------------------------------------------------------------------------
    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction direction) {
        return new int[]{0,1,2,3,4,5,6,7,8};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        return direction != Direction.DOWN && this.inputItems.get(slot).isEmpty();
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        return direction == Direction.DOWN;
    }

    @Override
    public void clearContent() {
        Collections.fill(inputItems, ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public boolean isEmpty() {
        return this.inputItems.isEmpty();
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        if (i >= this.inputItems.size())
            return ItemStack.EMPTY;
        return this.inputItems.get(i);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(inputItems, slot, amount);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        int count = inputItems.get(slot).getCount();
        return  slot>= 0 && slot < inputItems.size() ? inputItems.get(slot).split(count) : ItemStack.EMPTY;}

    @Override
    public void setItem(int slot, @NotNull ItemStack itemStack) {
        this.inputItems.set(slot, itemStack);
    }
}
