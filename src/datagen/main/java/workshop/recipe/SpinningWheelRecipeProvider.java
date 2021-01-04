package workshop.recipe;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.recipe.SpinningWheelRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpinningWheelRecipeProvider extends TitaniumSerializableProvider {

    public static final List<SpinningWheelRecipe> recipes = new ArrayList<>();

    public SpinningWheelRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn, Workshop.MOD_ID);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> serializables) {
        recipes.add(new Builder("wool_to_string")
                .setInputs(Ingredient.fromTag(ItemTags.WOOL))
                .setOutput(new ItemStack(Items.STRING, 2))
                .setTime(500)
                .build());
        recipes.add(new Builder("leather_cordage")
                .setInputs(Ingredient.fromItems(Items.LEATHER), Ingredient.fromItems(Items.TALL_GRASS), Ingredient.fromItems(WorkshopItems.TANNIN.get()))
                .setOutput(new ItemStack(WorkshopItems.LEATHER_CORDAGE.get(), 2))
                .setTime(300)
                .build());
        recipes.forEach(recipe -> serializables.put(recipe, recipe));
    }

    protected static class Builder {
        private final ResourceLocation name;
        private Ingredient[] input;
        private ItemStack output;
        private int processingTime;

        public Builder(String name) {
            this.name = new ResourceLocation(Workshop.MOD_ID, name);
        }

        public Builder setInputs(Ingredient... in) {
            this.input = in;
            return this;
        }

        public Builder setOutput(ItemStack out) {
            this.output = out;
            return this;
        }

        public Builder setTime(int time) {
            this.processingTime = time;
            return this;
        }

        public void validate() {
            //TODO
        }

        public SpinningWheelRecipe build() {
            validate();
            return new SpinningWheelRecipe(name, input, output, processingTime);
        }
    }
}
