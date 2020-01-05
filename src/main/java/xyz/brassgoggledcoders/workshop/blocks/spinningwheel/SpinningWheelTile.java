package xyz.brassgoggledcoders.workshop.blocks.spinningwheel;


import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;
import com.hrznstudio.titanium.util.RecipeUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.recipes.SpinningWheelRecipe;
import xyz.brassgoggledcoders.workshop.registries.Recipes;

import static xyz.brassgoggledcoders.workshop.blocks.BlockNames.SPINNING_WHEEL_BLOCK;

public class SpinningWheelTile extends TileActive {

    @Save
    private SidedInvHandler input;
    @Save
    private SidedInvHandler output;

    private SpinningWheelRecipe currentRecipe;

    private int progress = 0;

    public SpinningWheelTile() {
        super(SPINNING_WHEEL_BLOCK);
        this.addInventory(this.input = (SidedInvHandler) new SidedInvHandler("input", 34, 25, 3, 0)
                .setColor(DyeColor.RED)
                .setRange(1, 3)
                .setOnSlotChanged((stack, integer) -> checkForRecipe())
                .setTile(this));
        this.addInventory(this.output = (SidedInvHandler) new SidedInvHandler("output", 102, 44, 1, 0)
                .setColor(DyeColor.BLACK)
                .setInputFilter((stack, integer) -> false)
                .setTile(this));
    }

    private void checkForRecipe() {
        if (isServer()) {
            if (currentRecipe == null || !currentRecipe.matches(input)) {
                currentRecipe = this.getWorld().getRecipeManager()
                        .getRecipes()
                        .stream()
                        .filter(recipe -> recipe.getType() == Recipes.SPINNING_WHEEL)
                        .map(recipe -> (SpinningWheelRecipe) recipe)
                        .filter(this::matches)
                        .findFirst()
                        .orElse(null);
                progress = 0;
            }
        }
    }

    private boolean matches(SpinningWheelRecipe wheelRecipe) {
        return wheelRecipe.matches(input);
    }

    private void onFinish() {
        if (currentRecipe != null) {
            SpinningWheelRecipe wheelRecipe = currentRecipe;
            for (Ingredient.IItemList iItemList : wheelRecipe.itemsIn) {
                boolean found = false;
                for (ItemStack stack : iItemList.getStacks()) {
                    int i = 0;
                    for (; i < input.getSlots(); i++) {
                        ItemStack stack2 = input.getStackInSlot(i);
                        if (stack2.isItemEqual(stack)) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        ItemStack stack2 = input.getStackInSlot(i);
                        stack2.shrink(1);
                        break;
                    }
                }
            }
            if (wheelRecipe.itemOut != null && !wheelRecipe.itemOut.isEmpty()) {
                ItemHandlerHelper.insertItem(output, wheelRecipe.itemOut.copy(), false);
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
}
