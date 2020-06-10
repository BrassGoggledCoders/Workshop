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

    @Override
    public void execute(PluginPhase phase) {
        if (phase == PluginPhase.CONSTRUCTION) {
            ResourceRegistry.getOrCreate("iron").addAll(ResourceType.DUST, WorkshopResourceType.FILM, WorkshopResourceType.PIPE);
            ResourceRegistry.getOrCreate("gold").addAll(ResourceType.DUST, WorkshopResourceType.FILM, WorkshopResourceType.PIPE);
            ResourceRegistry.getOrCreate("glass").setColor(WorkshopFluids.fromHex("80FFFFFF")).add(WorkshopResourceType.PIPE);
        }
    }
}