package xyz.brassgoggledcoders.workshop.datagen.langauge;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;

public class WorkshopGBLanguageProvider extends LanguageProvider {
    public WorkshopGBLanguageProvider(DataGenerator gen) {
        super(gen, Workshop.MOD_ID, "en_gb");
    }

    @Override
    protected void addTranslations() {
        //region Items
        this.addItem(WorkshopItems.CARAMEL_APPLE, "Toffee Apple");
        //endregion
    }
}
