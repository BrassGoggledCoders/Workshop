package xyz.brassgoggledcoders.workshop.blocks.press;


import com.hrznstudio.titanium.util.RecipeUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.workshop.recipes.PressRecipes;
import xyz.brassgoggledcoders.workshop.util.WorkTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PressTile extends TileEntity implements ITickableTileEntity {

    private static final int CAPACITY = 4 * FluidAttributes.BUCKET_VOLUME;
    protected FluidTank tank = new FluidTank(CAPACITY);
    private int internalFluid;
    private int pressTime = 120;
    private PressRecipes currentRecipe;

    public PressTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {

        onStart();

    }

    private void checkForRecipe(ItemStack input){
        if (!world.isRemote) {
            if (currentRecipe != null && currentRecipe.matches(input)) {
                return;
            }
            currentRecipe = RecipeUtil.getRecipes(world, PressRecipes.SERIALIZER.getRecipeType()).stream().filter(alembicRecipe -> alembicRecipe.matches(input)).findFirst().orElse(null);
        }
    }

    private void onStart(){
        if(getPressTime() != 0){
            handler.ifPresent(h -> {
                ItemStack stack = h.getStackInSlot(0);
                checkForRecipe(stack);
                if(stack.isEmpty()){
                    pressTime = 120;
                }
                else if(!stack.isEmpty()) {
                    if (stack == currentRecipe.input) {
                        if (getInternalFluid() < 4000 && getPotentialFluid() >= 4000) {
                            pressTime = -1;
                            onFinish();
                        }
                    }
                }
            });
        }
    }

    private void onFinish(){
        if(getPressTime() == 0){
            handler.ifPresent(h -> {
                ItemStack stack = h.getStackInSlot(0);
                if(stack == currentRecipe.input){
                    int fluidOutAmount = currentRecipe.fluidOutAmount;
                    stack.shrink(1);
                    tank.fill(currentRecipe.output, IFluidHandler.FluidAction.EXECUTE);
                    if(getInternalFluid() > 4000){
                        if(getPotentialFluid() >= 4000) {
                            internalFluid = +fluidOutAmount;
                        }
                    }
                }

            });
        }
    }

    //Inventory Handler

    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> tank);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                return handler.cast();
        }
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return fluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }



    private IItemHandler createHandler() {
        return new ItemStackHandler(1) {

            @Override
            protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
                return 8;
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem().isIn(WorkTags.Items.PRESS) ;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                Item item = stack.getItem();
                if (!stack.getItem().isIn(WorkTags.Items.PRESS)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }

            @Override
            @Nonnull
            public ItemStack extractItem(int slot, int amount, boolean simulate)
            {
                return extractItem(slot, amount, simulate);
            }

        };

    }

    private int getPotentialFluid(){
        return currentRecipe.fluidOutAmount + internalFluid;
    }

    private int getInternalFluid(){
        return internalFluid;
    }

    private int getPressTime(){
        return pressTime;
    }


    @Override
    public void read(CompoundNBT tag)
    {
        super.read(tag);
        tank.readFromNBT(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        tag = super.write(tag);
        tank.writeToNBT(tag);
        return tag;
    }

}
