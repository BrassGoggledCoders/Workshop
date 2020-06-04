package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopConfig;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScrapBinTileEntity extends TileEntity implements INamedContainerProvider, IComponentHarness, GUITile, IScreenAddonProvider,
        IContainerAddonProvider {
    private final SidedInventoryComponent<ScrapBinTileEntity> inventoryComponent;
    private final SidedInventoryComponent<ScrapBinTileEntity> scrapOutput;
    private final ProgressBarComponent<ScrapBinTileEntity> scrapValue;

    public ScrapBinTileEntity() {
        super(WorkshopBlocks.SCRAP_BIN.getTileEntityType());
        int pos = 0;
        this.inventoryComponent = (SidedInventoryComponent<ScrapBinTileEntity>) new SidedInventoryComponent<ScrapBinTileEntity>("inventory", 8, 16, 24, pos++)
                .setColor(DyeColor.WHITE)
                .setRange(8, 3);
        this.scrapOutput = new SidedInventoryComponent<ScrapBinTileEntity>("output", 150, 75, 1, pos++).setColor(DyeColor.BLACK);
        this.scrapValue = new ProgressBarComponent<>(155, 8, WorkshopConfig.COMMON.itemsRequiredPerScrapBag.get());
        this.inventoryComponent.setOnSlotChanged((stack, slotNum) -> {
            if (ItemHandlerHelper.insertItem(scrapOutput, new ItemStack(WorkshopItems.SCRAP_BAG.get()), true).isEmpty()) {
                //TODO Caching
                int count = stack.getCount();
                for (int i = 0; i < this.inventoryComponent.getSlots(); i++) {
                    if (i != slotNum) {
                        ItemStack otherStack = this.inventoryComponent.getStackInSlot(i);
                        if (ItemStack.areItemsEqual(stack, otherStack)) {
                            count += otherStack.getCount();
                        }
                    }
                }
                //If we have more than a stack total
                if (count > 64) {
                    //Shrink by the amount we are over a stack
                    int diff = count - 64;
                    stack.shrink(diff);
                    //Add that value to the scrap counter
                    this.scrapValue.setProgress(Math.min(this.scrapValue.getProgress() + diff, this.scrapValue.getMaxProgress()));
                    this.scrapValue.tickBar();
                }
            }
        });
        this.scrapValue.setOnFinishWork(() -> ItemHandlerHelper.insertItem(scrapOutput, new ItemStack(WorkshopItems.SCRAP_BAG.get()), false));
    }

    @Override
    public void read(CompoundNBT compound) {
        inventoryComponent.deserializeNBT(compound.getCompound("inventory"));
        scrapOutput.deserializeNBT(compound.getCompound("scrap"));
        scrapValue.deserializeNBT(compound.getCompound("scrapValue"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inventory", inventoryComponent.serializeNBT());
        compound.put("scrap", scrapOutput.serializeNBT());
        compound.put("scrapValue", scrapValue.serializeNBT());
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
            NetworkHooks.openGui((ServerPlayerEntity) player, this, packetBuffer ->
                    LocatorFactory.writePacketBuffer(packetBuffer, new TileEntityLocatorInstance(this.pos)));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = new ArrayList<>();
        addons.addAll(this.inventoryComponent.getScreenAddons());
        addons.addAll(this.scrapOutput.getScreenAddons());
        addons.addAll(this.scrapValue.getScreenAddons());
        return addons;
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> addons = new ArrayList<>();
        addons.addAll(this.inventoryComponent.getContainerAddons());
        addons.addAll(this.scrapOutput.getContainerAddons());
        addons.addAll(this.scrapValue.getContainerAddons());
        return addons;
    }
}
