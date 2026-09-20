package com.chunshui.phit.mikus_vocal_spell.block.block_renderer;

import com.chunshui.phit.mikus_vocal_spell.block.block_entity.EchoAltarBE;
import com.chunshui.phit.mikus_vocal_spell.block.block_renderer.model.EchoAltarModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class EchoAltarRenderer extends GeoBlockRenderer<EchoAltarBE> {
    ItemRenderer itemRenderer;
    public EchoAltarRenderer(BlockEntityRendererProvider.Context context) {
        super(new EchoAltarModel());
        itemRenderer = context.getItemRenderer();
    }
    float count = 0;


    @Override
    public void actuallyRender(PoseStack poseStack, EchoAltarBE animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        count ++;
        NonNullList<ItemStack> inputItems = animatable.getInputItems();

        for (int i = 0; i < inputItems.size(); i++) {
            ItemStack inputItem = inputItems.get(i);
           // MikusVocalSpellIronsSpellsAddon.LOGGER.debug("i :{}{}", i, inputItem.getDisplayName().getString());
            if (!inputItem.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0, 1, 0);
                poseStack.mulPose(Axis.YP.rotation((float) (( count + partialTick) * Math.PI / 200)));
                float angle = (float) (2 * Math.PI * i/ inputItems.size());
                float x = Mth.sin(angle);
                float z = Mth.cos(angle);

                poseStack.translate(x, 0.2, z);

                itemRenderer.renderStatic(inputItem, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, animatable.getLevel(), 0);
                poseStack.popPose();
            }
        }
    }
}
