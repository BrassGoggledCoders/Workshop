package xyz.brassgoggledcoders.workshop.component.machine;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.component.sideness.IFacingComponent;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class RecipeMachineComponent<T extends IRecipeMachineHarness<T, U>, U extends IRecipe<IInventory>> extends MachineComponent<T> {
    private final ProgressBarComponent<T> primaryBar;

    private int timeSinceLastRecipeCheck = 50;
    private boolean recheck = false;
    private U currentRecipe;

    public RecipeMachineComponent(T componentHarness, Supplier<BlockPos> posSupplier, ProgressBarComponent<T> primaryBar) {
        super(componentHarness, posSupplier);
        this.primaryBar = primaryBar;
        if (primaryBar != null) {
            this.primaryBar.setCanIncrease(value -> currentRecipe != null);
            this.addProgressBar(primaryBar);
        }
    }

    public ProgressBarComponent<T> getPrimaryBar() {
        return primaryBar;
    }

    @Override
    public void tick() {
        World world = this.componentHarness.getComponentWorld();
        BlockPos pos = this.posSupplier.get();
        if (!world.isRemote()) {
            if (this.multiProgressBarHandler != null) {
                this.multiProgressBarHandler.update();
            }

            if (world.getGameTime() % (long) this.getFacingHandlerWorkTime() == 0L) {
                if (this.multiInventoryComponent != null) {
                    for (InventoryComponent<T> inventoryHandler : this.multiInventoryComponent.getInventoryHandlers()) {
                        if (inventoryHandler instanceof IFacingComponent && ((IFacingComponent) inventoryHandler)
                                .work(world, pos, this.getFacingDirection(), this.getFacingHandlerWorkAmount())) {
                            recheck = true;
                            break;
                        }
                    }
                }

                if (this.multiTankComponent != null) {
                    for (FluidTankComponent<T> tankComponent : this.multiTankComponent.getTanks()) {
                        if (tankComponent instanceof IFacingComponent && ((IFacingComponent) tankComponent)
                                .work(world, pos, this.getFacingDirection(), this.getFacingHandlerWorkAmount())) {
                            recheck = true;
                            break;
                        }
                    }
                }
            }

            if (currentRecipe == null) {
                handleNoRecipe(recheck);
            } else {
                handleRecipe();
            }
            recheck = false;
        }
    }

    public void forceRecipeRecheck() {
        recheck = true;
    }

    protected void handleNoRecipe(boolean didWork) {
        if (timeSinceLastRecipeCheck-- <= 0 || didWork) {
            timeSinceLastRecipeCheck = 50;
            if (componentHarness.hasInputs()) {
                currentRecipe = componentHarness.getComponentWorld().getRecipeManager()
                        .getRecipes()
                        .stream()
                        .filter(componentHarness::checkRecipe)
                        .map(componentHarness::castRecipe)
                        .filter(componentHarness::matchesInputs)
                        .findFirst()
                        .orElse(null);
                primaryBar.setProgress(0);
                if (currentRecipe != null) {
                    primaryBar.setMaxProgress(componentHarness.getProcessingTime(currentRecipe));
                }
            }
        }
    }

    protected void handleRecipe() {
        if (componentHarness.matchesInputs(currentRecipe)) {
            if (primaryBar.getProgress() >= primaryBar.getMaxProgress() - 1) {
                componentHarness.handleComplete(currentRecipe);
                primaryBar.setProgress(0);
            }
        } else {
            currentRecipe = null;
            primaryBar.setProgress(0);
        }
        componentHarness.markComponentDirty();
    }


    public int getFacingHandlerWorkTime() {
        return 10;
    }

    public int getFacingHandlerWorkAmount() {
        return 4;
    }

    public U getCurrentRecipe() {
        return currentRecipe;
    }
}
