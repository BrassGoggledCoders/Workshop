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
import xyz.brassgoggledcoders.workshop.recipes.SpinningWheelRecipe;

import static xyz.brassgoggledcoders.workshop.blocks.BlockNames.SPINNING_WHEEL_BLOCK;

public class SpinningWheelTile extends TileActive {

    @Save
    private SidedInvHandler input;
    @Save
    private SidedInvHandler output;

    private SpinningWheelRecipe currentRecipe;
    private int progress;

    public SpinningWheelTile() {
        super(SPINNING_WHEEL_BLOCK);
        this.addInventory(this.input = (SidedInvHandler) new SidedInvHandler("input", 34, 25, 3, 0)
                .setColor(DyeColor.RED)
                .setRange(1, 3)
                .setTile(this));
        this.addInventory(this.output = (SidedInvHandler) new SidedInvHandler("output", 102, 44, 1, 0)
                .setColor(DyeColor.BLACK)
                .setInputFilter((stack, integer) -> false)
                .setTile(this));

    }

    private void checkForRecipe() {
        if (isServer()) {
            if (currentRecipe != null && currentRecipe.matches(world, pos)) {
                return;
            }
            currentRecipe = RecipeUtil.getRecipes(world, SpinningWheelRecipe.SERIALIZER.getRecipeType()).stream().filter(wheelRecipe -> wheelRecipe.matches(world, pos)).findFirst().orElse(null);
        }
    }

    private Runnable onFinish() {
        return () -> {
            if (currentRecipe != null) {
                SpinningWheelRecipe wheelRecipe = currentRecipe;
                for (int i = 0; i < input.getSlots(); i++) {
                    input.getStackInSlot(i).shrink(1);
                }
                if(wheelRecipe.output !=null && !wheelRecipe.output.isEmpty()){
                    ItemHandlerHelper.insertItem(output, wheelRecipe.output.copy(), false);
                    //checkForRecipe();
                }
            }
        };
    }

    private boolean fullProgress(){
        return progress >= 100;
    }

    @Override
    public boolean onActivated(PlayerEntity playerIn, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        //ToDo: insert quarter spin here

        if(!playerIn.getHeldItemMainhand().isEmpty()) {
            Item item = playerIn.getHeldItemMainhand().getItem();
            item.getDefaultInstance().shrink(1);
            input.setStackInSlot(1, item.getDefaultInstance());
        }
        if(playerIn.getHeldItemMainhand().isEmpty()){
            ItemStack outputItem = output.getStackInSlot(1);
            ItemStack inputItem = null;
            int slots = input.getSlots();
            for(int i = 0; i <= slots; i++){
                if(inputItem == null){
                    inputItem = input.getStackInSlot(i);
                }else{
                    break;
                }
            }
            if(!outputItem.isEmpty()){
                playerIn.setHeldItem(Hand.MAIN_HAND, outputItem);
                outputItem.shrink(1);
            }else if(!inputItem.isEmpty()){
                playerIn.setHeldItem(Hand.MAIN_HAND, inputItem);
                inputItem.shrink(1);
            }
        }
        checkForRecipe();
        if(!fullProgress() && currentRecipe != null){
            progress = +25;
            return true;
        }else if(fullProgress()){
            progress = 0;
            onFinish();
            return true;
        }else{return false;}
    }
}
