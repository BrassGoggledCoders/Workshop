package xyz.brassgoggledcoders.workshop.blocks.spinningwheel;


import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;
import com.hrznstudio.titanium.util.RecipeUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
                        .filter(recipe -> recipe.getType() == SpinningWheelRecipe.SERIALIZER.getRecipeType())
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
            for (int i = 0; i < input.getSlots(); i++) {
                input.getStackInSlot(i).shrink(1);
            }
            if (wheelRecipe.output != null && !wheelRecipe.output.isEmpty()) {
                ItemHandlerHelper.insertItem(output, wheelRecipe.output.copy(), false);
                //checkForRecipe();
            }
            checkForRecipe();
        }
    }

    private boolean fullProgress() {
        return progress >= 4;
    }

    @Override
    public boolean onActivated(PlayerEntity playerIn, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (!playerIn.isSneaking()) {
            if(!world.isRemote) {
                extractInsertItem(playerIn, hand);
            }
        } else{
            if (!fullProgress() && currentRecipe != null) {
                //ToDo: insert quarter spin here
                progress += 1;
                Workshop.LOGGER.info(progress);
                return true;
            } else if (fullProgress()) {
                progress = 0;
                onFinish();
                return true;
            } else {
                checkForRecipe();
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
                    if(input.getStackInSlot(x).isEmpty()) {
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
            if(!outStack.isEmpty()) {
                int count = outStack.getCount();
                ItemStack item = output.extractItem(0, count, false);
                player.addItemStackToInventory(item);
            }else{
                int slots = input.getSlots();
                for (int x = 0; x < slots; ++x) {
                    if (!used) {
                        ItemStack inStack = input.getStackInSlot(x);
                        if(!inStack.isEmpty()){
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
