package xyz.brassgoggledcoders.workshop.compat.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagRegistryManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
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

    //TODO Tooltips
    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
        this.parent.drawCenteredStringNoShadow(ms, title, 58, 0, this.book.headerColor);
        for(int i = 0; i < hotBlocks.size(); i++) {
            IItemProvider provider = hotBlocks.get(i);
            int xPosition = i * 18;
            int yPosition = 10;
            if(provider instanceof Block && Items.AIR.equals(provider.asItem())) {
                BlockState bs = ((Block) provider).getDefaultState();
                MiscRenderer r = new MiscRenderer();
                if(!bs.getFluidState().isEmpty()) {
                    r.render(ms, xPosition, yPosition, new FluidStack(bs.getFluidState().getFluid(), FluidAttributes.BUCKET_VOLUME));
                }
                else {
                    //TODO don't render texture directly, ew
                    //IRenderTypeBuffer.Impl bf = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
                    //Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(bs, ms, bf, 15728880, OverlayTexture.NO_OVERLAY);
                    Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
                    Matrix4f matrix = ms.getLast().getMatrix();
                    MiscRenderer.drawTextureWithMasking(matrix, xPosition, yPosition, Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getTexture(bs),0, 0, 100);
                }
            }
            else {
                ItemStack stack = new ItemStack(provider);
                if (!stack.isEmpty()) {
                    this.parent.renderItemStack(ms, xPosition, yPosition, mouseX, mouseY, stack);
                }
            }
        }
        super.render(ms, mouseX, mouseY, pticks);
    }
}
