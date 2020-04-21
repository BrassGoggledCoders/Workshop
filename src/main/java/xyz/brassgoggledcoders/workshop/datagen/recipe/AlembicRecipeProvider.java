package xyz.brassgoggledcoders.workshop.datagen.recipe;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.recipe.AlembicRecipe;
import xyz.brassgoggledcoders.workshop.tileentity.AlembicTileEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlembicRecipeProvider extends TitaniumSerializableProvider {

    public static final List<AlembicRecipe> recipes = new ArrayList<>();

    public AlembicRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn, Workshop.MOD_ID);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> serializables) {
        recipes.add(new Builder("distilled_water")
                .setInputs(Ingredient.fromItems(Items.WATER_BUCKET))
                .setOutput(new FluidStack(WorkshopFluids.DISTILLED_WATER.getFluid(), WorkshopFluids.BOTTLE_VOLUME))
                .setResidue(new ItemStack(Items.BUCKET), new ItemStack(WorkshopItems.SALT.get()))
                .setTime(500)
                .build());
        recipes.add(new Builder("adhesive_oil")
                .setInputs(Ingredient.fromStacks(FluidUtil.getFilledBucket(new FluidStack(WorkshopFluids.RESIN.getFluid(), FluidAttributes.BUCKET_VOLUME))),
                        Ingredient.fromItems(Items.KELP),
                        Ingredient.fromItems(WorkshopItems.SALT.get()))
                .setOutput(new FluidStack(WorkshopFluids.ADHESIVE_OILS.getFluid(), FluidAttributes.BUCKET_VOLUME))
                .setResidue(new ItemStack(Items.BUCKET), new ItemStack(WorkshopItems.ASH.get(), 4))
                .setTime(8 * 20)
                .build());
        recipes.add(new Builder("adhesive_oil_alt")
                .setInputs(Ingredient.fromStacks(FluidUtil.getFilledBucket(new FluidStack(WorkshopFluids.RESIN.getFluid(), FluidAttributes.BUCKET_VOLUME))),
                        Ingredient.fromItems(Items.SEAGRASS),
                        Ingredient.fromItems(WorkshopItems.SALT.get()))
                .setOutput(new FluidStack(WorkshopFluids.ADHESIVE_OILS.getFluid(), FluidAttributes.BUCKET_VOLUME))
                .setResidue(new ItemStack(Items.BUCKET), new ItemStack(WorkshopItems.ASH.get(), 4))
                .setTime(8 * 20)
                .build());
        recipes.add(new Builder("tannin")
                .setInputs(Ingredient.fromItems(WorkshopItems.MEDICINAL_ROOT.get()), Ingredient.fromItems(WorkshopItems.TEA_LEAVES.get()))
                .setOutput(FluidStack.EMPTY) //TODO
                .setResidue(new ItemStack(WorkshopItems.TANNIN.get()), new ItemStack(WorkshopItems.ASH.get(), 2))
                .setTime(8 * 20)
                .build());
        recipes.forEach(recipe -> serializables.put(recipe, recipe));
    }

    protected static class Builder {
        private ResourceLocation name;
        private Ingredient[] input;
        private FluidStack output;
        private ItemStack[] residue;
        private int processingTime;

        public Builder(String name) {
            this.name = new ResourceLocation(Workshop.MOD_ID, name);
        }

        public Builder setInputs(Ingredient... in) {
            this.input = in;
            return this;
        }

        public Builder setOutput(FluidStack out) {
            this.output = out;
            return this;
        }

        public Builder setResidue(ItemStack... out) {
            this.residue = out;
            return this;
        }

        public Builder setTime(int time) {
            this.processingTime = time;
            return this;
        }

        public void validate() {
            if(this.input.length > AlembicTileEntity.inputSize) {
                throw new IllegalArgumentException(String.format("Input list size %d is too large for alembic, must be smaller than %d", this.input.length, AlembicTileEntity.inputSize));
            }
            if(this.residue != null && this.residue.length > AlembicTileEntity.residueSize) {
                throw new IllegalArgumentException(String.format("Residue list size %d is too large for alembic, must be smaller than %d", this.residue.length, AlembicTileEntity.residueSize));
            }
            if(this.processingTime <= 0) {
                throw new IllegalArgumentException("Processing time must be more than zero");
            }
        }

        public AlembicRecipe build() {
            validate();
            return new AlembicRecipe(name, input, output, residue, processingTime);
        }
    }
}