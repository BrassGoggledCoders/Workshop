package xyz.brassgoggledcoders.workshop.blocks.press;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;

public class PressTileEntityRenderer extends TileEntityRenderer<PressTile> {

    @Override
    public void render(PressTile press, double x, double y, double z, float partialTicks, int destroyStage) {
        if (!press.hasWorld()) {
            return;
        }
        //Item Visuals
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack item = press.getInputInventory().getStackInSlot(0);
        if(!item.isEmpty()){
            GlStateManager.pushMatrix();
            GlStateManager.translated(x + 0.5, y + 0.5, z + 0.5);
            GlStateManager.rotatef(45.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.scalef(0.4f, 0.4f, 0.4f);
            itemRenderer.renderItem(item, ItemCameraTransforms.TransformType.FIXED);
            GlStateManager.popMatrix();
        }
        // Piston head

        super.render(press, x, y, z, partialTicks, destroyStage);
    }
}
