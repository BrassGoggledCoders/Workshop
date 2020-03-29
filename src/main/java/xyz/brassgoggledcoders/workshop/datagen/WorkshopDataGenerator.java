package xyz.brassgoggledcoders.workshop.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import xyz.brassgoggledcoders.workshop.datagen.langauge.WorkshopGBLanguageProvider;
import xyz.brassgoggledcoders.workshop.datagen.langauge.WorkshopUSLanguageProvider;

public class WorkshopDataGenerator {

    public static void gatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();

        if (event.includeClient()) {
            dataGenerator.addProvider(new WorkshopUSLanguageProvider(dataGenerator));
            dataGenerator.addProvider(new WorkshopGBLanguageProvider(dataGenerator));
        }
    }
}
