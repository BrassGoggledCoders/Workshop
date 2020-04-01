package xyz.brassgoggledcoders.workshop.content;

import com.hrznstudio.titanium.annotation.MaterialReference;
import com.hrznstudio.titanium.material.ResourceRegistry;
import com.hrznstudio.titanium.material.ResourceType;
import net.minecraft.item.Item;

public class WorkshopMaterials {

    @MaterialReference(type = "dust", material = "iron")
    public static Item IRON_DUST;
    @MaterialReference(type = "dust", material = "gold")
    public static Item GOLD_DUST;

    @MaterialReference(type = "film", material = "iron")
    public static Item IRON_FILM;
    @MaterialReference(type = "film", material = "gold")
    public static Item GOLD_FILM;

    @MaterialReference(type = "pipe", material = "iron")
    public static Item IRON_PIPE;
    @MaterialReference(type = "pipe", material = "gold")
    public static Item GOLD_PIPE;

    public static void init() {
        ResourceRegistry.getOrCreate("iron").addAll(ResourceType.DUST, WorkshopResourceType.FILM, WorkshopResourceType.PIPE);
        ResourceRegistry.getOrCreate("gold").addAll(ResourceType.DUST, WorkshopResourceType.FILM, WorkshopResourceType.PIPE);
    }

}
