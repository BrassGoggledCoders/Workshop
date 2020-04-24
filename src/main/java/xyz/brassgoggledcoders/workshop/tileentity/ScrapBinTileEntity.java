package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
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
import net.minecraft.item.Item;
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
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

public class ScrapBinTileEntity extends TileEntity implements INamedContainerProvider, IComponentHarness, GUITile, IScreenAddonProvider,
        IContainerAddonProvider {
    private final SidedInventoryComponent<ScrapBinTileEntity> inventoryComponent;
    //TODO NBT Sensitivity
    private final Map<Item, Integer> totalPerItemType = new HashMap<>();

    public ScrapBinTileEntity() {
        super(WorkshopBlocks.SCRAP_BIN.getTileEntityType());
        int pos = 0;
        this.inventoryComponent = (SidedInventoryComponent<ScrapBinTileEntity>) new SidedInventoryComponent<ScrapBinTileEntity>("inventory", 34, 25, 27, pos++)
                .setColor(DyeColor.WHITE)
                .setOnSlotChanged((stack, slotNum) -> {
                    Item item = stack.getItem();
                    if (totalPerItemType.containsKey(item)) {
                        totalPerItemType.put(item, totalPerItemType.get(item) + stack.getCount());
                        if (totalPerItemType.get(item) > 64) {
                            stack.setCount(0);
                        }
                    } else {
                        totalPerItemType.put(item, stack.getCount());
                    }
                    Workshop.LOGGER.error(stack.getDisplayName().getString() + slotNum);
                    totalPerItemType.forEach((s, sN) -> Workshop.LOGGER.warn(s.getName().getString() + sN));
                });
    }

    @Override
    public void read(CompoundNBT compound) {
        inventoryComponent.deserializeNBT(compound.getCompound("inventory"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inventory", inventoryComponent.serializeNBT());
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
        return addons;
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> addons = new ArrayList<>();
        addons.addAll(this.inventoryComponent.getContainerAddons());
        return addons;
    }
}
