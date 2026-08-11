package com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import java.util.function.Supplier;

public class CoreMeltRender extends GeoEntityRenderer<CoreMeltEntity> {
    public CoreMeltRender(EntityRendererProvider.Context renderManager, Supplier<GeoModel<CoreMeltEntity>> model) {
        super(renderManager, model.get());

        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public boolean shouldRender(@NotNull CoreMeltEntity entity, @NotNull Frustum frustum, double camX, double camY, double camZ) {
        double xDistance = camX - entity.getX();
        double yDistance = camY - entity.getY();
        double zDistance = camZ - entity.getZ();
        double distance = xDistance * xDistance + yDistance * yDistance + zDistance * zDistance;

        return Math.sqrt(distance) >= 2;
    }
}
