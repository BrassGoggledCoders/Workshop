package workshop.language;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;

public class WorkshopGBLanguageProvider extends WorkshopUSLanguageProvider {
    public WorkshopGBLanguageProvider(DataGenerator gen) {
        super(gen, "en_gb");
    }

    @Override
    protected void addTranslations() {
        //region Items
        this.addItem(WorkshopItems.CARAMEL_APPLE, "Toffee Apple");
        //endregion
        this.addBlock(WorkshopBlocks.CONCRETES.get(DyeColor.GRAY.getId()), "Grey Rebarred Concrete");
        this.addBlock(WorkshopBlocks.CONCRETES.get(DyeColor.LIGHT_GRAY.getId()), "Light Grey Rebarred Concrete");
        this.addAdvancement("drunk", "Get Hammered", "*hic*");
    }
}
