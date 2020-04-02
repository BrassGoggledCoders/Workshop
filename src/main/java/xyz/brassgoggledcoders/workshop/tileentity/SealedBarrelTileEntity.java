package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.component.sideness.IFacingComponent;
import com.hrznstudio.titanium.component.sideness.IFacingComponentHarness;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.network.IButtonHandler;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.component.machine.IMachineHarness;
import xyz.brassgoggledcoders.workshop.component.machine.MachineComponent;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.AlembicRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class SealedBarrelTileEntity extends TileEntity implements INamedContainerProvider, IComponentHarness, GUITile, IScreenAddonProvider,
        IContainerAddonProvider {

    private SidedFluidTankComponent<SealedBarrelTileEntity> tank;

    public SealedBarrelTileEntity() {
        super(WorkshopBlocks.SEALED_BARREL.getTileEntityType());
        this.tank = (SidedFluidTankComponent<SealedBarrelTileEntity>) new SidedFluidTankComponent<SealedBarrelTileEntity>("tank", 4000, 100, 20, 0).
                setColor(DyeColor.MAGENTA).
                setTankAction(SidedFluidTankComponent.Action.BOTH)
                .setValidator(fluidStack -> fluidStack.getFluid().getFluid().getAttributes().getTemperature() < Fluids.LAVA.getAttributes().getTemperature());
        this.tank.setComponentHarness(this);
    }

    @Override
    public void read(CompoundNBT compound) {
        tank.readFromNBT(compound.getCompound("capability"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("capability", tank.writeToNBT(new CompoundNBT()));
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
        return new BasicAddonContainer(this, IWorldPosCallable.of(Objects.requireNonNull(this.getWorld()),
                this.getPos()), inventoryPlayer, menu);
    }

    @Override
    public ActionResultType onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (player instanceof ServerPlayerEntity) {
            if(!player.isCrouching() && FluidUtil.interactWithFluidHandler(player, hand, this.tank)) {
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
        return addons;
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> addons = new ArrayList<>();
        addons.addAll(this.tank.getContainerAddons());
        return addons;
    }
}
