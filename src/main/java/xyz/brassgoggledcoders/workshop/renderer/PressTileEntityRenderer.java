package xyz.brassgoggledcoders.workshop.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import xyz.brassgoggledcoders.workshop.blocks.press.PressBlock;
import xyz.brassgoggledcoders.workshop.tileentity.PressTileEntity;

public class PressTileEntityRenderer extends TileEntityRenderer<PressTileEntity> {

    public PressTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Deprecated
    @Override
    public void render(PressTileEntity pressTileEntity, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int i, int i1) {
        if (!pressTileEntity.hasWorld()) {
            return;
        }
        //Fluid Visuals
        if(!pressTileEntity.getOutputFluid().isEmpty()) {
            renderFluidBlock(pressTileEntity);
        }

        //Item Visuals
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack item = pressTileEntity.getInputInventory().getStackInSlot(0);
        float f = pressTileEntity.getWorld().getBlockState(pressTileEntity.getPos()).get(PressBlock.FACING).getHorizontalAngle();
        if(!item.isEmpty() && !pressTileEntity.isActive()){
            GlStateManager.pushMatrix();
            //GlStateManager.translated(x + 0.5, y + 0.5, z + 0.5);
            GlStateManager.rotatef(f, 0, 1, 0.0F);
            GlStateManager.scalef(0.4f, 0.4f, 0.4f);
            itemRenderer.renderItem(null, item, ItemCameraTransforms.TransformType.FIXED, false, matrixStack, iRenderTypeBuffer,
                    pressTileEntity.getWorld(), i, i1);
            GlStateManager.popMatrix();
        }
    }

    public void renderFluidBlock(PressTileEntity tile){
        BlockPos pos = tile.getPos();
        FluidStack fluid = tile.getOutputFluid().getFluid();
        int amount = fluid.getAmount();

        int color = fluid.getFluid().getAttributes().getColor();
        Texture texture = Minecraft.getInstance().getTextureManager().getTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        if (texture instanceof AtlasTexture) {
            TextureAtlasSprite still = ((AtlasTexture) texture).getSprite(fluid.getFluid().getAttributes().getStillTexture(fluid));
            float posY = 2 / 16f - 1 / 32f;

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder renderer = tessellator.getBuffer();
            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

            GlStateManager.pushMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableBlend();

            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.translated(pos.getX(), pos.getY(), pos.getZ());
            renderSide(renderer, still, 0, 0, 0, 0, 0, 0, Direction.NORTH, color, false);
            renderSide(renderer, still, 0, 0, 0, 0, 0, 0, Direction.SOUTH, color, false);
            renderSide(renderer, still, 0, 0, 0, 0, 0, 0, Direction.EAST, color, false);
            renderSide(renderer, still, 0, 0, 0, 0, 0, 0, Direction.WEST, color, false);
            renderSide(renderer, still, 0, 0, 0, 0, 0, 0, Direction.UP, color, false);
            renderSide(renderer, still, 0, 0, 0, 0, 0, 0, Direction.DOWN, color, false);
            GlStateManager.disableBlend();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();
        }

    }

