package xyz.brassgoggledcoders.workshop.blocks.sinteringfurnace;


import com.hrznstudio.titanium.util.RecipeUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import xyz.brassgoggledcoders.workshop.recipes.SinteringFurnaceRecipe;

import javax.annotation.Nullable;

import static xyz.brassgoggledcoders.workshop.registries.TileEntities.SINTERING_FURNACE_TILE;

public class SinteringFurnaceTile extends TileEntity {

    public SinteringFurnaceTile() {
        super(SINTERING_FURNACE_TILE.get());
    }





    @Nullable
    public SinteringFurnaceRecipe findRecipe(ItemStack powder, ItemStack targetMaterial) {
        return RecipeUtil.getRecipes(world, SinteringFurnaceRecipe.SERIALIZER.getRecipeType()).stream().filter(sinteringFurnaceRecipe -> sinteringFurnaceRecipe.matches(powder,targetMaterial)).findFirst().orElse(null);
    }
}
