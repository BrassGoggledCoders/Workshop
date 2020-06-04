package xyz.brassgoggledcoders.workshop.content;


import com.hrznstudio.titanium.annotation.config.ConfigFile;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@ConfigFile(value = "general", type = ModConfig.Type.COMMON)
public class WorkshopConfig {
    public static class Common {
        public final ForgeConfigSpec.IntValue itemsRequiredPerScrapBag;

        public Common(ForgeConfigSpec.Builder builder) {
            itemsRequiredPerScrapBag = builder
                    .comment("Number of items the scrap bin needs to destroy to produce a scrap bag")
                    .defineInRange("scrapBin.itemsRequired", 100, 1, Integer.MAX_VALUE);
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}
