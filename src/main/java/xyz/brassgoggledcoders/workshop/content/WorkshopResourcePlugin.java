package xyz.brassgoggledcoders.workshop.content;

import com.hrznstudio.titanium.annotation.MaterialReference;
import com.hrznstudio.titanium.annotation.plugin.FeaturePlugin;
import com.hrznstudio.titanium.material.ResourceRegistry;
import com.hrznstudio.titanium.material.ResourceType;
import com.hrznstudio.titanium.plugin.FeaturePluginInstance;
import com.hrznstudio.titanium.plugin.PluginPhase;
import net.minecraft.item.Item;

@FeaturePlugin(value = ResourceRegistry.PLUGIN_NAME, type = FeaturePlugin.FeaturePluginType.FEATURE)
public class WorkshopResourcePlugin implements FeaturePluginInstance {
    @MaterialReference(type = "pipe", material = "glass")
    public static Item GLASS_PIPE;

    @MaterialReference(type = "nugget", material = "copper")
    public static Item COPPER_NUGGET;

    @MaterialReference(type = "nugget", material = "silver")
    public static Item SILVER_NUGGET;


    @Override
    public void execute(PluginPhase phase) {
        if (phase == PluginPhase.CONSTRUCTION) {
            ResourceRegistry.getOrCreate("iron").addAll(ResourceType.DUST, WorkshopResourceType.FILM, WorkshopResourceType.PIPE);
            ResourceRegistry.getOrCreate("gold").addAll(ResourceType.DUST, WorkshopResourceType.FILM, WorkshopResourceType.PIPE);
            ResourceRegistry.getOrCreate("copper").addAll(ResourceType.NUGGET, ResourceType.INGOT, ResourceType.METAL_BLOCK);
            ResourceRegistry.getOrCreate("silver").addAll(ResourceType.NUGGET, ResourceType.INGOT, ResourceType.METAL_BLOCK);
            ResourceRegistry.getOrCreate("glass").setColor(WorkshopFluids.fromHex("80FFFFFF")).add(WorkshopResourceType.PIPE);
        }
    }
}