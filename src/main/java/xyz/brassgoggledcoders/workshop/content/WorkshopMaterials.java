package xyz.brassgoggledcoders.workshop.content;

import com.hrznstudio.titanium.annotation.MaterialReference;
import com.hrznstudio.titanium.material.ResourceRegistry;
import com.hrznstudio.titanium.material.ResourceType;
import net.minecraft.item.Item;

public class WorkshopMaterials {

    @MaterialReference(type = "pipe", material = "glass")
    public static Item GLASS_PIPE;

    public static void init() {
        ResourceRegistry.getOrCreate("iron").addAll(ResourceType.DUST, WorkshopResourceType.FILM, WorkshopResourceType.PIPE);
        ResourceRegistry.getOrCreate("gold").addAll(ResourceType.DUST, WorkshopResourceType.FILM, WorkshopResourceType.PIPE);
        ResourceRegistry.getOrCreate("glass").setColor(WorkshopFluids.fromHex("80FFFFFF")).add(WorkshopResourceType.PIPE);
    }

}
