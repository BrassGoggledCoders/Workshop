package xyz.brassgoggledcoders.workshop.compat.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagRegistryManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import vazkii.patchouli.client.RenderHelper;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;

import java.util.ArrayList;
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
                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                if(!bs.getFluidState().isEmpty()) {
                    ItemStack stack = new ItemStack(bs.getFluidState().getFluid().getFilledBucket());
                    if (!stack.isEmpty()) {
                        RenderHelper.transferMsToGl(ms, () -> {
                            itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
                            //itemRenderer.renderItemOverlays(this.parent.getFont(), stack, x, y);
                        });
                    }
                }
                else {
                    RenderHelper.transferMsToGl(ms, () -> {
                        itemRenderer.zLevel += 50f;
                        itemRenderer.renderItemModelIntoGUI(new ItemStack(Items.BEDROCK), x, y, Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModel(bs));
                        itemRenderer.zLevel -= 50f;
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
