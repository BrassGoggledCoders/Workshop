package xyz.brassgoggledcoders.workshop.blocks.alembic;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;
import com.hrznstudio.titanium.util.RecipeUtil;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.workshop.blocks.WorkShopMachine;
import xyz.brassgoggledcoders.workshop.recipes.AlembicRecipe;
import xyz.brassgoggledcoders.workshop.util.WorkTags;


import static xyz.brassgoggledcoders.workshop.blocks.BlockNames.ALEMBIC_BLOCK;


public class AlembicTile extends WorkShopMachine {

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

    public AlembicTile() {
        super(ALEMBIC_BLOCK, 102, 22);
        this.addInventory(this.input = (SidedInvHandler) new SidedInvHandler("input", 34, 19, 3, 0)
                .setColor(DyeColor.RED)
                .setSlotPosition(integer -> getSlotPos(integer))
                .setTile(this)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.addInventory(this.container = (SidedInvHandler) new SidedInvHandler("container", 34, 10, 1, 0)
                .setColor(DyeColor.WHITE)
                .setTile(this)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.addInventory(this.residue = (SidedInvHandler) new SidedInvHandler("residue", 50, 19, 3, 0)
                .setColor(DyeColor.YELLOW)
                .setInputFilter((stack, integer) -> false)
                .setTile(this));
        this.addInventory(this.output = (SidedInvHandler) new SidedInvHandler("output", 50, 10, 1, 0)
                .setColor(DyeColor.BLACK)
                .setInputFilter((stack, integer) -> false)
                .setTile(this));
        this.addInventory(this.coldItem = (SidedInvHandler) new SidedInvHandler("coldItem", 10, 10, 1, 0)
                .setColor(DyeColor.LIGHT_BLUE)
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
        } else if (coldItem.getStackInSlot(1).getItem().isIn(WorkTags.Items.COLD)) {
            coldtime = currentRecipe != null ? currentRecipe.cooldownTime / 2 : 100;
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
                for (int i = 0; i < coldItem.getSlots(); i++) {
                    coldItem.getStackInSlot(i).shrink(1);
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
