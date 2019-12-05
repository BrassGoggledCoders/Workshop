package xyz.brassgoggledcoders.workshop.recipes;

import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

public class SeasoningBarrelRecipe extends SerializableRecipe {

    public static GenericSerializer<SeasoningBarrelRecipe> SERIALIZER = new GenericSerializer<>(new ResourceLocation(MOD_ID, "seasoningbarrel"), SeasoningBarrelRecipe.class);
    public static List<SeasoningBarrelRecipe> RECIPES = new ArrayList<>();

    public ItemStack productIn;
    public ItemStack catalystOut;
    public FluidStack fluidInput;
    public FluidStack fluidOut;
    public int seasoningTime;

    public SeasoningBarrelRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public SeasoningBarrelRecipe(ResourceLocation resourceLocation, FluidStack fluidInput, ItemStack catalystOut, FluidStack output, ItemStack productIn, int seasoningTime) {
        super(resourceLocation);
        this.fluidInput = fluidInput;
        this.fluidOut = output;
        this.catalystOut = catalystOut;
        this.productIn = productIn;
        this.seasoningTime = seasoningTime;
        RECIPES.add(this);
    }

    public boolean matches(IItemHandler handler, PosFluidTank tank) {
        if (catalystOut == null || tank == null || fluidInput == null) return false;
        return handler.getStackInSlot(0).equals(catalystOut) && tank.drainForced(fluidInput, IFluidHandler.FluidAction.SIMULATE).getAmount() == fluidInput.getAmount();
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return catalystOut;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return SERIALIZER.getRecipeType();
    }
}
