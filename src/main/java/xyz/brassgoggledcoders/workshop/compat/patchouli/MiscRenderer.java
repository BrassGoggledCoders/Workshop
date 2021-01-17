package xyz.brassgoggledcoders.workshop.compat.patchouli;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

//Adapted from JEI
@Deprecated
public class MiscRenderer {
    public void render(MatrixStack matrixStack, final int xPosition, final int yPosition, @Nullable FluidStack fluidStack) {
        RenderSystem.enableBlend();
        RenderSystem.enableAlphaTest();
        drawFluid(matrixStack, xPosition, yPosition, fluidStack);
        RenderSystem.color4f(1, 1, 1, 1);
        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();
    }

    private void drawFluid(MatrixStack matrixStack, final int xPosition, final int yPosition, @Nullable FluidStack fluidStack) {
        if (fluidStack == null) {
            return;
        }
        Fluid fluid = fluidStack.getFluid();
        if (fluid == null) {
            return;
        }

        TextureAtlasSprite fluidStillSprite = getStillFluidSprite(fluidStack);

        FluidAttributes attributes = fluid.getAttributes();
        int fluidColor = attributes.getColor(fluidStack);
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        Matrix4f matrix = matrixStack.getLast().getMatrix();
        setGLColorFromInt(fluidColor);
        drawTextureWithMasking(matrix, xPosition, yPosition, fluidStillSprite, 0, 0, 100);
    }

    private static TextureAtlasSprite getStillFluidSprite(FluidStack fluidStack) {
        Minecraft minecraft = Minecraft.getInstance();
        Fluid fluid = fluidStack.getFluid();
        FluidAttributes attributes = fluid.getAttributes();
        ResourceLocation fluidStill = attributes.getStillTexture(fluidStack);
        return minecraft.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluidStill);
    }

    @SuppressWarnings("deprecation")
    private static void setGLColorFromInt(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = ((color >> 24) & 0xFF) / 255F;

        RenderSystem.color4f(red, green, blue, alpha);
    }

    public static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, int maskTop, int maskRight, float zLevel) {
        float uMin = textureSprite.getMinU();
        float uMax = textureSprite.getMaxU();
        float vMin = textureSprite.getMinV();
        float vMax = textureSprite.getMaxV();
        uMax = uMax - (maskRight / 16F * (uMax - uMin));
        vMax = vMax - (maskTop / 16F * (vMax - vMin));

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(matrix, xCoord, yCoord + 16, zLevel).tex(uMin, vMax).endVertex();
        bufferBuilder.pos(matrix, xCoord + 16 - maskRight, yCoord + 16, zLevel).tex(uMax, vMax).endVertex();
        bufferBuilder.pos(matrix, xCoord + 16 - maskRight, yCoord + maskTop, zLevel).tex(uMax, vMin).endVertex();
        bufferBuilder.pos(matrix, xCoord, yCoord + maskTop, zLevel).tex(uMin, vMin).endVertex();
        tessellator.draw();
    }

    //TODO
    public List<ITextComponent> getTooltip(FluidStack fluidStack, ITooltipFlag tooltipFlag) {
        List<ITextComponent> tooltip = new ArrayList<>();
        Fluid fluidType = fluidStack.getFluid();
        if (fluidType == null) {
            return tooltip;
        }
        ITextComponent displayName = fluidStack.getDisplayName();
        tooltip.add(displayName);
        return tooltip;
    }
}
