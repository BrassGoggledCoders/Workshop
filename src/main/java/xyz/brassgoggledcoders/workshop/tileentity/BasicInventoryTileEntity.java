package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.sideness.IFacingComponent;
import com.hrznstudio.titanium.component.sideness.IFacingComponentHarness;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import com.hrznstudio.titanium.network.IButtonHandler;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import xyz.brassgoggledcoders.workshop.component.machine.IMachineHarness;
import xyz.brassgoggledcoders.workshop.component.machine.MachineComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

public abstract class BasicInventoryTileEntity<T extends BasicInventoryTileEntity<T>>
        extends TileEntity implements IMachineHarness<T>, INamedContainerProvider, IButtonHandler,
        IFacingComponentHarness, GUITile, INameable, ITickableTileEntity {
    private final MachineComponent<T> machineComponent;
    private ITextComponent customName;

    public BasicInventoryTileEntity(TileEntityType<T> tileEntityType) {
        super(tileEntityType);
        this.machineComponent = new MachineComponent<>(this.getSelf(), this::getPos);
    }

    public MachineComponent<T> getMachineComponent() {
        return this.machineComponent;
    }

    @Override
    public ActionResultType onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ActionResultType result = this.getMachineComponent().onActivated(player, hand, hit);
        if (result == ActionResultType.PASS) {
            if (player instanceof ServerPlayerEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, this, packetBuffer ->
                        LocatorFactory.writePacketBuffer(packetBuffer, new TileEntityLocatorInstance(this.pos)));
            }
            result = ActionResultType.SUCCESS;
        }
        return result;
    }

    @Override
    public void tick() {
        this.getMachineComponent().tick();
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
    @Nonnull
    public ITextComponent getDisplayName() {
        return getCustomName() != null ? getCustomName() : new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey())
                .applyTextStyle(TextFormatting.BLACK);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int menu, PlayerInventory inventoryPlayer, PlayerEntity entityPlayer) {
        return new BasicAddonContainer(this, new TileEntityLocatorInstance(this.pos), IWorldPosCallable.of(Objects.requireNonNull(this.getWorld()),
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

    @Override
    public void read(CompoundNBT compound) {
        if (compound.contains("CustomName", 8)) {
            this.customName = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
        }
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        if (this.customName != null) {
            compound.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
        }
        return super.write(compound);
    }

    @Nonnull
    @Override
    public ITextComponent getName() {
        return customName;
    }

    public void setCustomName(ITextComponent name) {
        this.customName = name;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), -1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Nonnull
    @Override
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
        return this.getMachineComponent().getCapability(cap, side);
    }
}
