package xyz.brassgoggledcoders.workshop.compat.patchouli;

import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.client.book.ClientBookRegistry;
import xyz.brassgoggledcoders.workshop.Workshop;

public class PageTypes {
    public static void registerPageTypes() {
        ClientBookRegistry.INSTANCE.pageTypes.put(new ResourceLocation(Workshop.MOD_ID, "tag"), TagPage.class);
    }
}
