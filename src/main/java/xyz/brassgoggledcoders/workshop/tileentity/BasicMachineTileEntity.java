package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.component.sideness.IFacingComponent;
import com.hrznstudio.titanium.component.sideness.IFacingComponentHarness;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import com.hrznstudio.titanium.network.IButtonHandler;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.crafting.IRecipe;
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

public abstract class BasicMachineTileEntity<T extends BasicMachineTileEntity<T, U>, U extends IRecipe<IInventory>>
        extends TileEntity implements IMachineHarness<T, U>, ITickableTileEntity, INamedContainerProvider, IButtonHandler,
        IFacingComponentHarness, GUITile {
    private final MachineComponent<T, U> machineComponent;

    public BasicMachineTileEntity(TileEntityType<T> tileEntityType, ProgressBarComponent<T> progressBar) {
        super(tileEntityType);
        this.machineComponent = new MachineComponent<>(this.getSelf(), this::getPos, progressBar);
    }

    public MachineComponent<T, U> getMachineComponent() {
        return this.machineComponent;
    }

    @Override
    public ActionResultType onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ActionResultType result = this.getMachineComponent().onActivated(player, hand, hit);
        if (result == ActionResultType.PASS && player instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) player, this, packetBuffer ->
                    LocatorFactory.writePacketBuffer(packetBuffer, new TileEntityLocatorInstance(this.pos)));
            result = ActionResultType.SUCCESS;
        }
        return result;
    }

    @Override
    public World getComponentWorld() {
        return this.world;
    }

    @Override
    public void markComponentForUpdate(boolean reference) {
    }

    @Override
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

    @Override
    public void handleButtonMessage(int i, PlayerEntity playerEntity, CompoundNBT compoundNBT) {
        this.machineComponent.handleButtonMessage(i, playerEntity, compoundNBT);
    }

    @Override
    public IFacingComponent getHandlerFromName(String name) {
        return this.machineComponent.getHandlerFromName(name);
    }
}
