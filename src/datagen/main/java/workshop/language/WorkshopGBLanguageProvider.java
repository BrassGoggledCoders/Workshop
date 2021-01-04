package workshop.language;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.data.LanguageProvider;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
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
        this.addBlock(WorkshopBlocks.CONCRETES.get(DyeColor.GRAY.getId()), "Grey Rebarred Concrete");
        this.addBlock(WorkshopBlocks.CONCRETES.get(DyeColor.LIGHT_GRAY.getId()), "Light Grey Rebarred Concrete");
    }
}
