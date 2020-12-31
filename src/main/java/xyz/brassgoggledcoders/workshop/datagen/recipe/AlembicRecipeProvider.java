package xyz.brassgoggledcoders.workshop.datagen.recipe;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.datagen.tags.WorkshopItemTagsProvider;
import xyz.brassgoggledcoders.workshop.recipe.AlembicRecipe;
import xyz.brassgoggledcoders.workshop.tileentity.AlembicTileEntity;
import xyz.brassgoggledcoders.workshop.util.RangedItemStack;

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
                .setOutput(new FluidStack(WorkshopFluids.DISTILLED_WATER.getFluid(), FluidAttributes.BUCKET_VOLUME))
                .setResidue(new RangedItemStack(Items.BUCKET, 1, 1),
                        new RangedItemStack(WorkshopItems.SALT.get(), 1, 2),
                        new RangedItemStack(WorkshopItems.SILT.get(), 0, 1))
                        //new RangedItemStack(WorkshopBlocks.CHALK_WRITING.getItem(), 0, 1))
                .setTime(500)
                .build());
        recipes.add(new Builder("adhesive_oil")
                .setInputs(Ingredient.fromStacks(FluidUtil.getFilledBucket(new FluidStack(WorkshopFluids.RESIN.getFluid(), FluidAttributes.BUCKET_VOLUME))),
                        Ingredient.fromItems(Items.KELP),
                        Ingredient.fromItems(WorkshopItems.SALT.get()))
                .setOutput(new FluidStack(WorkshopFluids.ADHESIVE_OILS.getFluid(), FluidAttributes.BUCKET_VOLUME))
                .setResidue(new RangedItemStack(Items.BUCKET, 1, 1),
                        new RangedItemStack(WorkshopItems.ASH.get(), 1, 3))
                .setTime(8 * 20)
                .build());
        recipes.add(new Builder("adhesive_oil_alt")
                .setInputs(Ingredient.fromStacks(FluidUtil.getFilledBucket(new FluidStack(WorkshopFluids.RESIN.getFluid(), FluidAttributes.BUCKET_VOLUME))),
                        Ingredient.fromItems(Items.SEAGRASS),
                        Ingredient.fromItems(WorkshopItems.SALT.get()))
                .setOutput(new FluidStack(WorkshopFluids.ADHESIVE_OILS.getFluid(), FluidAttributes.BUCKET_VOLUME))
                .setResidue(new RangedItemStack(Items.BUCKET, 1, 1),
                        new RangedItemStack(WorkshopItems.ASH.get(), 1, 3))
                .setTime(8 * 20)
                .build());
        recipes.add(new Builder("tannin_seeds")
                .setInputs(Ingredient.fromTag(Tags.Items.SEEDS), Ingredient.fromItems(WorkshopItems.TEA_LEAVES.get()))
                .setResidue(new RangedItemStack(WorkshopItems.ASH.get(), 0, 2),
                        new RangedItemStack(WorkshopItems.SILT.get(), 0, 1),
                        new RangedItemStack(WorkshopItems.TANNIN.get(), 6, 6))
                .setTime(8 * 20)
                .build());
        recipes.add(new Builder("tannin_roots")
                .setInputs(Ingredient.fromTag(WorkshopItemTagsProvider.ROOTS), Ingredient.fromItems(WorkshopItems.TEA_LEAVES.get()))
                .setResidue(new RangedItemStack(WorkshopItems.ASH.get(), 0, 2),
                        new RangedItemStack(WorkshopItems.SILT.get(), 0, 1),
                        new RangedItemStack(WorkshopItems.TANNIN.get(), 6, 6))
                .setTime(8 * 20)
                .build());
        recipes.add(new Builder("rosin")
                .setInputs(Ingredient.fromItems(Items.SPRUCE_LOG), Ingredient.fromTag(Tags.Items.SLIMEBALLS))
                .setOutput(new FluidStack(WorkshopFluids.ADHESIVE_OILS.getFluid(), WorkshopFluids.BOTTLE_VOLUME))
                .setResidue(new RangedItemStack(WorkshopItems.ROSIN.get(), 4, 4),
                        new RangedItemStack(Items.SLIME_BALL, 0, 2))
                .setTime(10 * 20)
                .build());
        recipes.forEach(recipe -> serializables.put(recipe, recipe));
    }

    protected static class Builder {
        private final ResourceLocation name;
        private Ingredient[] input = new Ingredient[0];
        private FluidStack output = FluidStack.EMPTY;
        private RangedItemStack[] residue;
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

        public Builder setResidue(RangedItemStack... out) {
            this.residue = out;
            return this;
        }

        public Builder setTime(int time) {
            this.processingTime = time;
            return this;
        }

        public void validate() {
            if (this.input.length > AlembicTileEntity.inputSize) {
                throw new IllegalArgumentException(String.format("Input list size %d is too large for alembic, must be smaller than %d", this.input.length, AlembicTileEntity.inputSize));
            }
            /*if(this.residue != null && this.residue.length > AlembicTileEntity.residueSize) {
                throw new IllegalArgumentException(String.format("Residue list size %d is too large for alembic, must be smaller than %d", this.residue.length, AlembicTileEntity.residueSize));
            }*/
            if (this.processingTime <= 0) {
                throw new IllegalArgumentException("Processing time must be more than zero");
            }
        }

        public AlembicRecipe build() {
            validate();
            return new AlembicRecipe(name, input, output, residue, processingTime);
        }
    }
}
