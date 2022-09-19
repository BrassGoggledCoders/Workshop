package xyz.brassgoggledcoders.workshop.recipe.seasoningbarrel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.workshop.recipe.ingredient.fluid.FluidIngredient;

import javax.annotation.ParametersAreNonnullByDefault;

public class SeasoningBarrelRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<SeasoningBarrelRecipe> {
    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public SeasoningBarrelRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) throws JsonParseException {
        FluidIngredient fluidIngredient = FluidIngredient.EMPTY;
        Ingredient itemIngredient = Ingredient.EMPTY;

        if (pSerializedRecipe.has("fluidInput")) {
            fluidIngredient = FluidIngredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "fluidInput"));
        }
        if (pSerializedRecipe.has("itemInput")) {
            itemIngredient = Ingredient.fromJson(pSerializedRecipe.get("itemInput"));
        }

        if (fluidIngredient.isEmpty() && itemIngredient.isEmpty()) {
            throw new JsonParseException("Must include least one of 'fluidInput' or 'itemInput'");
        }

        FluidStack fluidStack = FluidStack.EMPTY;
        ItemStack itemStack = ItemStack.EMPTY;

        if (pSerializedRecipe.has("itemOutput")) {
            itemStack = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pSerializedRecipe, "itemOutput"), true);
        }
        if (pSerializedRecipe.has("fluidOutput")) {
            fluidStack = FluidStack.CODEC.decode(JsonOps.INSTANCE, pSerializedRecipe.get("fluidOutput"))
                    .getOrThrow(false, error -> {
                        throw new JsonParseException("Failed to parse 'fluidOutput' due to: " + error);
                    })
                    .getFirst();
        }

        if (itemStack.isEmpty() && fluidStack.isEmpty()) {
            throw new JsonParseException("Must include least one of 'fluidOutput' or 'itemOutput'");
        }

        int time = GsonHelper.getAsInt(pSerializedRecipe, "time", 20 * 30);

        return new SeasoningBarrelRecipe(
                pRecipeId,
                itemIngredient,
                fluidIngredient,
                itemStack,
                fluidStack,
                time
        );
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public SeasoningBarrelRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        return new SeasoningBarrelRecipe(
                pRecipeId,
                Ingredient.fromNetwork(pBuffer),
                FluidIngredient.fromNetwork(pBuffer),
                pBuffer.readItem(),
                pBuffer.readFluidStack(),
                pBuffer.readInt()
        );
    }

    @Override
    @ParametersAreNonnullByDefault
    public void toNetwork(FriendlyByteBuf pBuffer, SeasoningBarrelRecipe pRecipe) {
        pRecipe.getItemInput().toNetwork(pBuffer);
        pRecipe.getFluidInput().toNetwork(pBuffer);
        pBuffer.writeItem(pRecipe.getItemOutput());
        pBuffer.writeFluidStack(pRecipe.getFluidOutput());
        pBuffer.writeInt(pRecipe.getTime());
    }
}
