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
                currentRecipe = RecipeUtil.getRecipes(world, SpinningWheelRecipe.SERIALIZER.getRecipeType()).stream().filter(wheelRecipe -> wheelRecipe.matches(input)).findFirst().orElse(null);
                progress = 0;
            }
        }
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
        }
    }

    private boolean fullProgress() {
        return progress >= 100;
    }

    @Override
    public boolean onActivated(PlayerEntity playerIn, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        int j = 0;
        if (!playerIn.getHeldItem(hand).isEmpty()) {
            Item item = playerIn.getHeldItem(hand).getItem();
            Workshop.LOGGER.info(item.toString());
            if (j == 0) {
                input.insertItem(0, item.getDefaultInstance(), false);
                Workshop.LOGGER.info("item inserted");
                ++j;
            }
        } else if(playerIn.getHeldItem(hand).isEmpty() && playerIn.isSneaking()) {
            int max = output.getStackInSlot(0).getCount();
            if (output.extractItem(0, max, false).isEmpty()) {
                for(int i = 0; i < input.getSlots(); ++i) {
                    if (!input.extractItem(0, 1, false).isEmpty()) {
                        int inputmax = input.getStackInSlot(0).getCount();
                        ItemStack Item = input.extractItem(0, inputmax, false);
                        Workshop.LOGGER.info("input stack extracted");
                        playerIn.inventory.add(inputmax,Item);
                        ++j;
                    }
                }
            } else {
                ItemStack stack = output.extractItem(0, max, false);
                Workshop.LOGGER.info("ouput stack extracted");
                playerIn.inventory.addItemStackToInventory(stack);
                Workshop.LOGGER.info("stack added to inv");
            }
        } else if(playerIn.getHeldItem(hand).isEmpty()) {
            checkForRecipe();
            if (!fullProgress() && currentRecipe != null) {
                    //ToDo: insert quarter spin here
                    this.progress += 25;
                    Workshop.LOGGER.info(progress);
                    return true;
            } else if (fullProgress()) {
                progress = 0;
                onFinish();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
