package xyz.brassgoggledcoders.workshop.blocks.alembic;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.util.RecipeUtil;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.workshop.blocks.WorkshopGUIMachine;
import xyz.brassgoggledcoders.workshop.recipes.AlembicRecipe;


import static xyz.brassgoggledcoders.workshop.blocks.BlockNames.ALEMBIC_BLOCK;
import static xyz.brassgoggledcoders.workshop.util.WorkTags.Items.COLD;
import static xyz.brassgoggledcoders.workshop.util.WorkTags.Items.FLUIDCONTAINER;


public class AlembicTile extends WorkshopGUIMachine {

    @Save
    private SidedInvHandler input;
    @Save
    private SidedInvHandler container;
    @Save
    private SidedInvHandler residue;
    @Save
    private SidedInvHandler output;
    @Save
    private SidedInvHandler coldItem;
    private AlembicRecipe currentRecipe;
    private int coldTime;

    public AlembicTile() {
        super(ALEMBIC_BLOCK, 76, 42, 100, PosProgressBar.BarDirection.HORIZONTAL_RIGHT);
        this.addInventory(this.input = (SidedInvHandler) new SidedInvHandler("input", 34, 25, 3, 0)
                .setColor(DyeColor.RED)
                .setRange(1, 3)
                .setTile(this));
        this.addInventory(this.container = (SidedInvHandler) new SidedInvHandler("container", 56, 43, 1, 0)
                .setColor(DyeColor.WHITE)
                .setInputFilter((stack, integer) -> stack.getItem().isIn(FLUIDCONTAINER))
                .setTile(this));
        this.addInventory(this.residue = (SidedInvHandler) new SidedInvHandler("residue", 125, 25, 3, 0)
                .setColor(DyeColor.YELLOW)
                .setRange(1, 3)
                .setInputFilter((stack, integer) -> false)
                .setTile(this));
        this.addInventory(this.output = (SidedInvHandler) new SidedInvHandler("output", 102, 44, 1, 0)
                .setColor(DyeColor.BLACK)
                .setInputFilter((stack, integer) -> false)
                .setTile(this));
        this.addInventory(this.coldItem = (SidedInvHandler) new SidedInvHandler("coldItem", 79, 20, 1, 0)
                .setColor(DyeColor.LIGHT_BLUE)
                .setInputFilter((stack, integer) -> stack.getItem().isIn(COLD))
                .setTile(this));
    }

    public void checkForRecipe() {
        if (isServer()) {
            if (currentRecipe != null && currentRecipe.matches(input, container)) {
                return;
            }
            currentRecipe = RecipeUtil.getRecipes(world, AlembicRecipe.SERIALIZER.getRecipeType()).stream().filter(alembicRecipe -> alembicRecipe.matches(input, container)).findFirst().orElse(null);
        }
    }

    @Override
    public int getMaxProgress() {
        int coldtime = 0;
        if (coldItem == null) {
            coldtime = currentRecipe != null ? currentRecipe.cooldownTime : 100;
            this.coldTime = coldtime;
        } else if (coldItem.getStackInSlot(1).getItem().isIn(COLD)) {
            coldtime = currentRecipe != null ? currentRecipe.cooldownTime / 2 : 100;
            this.coldTime = coldtime;
        }
        return coldtime;
    }


    @Override
    public Runnable onFinish() {
        return () -> {
            if (currentRecipe != null) {
                AlembicRecipe alembicRecipe = currentRecipe;
                for (int i = 0; i < input.getSlots(); i++) {
                    input.getStackInSlot(i).shrink(1);
                }
                for (int i = 0; i < container.getSlots(); i++) {
                    container.getStackInSlot(i).shrink(1);
                }
                if (this.coldTime < currentRecipe.cooldownTime) {
                    for (int i = 0; i < coldItem.getSlots(); i++) {
                        coldItem.getStackInSlot(i).shrink(1);
                    }
                }
                if (alembicRecipe.output != null && !alembicRecipe.output.isEmpty()) {
                    ItemHandlerHelper.insertItem(output, alembicRecipe.output.copy(), false);
                    int size = alembicRecipe.residue.length;
                    for (int i = 0; i < size; ++i) {
                        for (ItemStack residueIn : alembicRecipe.residue) {
                            ItemHandlerHelper.insertItem(residue, residueIn, false);
                        }
                    }
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
                return Pair.of(slotSpacing, -offset - offset);
            case 3:
                return Pair.of(slotSpacing, -offset - offset - offset);
            default:
                return Pair.of(0, 0);
        }
    }
}
