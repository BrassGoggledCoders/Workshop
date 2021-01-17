package workshop;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourcePackType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import workshop.language.WorkshopGBLanguageProvider;
import workshop.language.WorkshopUSLanguageProvider;
import workshop.loot.WorkshopLootTableProvider;
import workshop.models.WorkshopBlockstateProvider;
import workshop.models.WorkshopItemModelProvider;
import workshop.recipe.*;
import workshop.tags.WorkshopBlockTagsProvider;
import workshop.tags.WorkshopFluidTagsProvider;
import workshop.tags.WorkshopItemTagsProvider;
import xyz.brassgoggledcoders.workshop.Workshop;

@Mod.EventBusSubscriber(modid = Workshop.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unused")
public class WorkshopDataGenerator {
    public static final ExistingFileHelper.ResourceType RECIPE = new ExistingFileHelper.ResourceType(ResourcePackType.SERVER_DATA, ".json", "recipes");

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        final DataGenerator dataGenerator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            dataGenerator.addProvider(new WorkshopUSLanguageProvider(dataGenerator));
            dataGenerator.addProvider(new WorkshopGBLanguageProvider(dataGenerator));
            dataGenerator.addProvider(new WorkshopBlockstateProvider(dataGenerator, existingFileHelper));
            dataGenerator.addProvider(new WorkshopItemModelProvider(dataGenerator, existingFileHelper));
        }

        if (event.includeServer()) {
            dataGenerator.addProvider(new WorkshopLootTableProvider(dataGenerator));

            dataGenerator.addProvider(new WorkshopRecipeProvider(dataGenerator, existingFileHelper));
            dataGenerator.addProvider(new SeasoningBarrelRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new AlembicRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new PressRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new SinteringFurnaceRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new CollectorRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new SpinningWheelRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new MoltenChamberRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new MortarRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new DryingBasinRecipeProvider(dataGenerator));

            WorkshopBlockTagsProvider blockTagsProvider = new WorkshopBlockTagsProvider(dataGenerator, existingFileHelper);
            dataGenerator.addProvider(blockTagsProvider);
            dataGenerator.addProvider(new WorkshopItemTagsProvider(dataGenerator, blockTagsProvider, existingFileHelper));
            dataGenerator.addProvider(new WorkshopFluidTagsProvider(dataGenerator, existingFileHelper));

            dataGenerator.addProvider(new WorkshopBookProvider(dataGenerator, existingFileHelper));
        }
    }
}
