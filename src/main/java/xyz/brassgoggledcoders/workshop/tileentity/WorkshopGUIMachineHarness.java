package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.workshop.component.machine.IMachineHarness;
import xyz.brassgoggledcoders.workshop.component.machine.MachineComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

public abstract class WorkshopGUIMachineHarness<T extends WorkshopGUIMachineHarness<T>> extends TileEntity implements IMachineHarness<T>, ITickableTileEntity, INamedContainerProvider {
    private final MachineComponent<T> machineComponent;
    private ProgressBarComponent<T> progressBar;

    public WorkshopGUIMachineHarness(TileEntityType<T> tTileEntityType, ProgressBarComponent<T> progressBar) {
        super(tTileEntityType);
        this.machineComponent = new MachineComponent<>(this.getSelf(), this::getPos);
        this.machineComponent.addProgressBar(this.progressBar = progressBar
                .setOnStart(() -> progressBar.setMaxProgress(getMaxProgress()))
                .setCanIncrease(tileEntity -> canIncrease())
                .setMaxProgress(1000)
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

    public ActionResultType onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ActionResultType result = this.getMachineComponent().onActivated(player, hand, hit);
        if (result == ActionResultType.PASS && player instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) player, this, packetBuffer ->
                    LocatorFactory.writePacketBuffer(packetBuffer, new TileEntityLocatorInstance(this.pos)));
            result = ActionResultType.SUCCESS;
        }
        return result;
    }

    public abstract boolean canIncrease();

    public int getMaxProgress() {
        return 100;
    }

    public abstract void onFinish();

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

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey())
                .applyTextStyle(TextFormatting.BLACK);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int menu, PlayerInventory inventoryPlayer, PlayerEntity entityPlayer) {
        return new BasicAddonContainer(this, IWorldPosCallable.of(Objects.requireNonNull(this.getWorld()),
                this.getPos()), inventoryPlayer, menu);
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    @Override
    public LocatorInstance getLocatorInstance() {
        return new TileEntityLocatorInstance(this.pos);
    }

    public abstract T getSelf();
}
