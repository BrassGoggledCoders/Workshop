/*
Original Code Copyright (C) 2017 Jason Taylor

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package xyz.brassgoggledcoders.workshop.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.opengl.GL11;
import xyz.brassgoggledcoders.workshop.tileentity.MortarTileEntity;

import java.util.List;
import java.util.stream.Collectors;

public class HUDRender {

    public static void render() {
        Minecraft minecraft = Minecraft.getInstance();
        RayTraceResult rayTraceResult = minecraft.objectMouseOver;

        if (!(minecraft.currentScreen instanceof ChatScreen) && rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {

        BlockPos blockPos =  ((BlockRayTraceResult)rayTraceResult).getPos();

        if (blockPos.getY() < 0 || blockPos.getY() >= 256) {
            // Sanity check.
            return;
        }

        if (minecraft.world.getTileEntity(blockPos) instanceof MortarTileEntity) {
            MortarTileEntity tileEntity = (MortarTileEntity) minecraft.world.getTileEntity(blockPos);
            if (tileEntity.hasWorld()) {
                ItemStackHandler itemStackHandler = tileEntity.getInputInventory();

                List<ItemStack> itemStackList = InventoryUtil.getItemStackStream(itemStackHandler).collect(Collectors.toList());

                float angle = (float) (Math.PI * 2 / itemStackList.size());
                double radius = 32;

                //GlStateManager.enableBlend();
                //GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                //GlStateManager.color(1, 1, 1, 1);
                RenderHelper.enableStandardItemLighting();
                int scaledWidth = minecraft.getMainWindow().getScaledWidth();
                for (int ii = 0; ii < itemStackList.size(); ii++) {
                    ItemStack itemStack = itemStackList.get(ii);
                    int x = (int) (MathHelper.cos(angle * ii) * radius + scaledWidth / 2) - 8;
                    int y = (int) (MathHelper.sin(angle * ii) * radius + minecraft.getMainWindow().getScaledHeight() / 2) - 8;
                    minecraft.getItemRenderer().renderItemAndEffectIntoGUI(itemStack, x, y);
                    minecraft.getItemRenderer().renderItemOverlays(minecraft.fontRenderer, itemStack, x, y);
                }
                //TODO add faded display of what the recipe *will* make
                if (!tileEntity.getOutputInventory().getStackInSlot(0).isEmpty()) {
                    ItemStack output = tileEntity.getOutputInventory().getStackInSlot(0);
                    int x = (int) (radius + scaledWidth / 2) - 8 + 64;
                    int y = minecraft.getMainWindow().getScaledHeight() / 2 - 8;
                    minecraft.getItemRenderer().renderItemAndEffectIntoGUI(output, x, y);
                    minecraft.getItemRenderer().renderItemOverlays(minecraft.fontRenderer, output, x, y);

                    x = (int) (radius + minecraft.getMainWindow().getScaledWidth() / 2) - 8 + 30;
                    y = minecraft.getMainWindow().getScaledHeight() / 2 - 4;

                    //TODO render progress
                    // Arrow
                    //drawTexturedRect(minecraft, IAssetProvider.DEFAULT_LOCATION, x + 5, y, 12, 11, 100, 177, 61, 256, 256);
                }
                RenderHelper.disableStandardItemLighting();
                //GlStateManager.disableLighting();
            }
        }
        }
    }

    public static void drawTexturedRect(Minecraft minecraft, ResourceLocation texture, int x, int y, int width, int height, int zLevel, int texPosX, int texPosY, int texWidth, int texHeight) {
        float u0 = texPosX / (float) texWidth;
        float v0 = texPosY / (float) texHeight;

        float u1 = u0 + width / (float) texWidth;
        float v1 = v0 + height / (float) texHeight;

        TextureManager renderEngine = minecraft.getRenderManager().textureManager;
        renderEngine.bindTexture(texture);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder
                .pos(x, (y + height), zLevel)
                .tex(u0, v1)
                .endVertex();
        bufferbuilder
                .pos((x + width), (y + height), zLevel)
                .tex(u1, v1)
                .endVertex();
        bufferbuilder
                .pos((x + width), y, zLevel)
                .tex(u1, v0)
                .endVertex();
        bufferbuilder
                .pos(x, y, zLevel)
                .tex(u0, v0)
                .endVertex();
        tessellator.draw();
    }
}