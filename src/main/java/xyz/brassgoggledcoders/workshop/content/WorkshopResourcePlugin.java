package xyz.brassgoggledcoders.workshop.content;

import com.hrznstudio.titanium.annotation.MaterialReference;
import com.hrznstudio.titanium.plugin.FeaturePluginInstance;
import com.hrznstudio.titanium.plugin.PluginPhase;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

//@FeaturePlugin(value = ResourceRegistry.PLUGIN_NAME, type = FeaturePlugin.FeaturePluginType.FEATURE)
public class WorkshopResourcePlugin implements FeaturePluginInstance {
    @MaterialReference(type = "pipe", material = "glass")
    public static Item GLASS_PIPE;
    @MaterialReference(type = "metal_block", material = "copper")
    public static Block COPPER_BLOCK;
    @MaterialReference(type = "metal_block", material = "silver")
    public static Block SILVER_BLOCK;

    @Override
    public void execute(PluginPhase phase) {
        if (phase == PluginPhase.CONSTRUCTION) {
            // ResourceRegistry.getOrCreate("iron").addAll(ResourceType.DUST, WorkshopResourceType.FILM, WorkshopResourceType.PIPE);
            //ResourceRegistry.getOrCreate("gold").addAll(ResourceType.DUST, WorkshopResourceType.FILM, WorkshopResourceType.PIPE);
            //ResourceRegistry.getOrCreate("glass").setColor(WorkshopFluids.fromHex("80FFFFFF")).add(WorkshopResourceType.PIPE);
            // ResourceRegistry.getOrCreate("copper").setColor(WorkshopFluids.fromHex("FFb87333")).addAll(ResourceType.NUGGET, ResourceType.METAL_BLOCK, ResourceType.INGOT);
            //ResourceRegistry.getOrCreate("silver").setColor(WorkshopFluids.fromHex("FFc0c0c0")).addAll(ResourceType.NUGGET, ResourceType.METAL_BLOCK, ResourceType.INGOT);
        }
    }
}