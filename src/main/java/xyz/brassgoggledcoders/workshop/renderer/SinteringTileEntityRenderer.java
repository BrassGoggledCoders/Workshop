package xyz.brassgoggledcoders.workshop.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import xyz.brassgoggledcoders.workshop.tileentity.SinteringFurnaceTileEntity;

public class SinteringTileEntityRenderer extends TileEntityRenderer<SinteringFurnaceTileEntity> {

    public SinteringTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Deprecated
    @Override
    public void render(SinteringFurnaceTileEntity furnace, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int i, int i1) {
        if (!furnace.hasWorld()) {
            return;
        }
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack input = furnace.getInputInventory().getStackInSlot(0);
        ItemStack output = furnace.getOutputInventory().getStackInSlot(0);
        ItemStack powder1 = furnace.getPowderInventory().getStackInSlot(0);
        ItemStack powder2 = furnace.getPowderInventory().getStackInSlot(1);
        Direction direction = furnace.getMachineComponent().getFacingDirection();
        float f = direction.getHorizontalAngle();
        //if (furnace.isInactive()) {
            if (!output.isEmpty()) {
                if (input.isEmpty()) {
                    //renderItem(itemRenderer, f, x + 0.5, y + 0.6, z + 0.5, output);
                } else {
                    if (direction == Direction.NORTH) {
                        //renderItem(itemRenderer, f, x + 0.7, y + 0.6, z + 0.5, input);
                        //renderItem(itemRenderer, f, x + 0.3, y + 0.6, z + 0.5, output);
                    } else if (direction == Direction.SOUTH) {
                        //renderItem(itemRenderer, f, x + 0.7, y + 0.6, z + 0.5, output);
                        //renderItem(itemRenderer, f, x + 0.3, y + 0.6, z + 0.5, input);
                    } else if (direction == Direction.EAST) {
                        //renderItem(itemRenderer, f, x + 0.5, y + 0.6, z + 0.3, output);
                        //renderItem(itemRenderer, f, x + 0.5, y + 0.6, z + 0.7, input);
                    } else if (direction == Direction.WEST) {
                        //renderItem(itemRenderer, f, x + 0.5, y + 0.6, z + 0.3, input);
                        //renderItem(itemRenderer, f, x + 0.5, y + 0.6, z + 0.7, output);
                    }
                }
            } else if (!input.isEmpty()) {
                //renderItem(itemRenderer, f, x + 0.5, y + 0.6, z + 0.5, input);
            }
        //} else {
            //SinteringFurnaceRecipe recipe = furnace.getCurrentRecipe();
            //if(recipe != null) {
            if (!powder1.isEmpty()) {
                GlStateManager.pushMatrix();
                //GlStateManager.translated(x + 0.5, y + 1.1, z + 0.5);
                GlStateManager.rotatef(f, 0, 1, 0.0F);
                GlStateManager.scalef(0.3f, 0.3f, 0.3f);
                //itemRenderer.renderItem(powder1, ItemCameraTransforms.TransformType.FIXED);
                GlStateManager.popMatrix();
            } else if (!powder2.isEmpty()) {
                GlStateManager.pushMatrix();
                //GlStateManager.translated(x + 0.5, y + 1.1, z + 0.5);
                GlStateManager.rotatef(f, 0, 1, 0.0F);
                GlStateManager.scalef(0.3f, 0.3f, 0.3f);
                //itemRenderer.renderItem(powder2, ItemCameraTransforms.TransformType.FIXED);
                GlStateManager.popMatrix();
            }

            // }

        //}
    }

    @Deprecated
    private void renderItem(ItemRenderer itemRenderer, float horizontalAxis, double x, double y, double z, ItemStack stack) {
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);
        GlStateManager.rotatef(horizontalAxis, 0.0F, 1.0F, 0.0F);
        GlStateManager.scalef(0.3f, 0.3f, 0.3f);
        //itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
        GlStateManager.popMatrix();
    }


}
