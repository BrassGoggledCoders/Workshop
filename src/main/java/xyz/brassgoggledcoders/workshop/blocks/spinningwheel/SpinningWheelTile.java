package xyz.brassgoggledcoders.workshop.blocks.spinningwheel;


import static xyz.brassgoggledcoders.workshop.blocks.BlockNames.SPINNING_WHEEL_BLOCK;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.recipes.SpinningWheelRecipe;
import xyz.brassgoggledcoders.workshop.registries.WorkshopRecipes;

public class SpinningWheelTile extends TileActive {

    @Save
    private SidedInvHandler inputInventory;
    @Save
    private SidedInvHandler outputInventory;

    private SpinningWheelRecipe currentRecipe;

    private int progress = 0;

    public SpinningWheelTile() {
        super(SPINNING_WHEEL_BLOCK);
        this.addInventory(this.inputInventory = (SidedInvHandler) new SidedInvHandler("inputInventory", 34, 25, 3, 0)
                .setColor(DyeColor.RED)
                .setRange(1, 3)
                .setOnSlotChanged((stack, integer) -> checkForRecipe())
                .setTile(this));
        this.addInventory(this.outputInventory = (SidedInvHandler) new SidedInvHandler("outputInventory", 102, 44, 1, 0)
                .setColor(DyeColor.BLACK)
                .setInputFilter((stack, integer) -> false)
                .setTile(this));
    }

    private void checkForRecipe() {
        if (isServer()) {
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

    private void onFinish() {
        if (currentRecipe != null) {
            SpinningWheelRecipe wheelRecipe = currentRecipe;
            for (Ingredient.IItemList iItemList : wheelRecipe.itemsIn) {
                boolean found = false;
                for (ItemStack stack : iItemList.getStacks()) {
                    int i = 0;
                    for (; i < inputInventory.getSlots(); i++) {
                        ItemStack stack2 = inputInventory.getStackInSlot(i);
                        if (stack2.isItemEqual(stack)) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        ItemStack stack2 = inputInventory.getStackInSlot(i);
                        stack2.shrink(1);
                        break;
                    }
                }
            }
            if (wheelRecipe.itemOut != null && !wheelRecipe.itemOut.isEmpty()) {
                ItemHandlerHelper.insertItem(outputInventory, wheelRecipe.itemOut.copy(), false);
                //checkForRecipe();
            }
            checkForRecipe();
        }
    }

    private boolean fullProgress() {
        return progress >= 8;
    }

    @Override
    public boolean onActivated(PlayerEntity playerIn, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (!world.isRemote) {
            if (!playerIn.isSneaking()) {
                extractInsertItem(playerIn, hand);
            } else {
                if (!fullProgress() && currentRecipe != null) {
                    progress += 1;
                    if (fullProgress()) {
                        onFinish();
                        progress = 0;
                    }
                } else {
                    checkForRecipe();
                }
            }
        }
        return false;
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
}
