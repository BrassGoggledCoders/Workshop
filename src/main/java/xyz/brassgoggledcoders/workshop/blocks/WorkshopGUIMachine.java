package xyz.brassgoggledcoders.workshop.blocks;

import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.components.MachineComponent;

import javax.annotation.Nonnull;

public abstract class WorkshopGUIMachine<T extends WorkshopGUIMachine<T>> extends TileEntity implements IComponentHarness, ITickableTileEntity, INamedContainerProvider {
    private final MachineComponent<T> machineComponent;
    private ProgressBarComponent<T> progressBar;

    @SuppressWarnings("unchecked")
    public WorkshopGUIMachine(TileEntityType<T> tTileEntityType, ProgressBarComponent<T> progressBar) {
        super(tTileEntityType);
        this.machineComponent = new MachineComponent<>((T) this, this::getPos);
        this.machineComponent.addProgressBar(this.progressBar = progressBar
                .setOnStart(() -> progressBar.setMaxProgress(getMaxProgress()))
                .setCanIncrease(tileEntity -> canIncrease())
                .setOnFinishWork(this::onFinish));
    }

    public MachineComponent<T> getMachineComponent() {
        return this.machineComponent;
    }

    public ProgressBarComponent<T> getProgressBar() {
        return progressBar;
    }

    @Override
    public void read(CompoundNBT compound) {
        progressBar.deserializeNBT(compound.getCompound("progress"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("progress", progressBar.serializeNBT());
        return super.write(compound);
    }

    public ActionResultType onActivated(PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
        this.getMachineComponent().onActivated(playerIn, hand, hit);
        if (playerIn instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, this, this.getPos());
        }
        return ActionResultType.SUCCESS;
    }

    public abstract boolean canIncrease();

    public int getMaxProgress() {
        return 100;
    }

    public abstract Runnable onFinish();

    public World getComponentWorld() {
        return this.world;
    }

    public void markComponentForUpdate() {

    }

    public void markComponentDirty() {
        this.markDirty();
    }

    @Override
    public void tick() {
        this.getMachineComponent().tick();
    }

    //TODO
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("");
    }
}
