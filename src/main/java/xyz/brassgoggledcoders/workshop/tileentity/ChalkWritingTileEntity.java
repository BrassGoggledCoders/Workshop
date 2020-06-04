package xyz.brassgoggledcoders.workshop.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.gui.ChalkWritingScreen;

import javax.annotation.Nullable;
import java.util.function.Function;

public class ChalkWritingTileEntity extends TileEntity implements GUITile {

    public final ITextComponent[] signText = new ITextComponent[]{new StringTextComponent(""), new StringTextComponent(""), new StringTextComponent(""), new StringTextComponent("")};
    private final String[] renderText = new String[4];

    public ChalkWritingTileEntity() {
        super(WorkshopBlocks.CHALK_WRITING.getTileEntityType());
    }

    @Override
    public ActionResultType onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        Minecraft.getInstance().displayGuiScreen(new ChalkWritingScreen(this));
        return ActionResultType.PASS;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        for (int i = 0; i < 4; ++i) {
            String s = ITextComponent.Serializer.toJson(this.signText[i]);
            compound.putString("Text" + (i + 1), s);
        }
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        for (int i = 0; i < 4; ++i) {
            String s = compound.getString("Text" + (i + 1));
            ITextComponent itextcomponent = ITextComponent.Serializer.fromJson(s.isEmpty() ? "\"\"" : s);
            this.signText[i] = itextcomponent;
            this.renderText[i] = null;
        }

    }

    @OnlyIn(Dist.CLIENT)
    public ITextComponent getText(int line) {
        return this.signText[line];
    }

    public void setText(int line, ITextComponent p_212365_2_) {
        this.signText[line] = p_212365_2_;
        this.renderText[line] = null;
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public String getRenderText(int line, Function<ITextComponent, String> p_212364_2_) {
        if (this.renderText[line] == null && this.signText[line] != null) {
            this.renderText[line] = p_212364_2_.apply(this.signText[line]);
        }
        return this.renderText[line];
    }


    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 9, this.getUpdateTag());
    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }
}
