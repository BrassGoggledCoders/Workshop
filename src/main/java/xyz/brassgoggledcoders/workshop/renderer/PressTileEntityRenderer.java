package xyz.brassgoggledcoders.workshop.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
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
import net.minecraft.world.ILightReader;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import xyz.brassgoggledcoders.workshop.block.PressBlock;
import xyz.brassgoggledcoders.workshop.tileentity.PressTileEntity;

import static xyz.brassgoggledcoders.workshop.content.WorkshopBlocks.PRESS_ARM;

public class PressTileEntityRenderer extends TileEntityRenderer<PressTileEntity> {

    public PressTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Deprecated
    @Override
    public void render(PressTileEntity press, float v, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight, int combinedOverlay) {
        if (!press.hasWorld()) {
            return;
        }

        //ArmRender
        renderArm(press, stack, buf, combinedLight, combinedOverlay);

        //Fluid Visuals
        if (!press.getOutputFluid().isEmpty()) {
            renderFluidBlock(press, stack, buf, combinedLight);
        }

        //Render Item
        renderInventory(press, stack, buf, combinedLight, combinedOverlay);

    }


    //Inventory Item Renderer
    private void renderInventory(PressTileEntity press, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack item = press.getInputInventory().getStackInSlot(0);
        float f = press.getWorld().getBlockState(press.getPos()).get(PressBlock.FACING).getHorizontalAngle();
        if (!item.isEmpty() && press.getHeight() > 0.5) {
            stack.push();
            stack.translate(0.5, 0.5, 0.5);
            stack.scale(0.4f, 0.4f, 0.4f);
            itemRenderer.renderItem(null, item, ItemCameraTransforms.TransformType.FIXED, false, stack, buf,
                    press.getWorld(), combinedLight, combinedOverlay);
            stack.pop();
        }
    }


    //Arm Stuff
    private void renderArm(PressTileEntity press, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight, int combinedOverlay) {
        BlockRendererDispatcher blockRender = Minecraft.getInstance().getBlockRendererDispatcher();
        stack.push();
        stack.translate(0, press.getHeight(), 0);
        stack.scale(1, 1, 1);
        blockRender.renderBlock(PRESS_ARM.getBlock().getDefaultState(), stack, buf, combinedLight, combinedOverlay);
        stack.pop();
    }

    public void renderFluidBlock(PressTileEntity tile, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight) {
        BlockPos pos = tile.getPos();
        FluidStack fluid = tile.getOutputFluid().getFluid();
        int amount = fluid.getAmount();

        int color = fluid.getFluid().getAttributes().getColor();
        Texture texture = Minecraft.getInstance().getTextureManager().getTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        if (texture instanceof AtlasTexture) {
            TextureAtlasSprite still = ((AtlasTexture) texture).getSprite(fluid.getFluid().getAttributes().getStillTexture(fluid));
            float posY = 2 / 16f - 1 / 32f;

            IVertexBuilder renderer = buf.getBuffer(RenderType.getSolid());

            stack.push();
            renderSide(renderer, stack, still, pos.getX(), pos.getY(), pos.getZ(), 10, 10, 10, Direction.NORTH, color, combinedLight, false);
            renderSide(renderer, stack, still, pos.getX(), pos.getY(), pos.getZ(), 10, 10, 10, Direction.SOUTH, color, combinedLight, false);
            renderSide(renderer, stack, still, pos.getX(), pos.getY(), pos.getZ(), 10, 10, 10, Direction.EAST, color, combinedLight, false);
            renderSide(renderer, stack, still, pos.getX(), pos.getY(), pos.getZ(), 10, 10, 10, Direction.WEST, color, combinedLight, false);
            renderSide(renderer, stack, still, pos.getX(), pos.getY(), pos.getZ(), 10, 10, 10, Direction.UP, color, combinedLight, false);
            renderSide(renderer, stack, still, pos.getX(), pos.getY(), pos.getZ(), 10, 10, 10, Direction.DOWN, color, combinedLight, false);
            stack.pop();
        }

    }

    private void renderSide(IVertexBuilder renderer, MatrixStack stack, TextureAtlasSprite sprite, float x, float y, float z, double w,
                            double h, double d, Direction face, int color, int combinedLight, boolean flowing) {
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

        float size = 16f;
        if (flowing) {
            size = 8f;
        }

        float x1 = x;
        float x2 = (float) (x + w);
        float y1 = y;
        float y2 = (float) (y + h);
        float z1 = z;
        float z2 = (float) (z + d);

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
                renderer.pos(stack.getLast().getMatrix(),x1, y1, z1).color(r, g, b, a).tex(minU, minV).lightmap(0, 240).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x2, y1, z1).color(r, g, b, a).tex(maxU, minV).lightmap(0, 240).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x2, y1, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(0, 240).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x1, y1, z2).color(r, g, b, a).tex(minU, maxV).lightmap(0, 240).normal(1, 0, 0).endVertex();
                break;
            case UP:
                renderer.pos(stack.getLast().getMatrix(), x1, y2, z1).color(r, g, b, a).tex(minU, minV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x1, y2, z2).color(r, g, b, a).tex(minU, maxV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x2, y2, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x2, y2, z1).color(r, g, b, a).tex(maxU, minV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                break;
            case NORTH:
                renderer.pos(stack.getLast().getMatrix(),x1, y1, z1).color(r, g, b, a).tex(minU, maxV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x1, y2, z1).color(r, g, b, a).tex(minU, minV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x2, y2, z1).color(r, g, b, a).tex(maxU, minV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x2, y1, z1).color(r, g, b, a).tex(maxU, maxV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                break;
            case SOUTH:
                renderer.pos(stack.getLast().getMatrix(),x1, y1, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x2, y1, z2).color(r, g, b, a).tex(minU, maxV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x2, y2, z2).color(r, g, b, a).tex(minU, minV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x1, y2, z2).color(r, g, b, a).tex(maxU, minV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                break;
            case WEST:
                renderer.pos(stack.getLast().getMatrix(),x1, y1, z1).color(r, g, b, a).tex(maxU, maxV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x1, y1, z2).color(r, g, b, a).tex(minU, maxV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x1, y2, z2).color(r, g, b, a).tex(minU, minV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x1, y2, z1).color(r, g, b, a).tex(maxU, minV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                break;
            case EAST:
                renderer.pos(stack.getLast().getMatrix(),x2, y1, z1).color(r, g, b, a).tex(minU, maxV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x2, y2, z1).color(r, g, b, a).tex(minU, minV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x2, y2, z2).color(r, g, b, a).tex(maxU, minV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                renderer.pos(stack.getLast().getMatrix(),x2, y1, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(combinedLight).normal(1, 0, 0).endVertex();
                break;
        }
    }

}
