package xyz.brassgoggledcoders.workshop.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.tileentity.PressTileEntity;
import xyz.brassgoggledcoders.workshop.util.FluidRenderer;

import javax.annotation.ParametersAreNonnullByDefault;

import static xyz.brassgoggledcoders.workshop.block.PressBlock.FACING;
import static xyz.brassgoggledcoders.workshop.content.WorkshopBlocks.PRESS_ARM;

public class PressTileEntityRenderer extends TileEntityRenderer<PressTileEntity> {

    public PressTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Deprecated
    @Override
    @ParametersAreNonnullByDefault
    public void render(PressTileEntity press, float v, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight, int combinedOverlay) {
        if (!press.hasWorld()) {
            return;
        }

        //Fluid Visuals
        renderFluidBlock(press, stack, buf, combinedLight);

        //ArmRender
        renderArm(press, stack, buf, combinedLight, combinedOverlay);

        //Render Item
        renderInventory(press, stack, buf, combinedLight, combinedOverlay);

    }


    //Inventory Item Renderer
    private void renderInventory(PressTileEntity press, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack item = press.getInputInventory().getStackInSlot(0);
        World world = press.getWorld();
        if(world != null) {
            float f = world.getBlockState(press.getPos()).get(FACING).getHorizontalAngle();
            if (!item.isEmpty() && press.getHeightChange() - 0.2 > 0.3 && press.getMachineComponent().getPrimaryBar().getProgress() < press.getMachineComponent().getPrimaryBar().getMaxProgress()/2) {
                stack.push();
                stack.translate(0.5, press.getHeightChange() - 0.2, 0.5);
                if (press.getMachineComponent().getPrimaryBar().getCanIncrease().test(press)) {
                    stack.rotate(Vector3f.ZN.rotationDegrees(f));
                } else {
                    stack.rotate(Vector3f.YP.rotationDegrees(f));
                }
                stack.scale(0.4f, 0.4f, 0.4f);
                itemRenderer.renderItem(null, item, ItemCameraTransforms.TransformType.FIXED, false, stack, buf,
                        press.getWorld(), combinedLight, combinedOverlay);
                stack.pop();
            }
        }
    }


    //Arm Stuff
    @Deprecated
    private void renderArm(PressTileEntity press, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight, int combinedOverlay) {
        BlockRendererDispatcher blockRender = Minecraft.getInstance().getBlockRendererDispatcher();
        stack.push();
        stack.translate(0, press.getHeightChange(), 0);
        stack.scale(1, 1, 1);
        blockRender.renderBlock(PRESS_ARM.getBlock().getDefaultState(), stack, buf, combinedLight, combinedOverlay);
        stack.pop();
    }

    public void renderFluidBlock(PressTileEntity tile, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight) {
        FluidStack liquid = tile.getOutputFluid().getFluid();
        IVertexBuilder builder = buf.getBuffer(FluidRenderer.getBlockRenderType());
        float minY = 1.1F;
        float maxY = 15F;
        if(!liquid.isEmpty()){
            float sections = (maxY - minY)/tile.getOutputFluid().getCapacity();
            float height = (float)liquid.getAmount()*sections;
            FluidRenderer.renderScaledFluidCuboid(liquid,stack,builder,combinedLight,2.2F,minY,2.1F,13.9F,minY + height,13.9F);
        }


    }

}
