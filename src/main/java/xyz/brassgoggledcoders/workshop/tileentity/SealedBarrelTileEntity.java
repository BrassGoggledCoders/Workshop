package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SealedBarrelTileEntity extends TileEntity implements INamedContainerProvider, IComponentHarness, GUITile, IScreenAddonProvider,
        IContainerAddonProvider, ITickableTileEntity {

    public static final int tankCapacity = 4000;//mB;
    private final FluidTankComponent<SealedBarrelTileEntity> tank;
    private final InventoryComponent<SealedBarrelTileEntity> drainingInventory;
    private final InventoryComponent<SealedBarrelTileEntity> fillingInventory;

    @SuppressWarnings("unchecked")
    public SealedBarrelTileEntity() {
        super(WorkshopBlocks.SEALED_BARREL.getTileEntityType());
        int pos = 0;
        this.tank = (FluidTankComponent<SealedBarrelTileEntity>) new SidedFluidTankComponent<>("tank", tankCapacity, 80, 20, pos++)
                .setColor(DyeColor.MAGENTA)
                .setTankAction(SidedFluidTankComponent.Action.BOTH)
                .setValidator(fluidStack -> fluidStack.getFluid().getFluid().getAttributes().getTemperature() < Fluids.LAVA.getAttributes().getTemperature());
        this.tank.setComponentHarness(this);
        this.drainingInventory = new SidedInventoryComponent<SealedBarrelTileEntity>("draining", 50, 50, 1, pos++)
                .setColor(InventoryUtil.FLUID_INPUT_COLOR)
                .setInputFilter((stack, integer) -> FluidUtil.getFluidHandler(stack).isPresent());
        this.fillingInventory = new SidedInventoryComponent<SealedBarrelTileEntity>("filling", 120, 50, 1, pos++)
                .setColor(InventoryUtil.FLUID_OUTPUT_COLOR)
                .setInputFilter((stack, slot) -> FluidUtil.getFluidHandler(stack).isPresent());
    }

    @Override
    public void read(CompoundNBT compound) {
        tank.readFromNBT(compound.getCompound("capability"));
        drainingInventory.deserializeNBT(compound.getCompound("draining"));
        fillingInventory.deserializeNBT(compound.getCompound("filling"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("capability", tank.writeToNBT(new CompoundNBT()));
        compound.put("draining", drainingInventory.serializeNBT());
        compound.put("filling", fillingInventory.serializeNBT());
        return super.write(compound);
    }

    @Override
    public World getComponentWorld() {
        return this.getWorld();
    }

    @Override
    public void markComponentForUpdate(boolean reference) {
    }

    @Override
    public void markComponentDirty() {
        this.markDirty();
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey())
                .applyTextStyle(TextFormatting.DARK_GRAY);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int menu, PlayerInventory inventoryPlayer, PlayerEntity entityPlayer) {
        return new BasicAddonContainer(this, new TileEntityLocatorInstance(this.pos), IWorldPosCallable.of(Objects.requireNonNull(this.getWorld()),
                this.getPos()), inventoryPlayer, menu);
    }

    @Override
    public ActionResultType onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (player instanceof ServerPlayerEntity) {
            if (!player.isCrouching() && FluidUtil.interactWithFluidHandler(player, hand, this.tank)) {
                return ActionResultType.SUCCESS;
            }
            NetworkHooks.openGui((ServerPlayerEntity) player, this, packetBuffer ->
                    LocatorFactory.writePacketBuffer(packetBuffer, new TileEntityLocatorInstance(this.pos)));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = new ArrayList<>();
        addons.addAll(this.tank.getScreenAddons());
        addons.addAll(this.fillingInventory.getScreenAddons());
        addons.addAll(this.drainingInventory.getScreenAddons());
        return addons;
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> addons = new ArrayList<>();
        addons.addAll(this.tank.getContainerAddons());
        addons.addAll(this.fillingInventory.getContainerAddons());
        addons.addAll(this.drainingInventory.getContainerAddons());
        return addons;
    }

    //Can't do it onSlotChanged like I wanted because I can't then set the stack without an infinite loop
    @Override
    public void tick() {
        if(!this.getWorld().isRemote) {
            ItemStack drainingInventoryStackInSlot = this.drainingInventory.getStackInSlot(0);
            if (!drainingInventoryStackInSlot.isEmpty()) {
                if (FluidUtil.tryEmptyContainer(drainingInventoryStackInSlot, this.tank, tankCapacity, null, false).isSuccess()) {
                    this.drainingInventory.setStackInSlot(0, FluidUtil.tryEmptyContainer(drainingInventoryStackInSlot, this.tank, tankCapacity, null, true).getResult());
                }
            }
            ItemStack fillingInventoryStackInSlot = this.fillingInventory.getStackInSlot(0);
            if (!this.tank.isEmpty() && !fillingInventoryStackInSlot.isEmpty()) {
                if (FluidUtil.tryFillContainer(fillingInventoryStackInSlot, this.tank, FluidAttributes.BUCKET_VOLUME, null, false).isSuccess()) {
                    this.fillingInventory.setStackInSlot(0, FluidUtil.tryFillContainer(fillingInventoryStackInSlot, this.tank, FluidAttributes.BUCKET_VOLUME, null, true).getResult());
                }
            }
        }
    }
}
