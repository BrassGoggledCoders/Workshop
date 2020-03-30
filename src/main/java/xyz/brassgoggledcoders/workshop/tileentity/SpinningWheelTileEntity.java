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

import javax.annotation.Nonnull;

public class SpinningWheelTileEntity extends BasicMachineTileEntity<SpinningWheelTileEntity, SpinningWheelRecipe> {

    private InventoryComponent<SpinningWheelTileEntity> inputInventory;
    private InventoryComponent<SpinningWheelTileEntity> outputInventory;

    private SpinningWheelRecipe currentRecipe;

    private int progress = 0;

    public SpinningWheelTileEntity() {
        super(WorkshopBlocks.SPINNING_WHEEL.getTileEntityType(), new ProgressBarComponent<>(0, 0, 100));
        this.getMachineComponent().addInventory(this.inputInventory = new SidedInventoryComponent<SpinningWheelTileEntity>(
                "inputInventory", 34, 25, 3, 0)
                .setColor(DyeColor.RED)
                .setRange(1, 3)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.getMachineComponent().addInventory(this.outputInventory = new SidedInventoryComponent("outputInventory", 102, 44, 1, 0)
                .setColor(DyeColor.BLACK)
                .setInputFilter((stack, integer) -> false));
    }


    @Override
    public void read(CompoundNBT compound) {
        inputInventory.deserializeNBT(compound.getCompound("inputInventory"));
        outputInventory.deserializeNBT(compound.getCompound("outputInventory"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inputInventory", inputInventory.serializeNBT());
        compound.put("outputInventory", outputInventory.serializeNBT());
        return super.write(compound);
    }

    private void checkForRecipe() {
        if (this.getWorld() != null && !this.getWorld().isRemote()) {
            if (currentRecipe == null || !currentRecipe.matches(inputInventory)) {
                currentRecipe = this.getWorld().getRecipeManager()
                        .getRecipes()
                        .stream()
                        .filter(recipe -> recipe.getType() == WorkshopRecipes.SPINNING_WHEEL)
                        .map(recipe -> (SpinningWheelRecipe) recipe)
                        .filter(this::matches)
                        .findFirst()
                        .orElse(null);
                progress = 0;
            }
        }
    }

    private boolean matches(SpinningWheelRecipe wheelRecipe) {
        return wheelRecipe.matches(inputInventory);
    }

    @Override
    public SpinningWheelTileEntity getSelf() {
        return this;
    }

    private boolean fullProgress() {
        return progress >= 8;
    }

    @Override
    public ActionResultType onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (this.getWorld() != null && !this.getWorld().isRemote()) {
            if (!player.isCrouching()) {
                extractInsertItem(player, hand);
            } else {
                if (!fullProgress() && currentRecipe != null) {
                    progress += 1;
                    if (fullProgress()) {
                        //onFinish();
                        progress = 0;
                    }
                } else {
                    checkForRecipe();
                }
            }
        }
        return ActionResultType.PASS;
    }

    public void extractInsertItem(PlayerEntity player, Hand hand) {
        ItemStack held = player.getHeldItem(hand);
        boolean used = false;
        if (!held.isEmpty()) {
            int slots = inputInventory.getSlots();
            for (int x = 0; x < slots; ++x) {
                if (!used) {
                    if (inputInventory.getStackInSlot(x).isEmpty()) {
                        ItemStack heldCopy = held.copy();
                        int count = held.getCount();
                        inputInventory.insertItem(x, heldCopy, false);
                        held.shrink(count);
                        used = true;
                    }
                }
            }
        } else {
            ItemStack outStack = outputInventory.getStackInSlot(0);
            if (!outStack.isEmpty()) {
                int count = outStack.getCount();
                ItemStack item = outputInventory.extractItem(0, count, false);
                player.addItemStackToInventory(item);
            } else {
                int slots = inputInventory.getSlots();
                for (int x = 0; x < slots; ++x) {
                    if (!used) {
                        ItemStack inStack = inputInventory.getStackInSlot(x);
                        if (!inStack.isEmpty()) {
                            int count = inStack.getCount();
                            ItemStack item = inputInventory.extractItem(x, count, false);
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
        return false;
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return false;
    }

    @Override
    public SpinningWheelRecipe castRecipe(IRecipe<?> iRecipe) {
        return null;
    }

    @Override
    public int getProcessingTime(SpinningWheelRecipe currentRecipe) {
        return 0;
    }

    @Override
    public boolean matchesInputs(SpinningWheelRecipe currentRecipe) {
        return false;
    }

    @Override
    public void handleComplete(SpinningWheelRecipe currentRecipe) {

    }
}
