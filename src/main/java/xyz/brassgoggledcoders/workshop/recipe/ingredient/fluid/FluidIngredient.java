package xyz.brassgoggledcoders.workshop.recipe.ingredient.fluid;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.workshop.util.JSONHelper;

import java.util.function.Predicate;

public class FluidIngredient implements Predicate<FluidStack> {
    public static final FluidIngredient EMPTY = new FluidIngredient(null, 0, null);

    private final Either<Fluid, TagKey<Fluid>> fluid;
    private final int amount;
    private final CompoundTag nbt;

    public FluidIngredient(Either<Fluid, TagKey<Fluid>> fluid, int amount, @Nullable CompoundTag nbt) {
        this.fluid = fluid;
        this.amount = amount;
        this.nbt = nbt;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean test(FluidStack fluidStack) {
        if (fluidStack.isEmpty() && this == EMPTY) {
            return true;
        } else {
            boolean fluidMatches = fluid.map(
                    fluidStack.getFluid()::equals,
                    fluidStack.getFluid()::is
            );
            boolean nbtMatches = nbt == null || nbt.equals(fluidStack.getTag());
            return fluidMatches && fluidStack.getAmount() >= amount && nbtMatches;
        }
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public static FluidIngredient fromJson(JsonObject jsonObject) throws JsonParseException {
        Either<Fluid, TagKey<Fluid>> fluid = JSONHelper.getValueOrTag(jsonObject, "fluid", ForgeRegistries.FLUIDS);
        int amount = GsonHelper.getAsInt(jsonObject, "amount", FluidAttributes.BUCKET_VOLUME);
        if (amount <= 0) {
            throw new JsonParseException("'amount' must be greater than 0");
        }
        CompoundTag nbt = null;
        if (jsonObject.has("nbt")) {
            nbt = CraftingHelper.getNBT(jsonObject.get("nbt"));
        }
        return new FluidIngredient(fluid, amount, nbt);
    }
}
