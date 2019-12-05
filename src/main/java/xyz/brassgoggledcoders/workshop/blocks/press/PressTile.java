package xyz.brassgoggledcoders.workshop.blocks.press;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.block.tile.fluid.SidedFluidTank;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.util.RecipeUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import xyz.brassgoggledcoders.workshop.recipes.PressRecipes;

import static xyz.brassgoggledcoders.workshop.blocks.BlockNames.PRESS_BLOCK;

public class PressTile extends TileActive {

    @Save
    private PosProgressBar progressBar;
    @Save
    private SidedInvHandler input;
    @Save
    private SidedFluidTank outputFluid;


    private PressRecipes currentRecipe;

    public PressTile() {
        super(PRESS_BLOCK);
        this.addProgressBar(progressBar = new PosProgressBar(0, 0, 120).
                setTile(this).
                setBarDirection(PosProgressBar.BarDirection.HORIZONTAL_RIGHT).
                setCanReset(tileEntity -> true).
                setOnStart(() -> progressBar.setMaxProgress(getMaxProgress())).
                setOnFinishWork(() -> onFinish().run()));
        this.addInventory(this.input = (SidedInvHandler) new SidedInvHandler("input", 34, 25, 1, 0)
                .setColor(DyeColor.RED)
                .setTile(this)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.addTank(this.outputFluid = (SidedFluidTank) new SidedFluidTank("output_fluid", 4000, 149, 20, 3).
                setColor(DyeColor.MAGENTA).
                setTile(this).
                setTankAction(PosFluidTank.Action.DRAIN));

    }

    private Runnable onFinish(){
        return () -> {
            if (currentRecipe != null) {
                PressRecipes pressRecipes = currentRecipe;
                input.getStackInSlot(0).shrink(1);
                outputFluid.fill(currentRecipe.output,);
            }

        };
    }

    @Override
    public boolean onActivated(PlayerEntity playerIn, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        if(heldItem.isEmpty()){
            int max = input.getStackInSlot(0).getCount();
            input.extractItem(0,max,false);
            return true;
        }
        else if(heldItem.equals(Items.BUCKET.getDefaultInstance())){
            return true;
        }else{
            input.insertItem(0, heldItem, false);
            return true;
        }
    }

    public int getMaxProgress(){
        return 120;
    }

    private void checkForRecipe(){
        if (!world.isRemote) {
            if (currentRecipe != null && currentRecipe.matches(input)) {
                return;
            }
            currentRecipe = RecipeUtil.getRecipes(world, PressRecipes.SERIALIZER.getRecipeType()).stream().filter(alembicRecipe -> alembicRecipe.matches(input)).findFirst().orElse(null);
        }
    }

}
