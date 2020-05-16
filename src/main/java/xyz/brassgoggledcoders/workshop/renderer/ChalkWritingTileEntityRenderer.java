package xyz.brassgoggledcoders.workshop.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.RenderComponentsUtil;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.brassgoggledcoders.workshop.tileentity.ChalkWritingTileEntity;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ChalkWritingTileEntityRenderer extends TileEntityRenderer<ChalkWritingTileEntity> {

    public ChalkWritingTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    public void render(ChalkWritingTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockState blockstate = tileEntityIn.getBlockState();
        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 0.5D, 0.5D);
        float f4 = -blockstate.get(WallSignBlock.FACING).getHorizontalAngle();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f4));
        matrixStackIn.translate(0.0D, -0.3125D, -0.4375D);

        FontRenderer fontrenderer = this.renderDispatcher.getFontRenderer();
        matrixStackIn.translate(0.0D, (double)0.33333334F, (double)0.046666667F);
        matrixStackIn.scale(0.010416667F, -0.010416667F, 0.010416667F);
        int i = DyeColor.WHITE.getTextColor();
        int j = (int)((double) NativeImage.getRed(i) * 0.4D);
        int k = (int)((double)NativeImage.getGreen(i) * 0.4D);
        int l = (int)((double)NativeImage.getBlue(i) * 0.4D);
        int i1 = NativeImage.getCombined(0, l, k, j);

        for(int j1 = 0; j1 < 4; ++j1) {
            String s = tileEntityIn.getRenderText(j1, (p_212491_1_) -> {
                List<ITextComponent> list = RenderComponentsUtil.splitText(p_212491_1_, 90, fontrenderer, false, true);
                return list.isEmpty() ? "" : list.get(0).getFormattedText();
            });
            if (s != null) {
                float f3 = (float)(-fontrenderer.getStringWidth(s) / 2);
                fontrenderer.renderString(s, f3, (float)(j1 * 10 - tileEntityIn.signText.length * 5), i1, false, matrixStackIn.getLast().getMatrix(), bufferIn, false, 0, combinedLightIn);
            }
        }

        matrixStackIn.pop();
    }
}
