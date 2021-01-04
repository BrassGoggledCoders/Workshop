package workshop.recipe;

import com.hrznstudio.titanium.material.ResourceMaterial;
import com.hrznstudio.titanium.material.ResourceRegistry;
import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.recipe.MortarRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MortarRecipeProvider extends TitaniumSerializableProvider {

    public static final List<MortarRecipe> recipes = new ArrayList<>();

    public MortarRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn, Workshop.MOD_ID);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> serializables) {
        recipes.add(new Builder("bonemeal")
                .setItemsIn(Ingredient.fromItems(Items.BONE))
                .setItemOut(new ItemStack(Items.BONE_MEAL, 6))
                .build());
        recipes.add(new Builder("blaze_rods")
                .setItemsIn(Ingredient.fromItems(Items.BLAZE_ROD))
                .setItemOut(new ItemStack(Items.BLAZE_POWDER, 3))
                .build());
        recipes.add(new Builder("cobble_to_gravel")
                .setItemsIn(Ingredient.fromItems(Blocks.COBBLESTONE))
                .setItemOut(new ItemStack(Blocks.GRAVEL))
                .build());
        recipes.add(new Builder("gravel_to_sand")
                .setItemsIn(Ingredient.fromItems(Blocks.GRAVEL))
                .setItemOut(new ItemStack(Blocks.SAND))
                .build());
        recipes.add(new Builder("lye")
                .setItemsIn(Ingredient.fromItems(WorkshopItems.ASH.get()))
                .setFluidIn(new FluidStack(WorkshopFluids.DISTILLED_WATER.getFluid(), WorkshopFluids.BOTTLE_VOLUME))
                .setItemOut(new ItemStack(WorkshopItems.LYE.get()))
                .build());
        recipes.add(new Builder("soap")
                .setItemsIn(Ingredient.fromItems(WorkshopItems.LYE.get()), Ingredient.fromItems(WorkshopItems.TALLOW.get()))
                .setItemOut(new ItemStack(WorkshopItems.SOAP.get()))
                .build());
        //TODO Doesn't work
        for (ResourceMaterial material : ResourceRegistry.getMaterials()) {
            Map<String, ForgeRegistryEntry> generated = material.getGenerated();
            if (generated.containsKey("dusts")) {
                recipes.add(new Builder(material.getMaterialType() + "_dust")
                        .setItemsIn(Ingredient.fromTag(ItemTags.getCollection().get(new ResourceLocation("forge", "ingots/" + material.getMaterialType()))))
                        //TODO Eugh
                        .setItemOut(new ItemStack(((ForgeRegistryEntry<Item>) generated.get("dusts")).delegate.get()))
                        .build());
            }
        }
        recipes.forEach(recipe -> serializables.put(recipe, recipe));
    }

    protected static class Builder {
        private final ResourceLocation name;
        private Ingredient[] itemIn;
        private ItemStack itemOut = ItemStack.EMPTY;
        private FluidStack fluidIn = FluidStack.EMPTY;
        private int time = 10;

        public Builder(String name) {
            this.name = new ResourceLocation(Workshop.MOD_ID, name);
        }

        public Builder setItemsIn(Ingredient... in) {
            this.itemIn = in;
            return this;
        }

        public Builder setItemOut(ItemStack out) {
            this.itemOut = out;
            return this;
        }

        public Builder setFluidIn(FluidStack in) {
            this.fluidIn = in;
            return this;
        }

        public Builder setTime(int time) {
            this.time = time;
            return this;
        }

        public void validate() {
        }

        public MortarRecipe build() {
            validate();
            return new MortarRecipe(name, itemIn, fluidIn, itemOut, time);
        }
    }
}