    private void renderSide(BufferBuilder renderer, TextureAtlasSprite sprite, double x, double y, double z, double w,
                            double h, double d, Direction face, int color, boolean flowing) {
        // safety
        if (sprite == null) {
            return;
        }

        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;

        float minU;
        float maxU;
        float minV;
        float maxV;

        double size = 16f;
        if (flowing) {
            size = 8f;
        }

        double x1 = x;
        double x2 = x + w;
        double y1 = y;
        double y2 = y + h;
        double z1 = z;
        double z2 = z + d;

        double xt1 = x1 % 1d;
        double xt2 = xt1 + w;
        while (xt2 > 1f)
            xt2 -= 1f;
        double yt1 = y1 % 1d;
        double yt2 = yt1 + h;
        while (yt2 > 1f)
            yt2 -= 1f;
        double zt1 = z1 % 1d;
        double zt2 = zt1 + d;
        while (zt2 > 1f)
            zt2 -= 1f;

        // flowing stuff should start from the bottom, not from the start
        if (flowing) {
            double tmp = 1d - yt1;
            yt1 = 1d - yt2;
            yt2 = tmp;
        }

        switch (face) {
            case DOWN:
            case UP:
                minU = sprite.getInterpolatedU(xt1 * size);
                maxU = sprite.getInterpolatedU(xt2 * size);
                minV = sprite.getInterpolatedV(zt1 * size);
                maxV = sprite.getInterpolatedV(zt2 * size);
                break;
            case NORTH:
            case SOUTH:
                minU = sprite.getInterpolatedU(xt2 * size);
                maxU = sprite.getInterpolatedU(xt1 * size);
                minV = sprite.getInterpolatedV(yt1 * size);
                maxV = sprite.getInterpolatedV(yt2 * size);
                break;
            case WEST:
            case EAST:
                minU = sprite.getInterpolatedU(zt2 * size);
                maxU = sprite.getInterpolatedU(zt1 * size);
                minV = sprite.getInterpolatedV(yt1 * size);
                maxV = sprite.getInterpolatedV(yt2 * size);
                break;
            default:
                minU = sprite.getMinU();
                maxU = sprite.getMaxU();
                minV = sprite.getMinV();
                maxV = sprite.getMaxV();
        }

        switch (face) {
            case DOWN:
                renderer.pos(x1, y1, z1).color(r, g, b, a).tex(minU, minV).endVertex();
                renderer.pos(x2, y1, z1).color(r, g, b, a).tex(maxU, minV).endVertex();
                renderer.pos(x2, y1, z2).color(r, g, b, a).tex(maxU, maxV).endVertex();
                renderer.pos(x1, y1, z2).color(r, g, b, a).tex(minU, maxV).endVertex();
                break;
            case UP:
                renderer.pos(x1, y2, z1).color(r, g, b, a).tex(minU, minV).endVertex();
                renderer.pos(x1, y2, z2).color(r, g, b, a).tex(minU, maxV).endVertex();
                renderer.pos(x2, y2, z2).color(r, g, b, a).tex(maxU, maxV).endVertex();
                renderer.pos(x2, y2, z1).color(r, g, b, a).tex(maxU, minV).endVertex();
                break;
            case NORTH:
                renderer.pos(x1, y1, z1).color(r, g, b, a).tex(minU, maxV).endVertex();
                renderer.pos(x1, y2, z1).color(r, g, b, a).tex(minU, minV).endVertex();
                renderer.pos(x2, y2, z1).color(r, g, b, a).tex(maxU, minV).endVertex();
                renderer.pos(x2, y1, z1).color(r, g, b, a).tex(maxU, maxV).endVertex();
                break;
            case SOUTH:
                renderer.pos(x1, y1, z2).color(r, g, b, a).tex(maxU, maxV).endVertex();
                renderer.pos(x2, y1, z2).color(r, g, b, a).tex(minU, maxV).endVertex();
                renderer.pos(x2, y2, z2).color(r, g, b, a).tex(minU, minV).endVertex();
                renderer.pos(x1, y2, z2).color(r, g, b, a).tex(maxU, minV).endVertex();
                break;
            case WEST:
                renderer.pos(x1, y1, z1).color(r, g, b, a).tex(maxU, maxV).endVertex();
                renderer.pos(x1, y1, z2).color(r, g, b, a).tex(minU, maxV).endVertex();
                renderer.pos(x1, y2, z2).color(r, g, b, a).tex(minU, minV).endVertex();
                renderer.pos(x1, y2, z1).color(r, g, b, a).tex(maxU, minV).endVertex();
                break;
            case EAST:
                renderer.pos(x2, y1, z1).color(r, g, b, a).tex(minU, maxV).endVertex();
                renderer.pos(x2, y2, z1).color(r, g, b, a).tex(minU, minV).endVertex();
                renderer.pos(x2, y2, z2).color(r, g, b, a).tex(maxU, minV).endVertex();
                renderer.pos(x2, y1, z2).color(r, g, b, a).tex(maxU, maxV).endVertex();
                break;
        }
    }
}
