package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.SpinningWheelRecipe;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;

public class SpinningWheelTileEntity extends BasicMachineTileEntity<SpinningWheelTileEntity, SpinningWheelRecipe> {

    private InventoryComponent<SpinningWheelTileEntity> input;
    private InventoryComponent<SpinningWheelTileEntity> output;

    private SpinningWheelRecipe currentRecipe;

    public SpinningWheelTileEntity() {
        super(WorkshopBlocks.SPINNING_WHEEL.getTileEntityType(), new ProgressBarComponent<>(0, 0, 100));
        int pos = 0;
        this.getMachineComponent().addInventory(this.input = new SidedInventoryComponent<SpinningWheelTileEntity>(
                "inputInventory", 34, 25, 3, pos++)
                .setColor(DyeColor.RED)
                .setRange(1, 3)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addInventory(this.output = new SidedInventoryComponent<SpinningWheelTileEntity>(
                "outputInventory", 102, 44, 1, pos++)
                .setColor(DyeColor.BLACK)
                .setInputFilter((stack, integer) -> false));
    }


    @Override
    public void read(CompoundNBT compound) {
        input.deserializeNBT(compound.getCompound("inputInventory"));
        output.deserializeNBT(compound.getCompound("outputInventory"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inputInventory", input.serializeNBT());
        compound.put("outputInventory", output.serializeNBT());
        return super.write(compound);
    }

    @Override
    public SpinningWheelTileEntity getSelf() {
        return this;
    }

    @Override
    public ActionResultType onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (this.getWorld() != null && !this.getWorld().isRemote()) {
            if (!player.isCrouching()) {
                extractInsertItem(player, hand);
                super.onActivated(player, hand, hit);
            } else {

            }
        }
        return super.onActivated(player, hand, hit);
    }

    @Deprecated
    public void extractInsertItem(PlayerEntity player, Hand hand) {
        ItemStack held = player.getHeldItem(hand);
        boolean used = false;
        if (!held.isEmpty()) {
            int slots = input.getSlots();
            for (int x = 0; x < slots; ++x) {
                if (!used) {
                    if (input.getStackInSlot(x).isEmpty()) {
                        ItemStack heldCopy = held.copy();
                        int count = held.getCount();
                        input.insertItem(x, heldCopy, false);
                        held.shrink(count);
                        used = true;
                    }
                }
            }
        } else {
            ItemStack outStack = output.getStackInSlot(0);
            if (!outStack.isEmpty()) {
                int count = outStack.getCount();
                ItemStack item = output.extractItem(0, count, false);
                player.addItemStackToInventory(item);
            } else {
                int slots = input.getSlots();
                for (int x = 0; x < slots; ++x) {
                    if (!used) {
                        ItemStack inStack = input.getStackInSlot(x);
                        if (!inStack.isEmpty()) {
                            int count = inStack.getCount();
                            ItemStack item = input.extractItem(x, count, false);
                            player.addItemStackToInventory(item);
                            used = true;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean hasInputs() {
        return InventoryUtil.anySlotsHaveItems(input);
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == WorkshopRecipes.SPINNING_WHEEL && recipe instanceof SpinningWheelRecipe;
    }

    @Override
    public SpinningWheelRecipe castRecipe(IRecipe<?> iRecipe) {
        return (SpinningWheelRecipe) iRecipe;
    }

    @Override
    public int getProcessingTime(SpinningWheelRecipe currentRecipe) {
        return currentRecipe.getProcessingTime();
    }

    @Override
    public boolean matchesInputs(SpinningWheelRecipe currentRecipe) {
        return currentRecipe.matches(input);
    }

    @Override
    public void handleComplete(SpinningWheelRecipe currentRecipe) {
        input.getStackInSlot(0).shrink(1);
        ItemStack itemOut = currentRecipe.output;
        if (itemOut != null) {
            output.insertItem(0, itemOut, false);
        }
    }
}
