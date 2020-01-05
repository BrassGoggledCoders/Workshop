package xyz.brassgoggledcoders.workshop.blocks.sinteringfurnace;


import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.util.RecipeUtil;
import net.minecraft.item.DyeColor;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.workshop.blocks.WorkshopGUIMachine;
import xyz.brassgoggledcoders.workshop.recipes.SeasoningBarrelRecipe;
import xyz.brassgoggledcoders.workshop.recipes.SinteringFurnaceRecipe;
import xyz.brassgoggledcoders.workshop.registries.Recipes;

import static xyz.brassgoggledcoders.workshop.blocks.BlockNames.SINTERING_FURNACE_BLOCK;

public class SinteringFurnaceTile extends WorkshopGUIMachine {

    @Save
    private SidedInvHandler powder;
    @Save
    private SidedInvHandler targetMaterial;
    @Save
    private SidedInvHandler output;
    @Save
    private SidedInvHandler fuel;
    private SinteringFurnaceRecipe currentRecipe;

    public SinteringFurnaceTile() {
        super(SINTERING_FURNACE_BLOCK, 102, 41, 100,  PosProgressBar.BarDirection.HORIZONTAL_RIGHT);

        this.addInventory(this.powder = (SidedInvHandler) new SidedInvHandler("powder", 34, 19, 8, 0)
                .setColor(DyeColor.ORANGE)
                .setSlotPosition(integer -> getSlotPos(integer))
                .setSlotLimit(1)
                .setTile(this)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.addInventory(this.targetMaterial = (SidedInvHandler) new SidedInvHandler("targetMaterial", 34, 19, 8, 0)
                .setColor(DyeColor.YELLOW)
                .setSlotPosition(integer -> getSlotPos(integer))
                .setSlotLimit(1)
                .setTile(this)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.addInventory(this.output = (SidedInvHandler) new SidedInvHandler("output", 34, 19, 8, 0)
                .setColor(DyeColor.BLACK)
                .setSlotPosition(integer -> getSlotPos(integer))
                .setSlotLimit(1)
                .setTile(this)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.addInventory(this.fuel = (SidedInvHandler) new SidedInvHandler("fuel", 34, 19, 8, 0)
                .setColor(DyeColor.RED)
                .setSlotPosition(integer -> getSlotPos(integer))
                .setSlotLimit(1)
                .setTile(this)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));

    }

    @Override
    public int getMaxProgress() {
        return currentRecipe != null ? currentRecipe.meltTime : 100;
    }

    private void checkForRecipe() {
        if (isServer()) {
            if (currentRecipe == null || !currentRecipe.matches(powder,targetMaterial)) {
                currentRecipe = this.getWorld().getRecipeManager()
                        .getRecipes()
                        .stream()
                        .filter(recipe -> recipe.getType() == Recipes.PRESS)
                        .map(recipe -> (SinteringFurnaceRecipe) recipe)
                        .filter(this::matches)
                        .findFirst()
                        .orElse(null);
            }
        }
    }

    private boolean matches(SinteringFurnaceRecipe sinteringFurnaceRecipe) {
        return sinteringFurnaceRecipe.matches(powder, targetMaterial);
    }


    @Override
    public Runnable onFinish() {
        return () -> {
            if (currentRecipe != null) {
                SinteringFurnaceRecipe sinteringFurnaceRecipe = currentRecipe;
                for (int i = 0; i < powder.getSlots(); i++) {
                    powder.getStackInSlot(i).shrink(1);
                }
                for (int i = 0; i < targetMaterial.getSlots(); i++) {
                    targetMaterial.getStackInSlot(i).shrink(1);
                }
                if(sinteringFurnaceRecipe.output !=null && !sinteringFurnaceRecipe.output.isEmpty()){
                    ItemHandlerHelper.insertItem(output, sinteringFurnaceRecipe.output.copy(), false);
                    //checkForRecipe();
                }
            }
        };
    }

    public static Pair<Integer, Integer> getSlotPos(int slot) {
        int slotSpacing = 22;
        int offset = 2;
        switch (slot) {
            case 1:
                return Pair.of(slotSpacing, -offset);
            case 2:
                return Pair.of(slotSpacing * 2, 0);
            case 3:
                return Pair.of(-offset, slotSpacing);
            case 4:
                return Pair.of(slotSpacing * 2 + offset, slotSpacing);
            case 5:
                return Pair.of(0, slotSpacing * 2);
            case 6:
                return Pair.of(slotSpacing, slotSpacing * 2 + offset);
            case 7:
                return Pair.of(slotSpacing * 2, slotSpacing * 2);
            default:
                return Pair.of(0, 0);
        }
    }

}
