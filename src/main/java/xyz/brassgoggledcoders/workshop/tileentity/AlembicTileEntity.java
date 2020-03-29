package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.asset.HeatBarComponent;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.AlembicRecipe;

import javax.annotation.Nonnull;

import static xyz.brassgoggledcoders.workshop.content.WorkshopTags.Items.COLD;
import static xyz.brassgoggledcoders.workshop.content.WorkshopTags.Items.FLUIDCONTAINER;

public class AlembicTileEntity extends WorkshopGUIMachineHarness<AlembicTileEntity> {

    private InventoryComponent<AlembicTileEntity> input;
    private InventoryComponent<AlembicTileEntity> container;
    private InventoryComponent<AlembicTileEntity> residue;
    private InventoryComponent<AlembicTileEntity> output;
    private InventoryComponent<AlembicTileEntity> coldItem;
    private HeatBarComponent<AlembicTileEntity> alembicTemp;

    private AlembicRecipe currentRecipe;
    private int coldTime;
    private int temp;
    private int maxTemp = 5000;

    public AlembicTileEntity() {
        super(WorkshopBlocks.ALEMBIC.getTileEntityType(), new ProgressBarComponent<AlembicTileEntity>(76, 42, 100).setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT));
        this.getMachineComponent().addInventory(this.input = new SidedInventoryComponent<AlembicTileEntity>("input", 34, 25, 3, 0)
                .setColor(DyeColor.RED)
                .setRange(1, 3));
        this.getMachineComponent().addInventory(this.container = new SidedInventoryComponent<AlembicTileEntity>("container", 56, 43, 1, 0)
                .setColor(DyeColor.WHITE)
                .setInputFilter((stack, integer) -> stack.getItem().isIn(FLUIDCONTAINER)));
        this.getMachineComponent().addInventory(this.residue = new SidedInventoryComponent<AlembicTileEntity>(
                "residue", 125, 25, 3, 0)
                .setColor(DyeColor.YELLOW)
                .setRange(1, 3)
                .setInputFilter((stack, integer) -> false));
        this.getMachineComponent().addInventory(this.output = new SidedInventoryComponent<AlembicTileEntity>("output", 102, 44, 1, 0)
                .setColor(DyeColor.BLACK)
                .setInputFilter((stack, integer) -> false));
        this.getMachineComponent().addInventory(this.coldItem = new SidedInventoryComponent<AlembicTileEntity>("coldItem", 79, 20, 1, 0)
                .setColor(DyeColor.LIGHT_BLUE)
                .setInputFilter((stack, integer) -> stack.getItem().isIn(COLD)));
        this.alembicTemp = new HeatBarComponent<AlembicTileEntity>(100, 20, temp, getMaxTemp()).setColor(DyeColor.LIGHT_BLUE);
    }

    @Override
    public void read(CompoundNBT compound) {
        input.deserializeNBT(compound.getCompound("input"));
        container.deserializeNBT(compound.getCompound("container"));
        residue.deserializeNBT(compound.getCompound("residue"));
        output.deserializeNBT(compound.getCompound("output"));
        coldItem.deserializeNBT(compound.getCompound("coldItem"));
        alembicTemp.deserializeNBT(compound.getCompound("temp"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("input", input.serializeNBT());
        compound.put("container", container.serializeNBT());
        compound.put("residue", container.serializeNBT());
        compound.put("output", container.serializeNBT());
        compound.put("coldItem", container.serializeNBT());
        compound.put("temp", container.serializeNBT());
        return super.write(compound);
    }

    private void checkForRecipe() {
        if (!this.getWorld().isRemote) {
            if (currentRecipe == null || !currentRecipe.matches(input, container)) {
                currentRecipe = this.getWorld().getRecipeManager().getRecipes().stream()
                        .filter(recipe -> recipe.getType() == WorkshopRecipes.ALEMBIC).map(recipe -> (AlembicRecipe) recipe)
                        .filter(this::matches).findFirst().orElse(null);
            }
        }
    }

    private boolean matches(AlembicRecipe alembicRecipe) {
        return alembicRecipe.matches(input, container);
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public int getTemp() {
        if (this.world.getBlockState(this.pos.down()).equals(Blocks.LAVA.getDefaultState())) {
            temp = 1000;
        } else {
            temp = 50;
        }
        return temp;
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
    public void onFinish() {
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
                // checkForRecipe();
            }
        }
    }

    @Override
    public ActionResultType onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        getTemp();
        return super.onActivated(player, hand, hit);
    }

    @Override
    public boolean canIncrease() {
        return false;
    }

    @Override
    public AlembicTileEntity getSelf() {
        return this;
    }
}
