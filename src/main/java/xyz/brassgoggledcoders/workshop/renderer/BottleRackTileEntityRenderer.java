package xyz.brassgoggledcoders.workshop.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.brassgoggledcoders.workshop.block.RotatableTileBlock;
import xyz.brassgoggledcoders.workshop.tileentity.BottleRackTileEntity;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import java.util.concurrent.atomic.AtomicReference;

@OnlyIn(value = Dist.CLIENT)
public class BottleRackTileEntityRenderer extends TileEntityRenderer<BottleRackTileEntity> {
    public BottleRackTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(BottleRackTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        AtomicReference<Double> offset = new AtomicReference<>(0.2);
        InventoryUtil.getItemStackStream(tileEntityIn.getFillingInventory()).forEach(stack -> {
            matrixStackIn.push();
            matrixStackIn.rotate(Vector3f.XN.rotationDegrees(tileEntityIn.getBlockState().get(RotatableTileBlock.FACING).getHorizontalAngle()));
            matrixStackIn.translate(offset.updateAndGet(d -> d += 0.2), 0.5, 0.5);
            matrixStackIn.scale(0.8F, 1, 0.8F);
            itemRenderer.renderItem(null, stack, ItemCameraTransforms.TransformType.FIXED, false, matrixStackIn, bufferIn,
                    tileEntityIn.getWorld(), combinedLightIn, combinedOverlayIn);
            matrixStackIn.pop();
        });
    }
}
