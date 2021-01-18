package xyz.brassgoggledcoders.workshop.compat.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagRegistryManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import vazkii.patchouli.client.RenderHelper;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import xyz.brassgoggledcoders.workshop.util.FluidRenderer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TagPage extends BookPage {
    @SerializedName("tag")
    ResourceLocation tagID;
    @SerializedName("registry")
    ResourceLocation typeID;
    String title;

    private final transient List<IItemProvider> hotBlocks = new ArrayList<>();

    public TagPage() {
    }

    @Override
    public void build(BookEntry entry, int pageNum) {
        if (this.tagID != null && typeID != null && TagRegistryManager.get(typeID) != null) {
            ITag<?> tag = TagRegistryManager.get(typeID).getCollection().getTagByID(tagID);
            for(Object obj : tag.getAllElements()) {
                if(obj instanceof IItemProvider) {
                    hotBlocks.add((IItemProvider) obj);
                }
            }
        }
        super.build(entry, pageNum);
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
        this.parent.drawCenteredStringNoShadow(ms, title, 58, 0, this.book.headerColor);
        for(int i = 0; i < hotBlocks.size(); i++) {
            IItemProvider provider = hotBlocks.get(i);
            int x = i * 18;
            int y = 10;
            if(provider instanceof Block && Items.AIR.equals(provider.asItem())) {
                Block block = (Block) provider;
                BlockState bs = block.getDefaultState();
                //TODO Render fluids in the same manner as JEI instead
                if(!bs.getFluidState().isEmpty()) {
                    ItemStack stack = new ItemStack(bs.getFluidState().getFluid().getFilledBucket());
                    if (!stack.isEmpty()) {
                        this.parent.renderItemStack(ms, x, y, mouseX, mouseY, stack);
                    }
                }
                else {
                    RenderHelper.transferMsToGl(ms, () -> {
                        Minecraft.getInstance().getItemRenderer().zLevel += 50f;
                        Minecraft.getInstance().getItemRenderer().renderItemModelIntoGUI(new ItemStack(Items.BEDROCK), x, y, Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModel(bs));
                        Minecraft.getInstance().getItemRenderer().zLevel -= 50f;
                    });
                }
                if (this.parent.isMouseInRelativeRange(mouseX, mouseY, x, y, 16, 16)) {
                    super.parent.setTooltip(new TranslationTextComponent(block.getTranslationKey()));
                }
            }
            else {
                ItemStack stack = new ItemStack(provider);
                if (!stack.isEmpty()) {
                    this.parent.renderItemStack(ms, x, y, mouseX, mouseY, stack);
                }
            }
        }
        super.render(ms, mouseX, mouseY, pticks);
    }
}
