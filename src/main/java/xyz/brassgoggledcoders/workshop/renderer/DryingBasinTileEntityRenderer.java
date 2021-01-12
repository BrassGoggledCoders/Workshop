package xyz.brassgoggledcoders.workshop.renderer;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.brassgoggledcoders.workshop.tileentity.DryingBasinTileEntity;
import xyz.brassgoggledcoders.workshop.util.FluidRenderer;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(value = Dist.CLIENT)
public class DryingBasinTileEntityRenderer extends TileEntityRenderer<DryingBasinTileEntity> {

    public DryingBasinTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Deprecated
    @Override
    @ParametersAreNonnullByDefault
    public void render(DryingBasinTileEntity tileEntityIn, float v, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight, int combinedOverlay) {
        if (tileEntityIn.hasWorld()) {
            if (!tileEntityIn.getOutputInventory().getStackInSlot(0).isEmpty()) {
                renderItem(tileEntityIn, tileEntityIn.getOutputInventory().getStackInSlot(0), stack, buf, combinedLight, combinedOverlay);
            } else if (!tileEntityIn.getInputInventory().getStackInSlot(0).isEmpty()) {
                renderItem(tileEntityIn, tileEntityIn.getInputInventory().getStackInSlot(0), stack, buf, combinedLight, combinedOverlay);
            }
            if (!tileEntityIn.getInputFluidTank().isEmpty()) {
                renderFluidBlock(tileEntityIn.getInputFluidTank(), stack, buf, combinedLight);
            }
        }
    }


    //Inventory Item Renderer
    private void renderItem(TileEntity tile, ItemStack item, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight, int combinedOverlay) {
        stack.push();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        if (item.getItem() instanceof BlockItem) {
            stack.translate(0.14, 0.2, 0.14);
            stack.scale(0.73F, 0.8F, 0.73F);
            Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(Block.getBlockFromItem(item.getItem()).getDefaultState(), stack, buf, combinedLight, combinedOverlay);
        } else {
            stack.translate(0.5, 0.4, 0.5);
            stack.rotate(Vector3f.XP.rotationDegrees(90));
            stack.scale(0.8F, 1, 0.8F);
            itemRenderer.renderItem(null, item, ItemCameraTransforms.TransformType.FIXED, false, stack, buf,
                    tile.getWorld(), combinedLight, combinedOverlay);
        }
        stack.pop();
    }

    private void renderFluidBlock(FluidTankComponent<?> tank, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight) {
        stack.push();
        IVertexBuilder builder = buf.getBuffer(FluidRenderer.getBlockRenderType());
        float minY = 3.1F;
        float maxY = 15F;
        float sections = (maxY - minY) / tank.getCapacity();
        float height = (float) tank.getFluidAmount() * sections;
        FluidRenderer.renderScaledFluidCuboid(tank.getFluid(), stack, builder, combinedLight, 2F, minY, 2F, 13.9F, minY + height, 13.9F);
        stack.pop();
    }

}
