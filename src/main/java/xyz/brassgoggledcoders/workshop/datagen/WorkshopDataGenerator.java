package xyz.brassgoggledcoders.workshop.datagen;

import com.google.gson.*;
import com.hrznstudio.titanium.recipe.serializer.JSONSerializableDataHandler;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.ILootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.datagen.langauge.WorkshopGBLanguageProvider;
import xyz.brassgoggledcoders.workshop.datagen.langauge.WorkshopUSLanguageProvider;
import xyz.brassgoggledcoders.workshop.datagen.loot.WorkshopLootTableProvider;
import xyz.brassgoggledcoders.workshop.datagen.models.WorkshopBlockstateProvider;
import xyz.brassgoggledcoders.workshop.datagen.models.WorkshopItemModelProvider;
import xyz.brassgoggledcoders.workshop.datagen.recipe.*;

import java.util.Iterator;

@Mod.EventBusSubscriber(modid = Workshop.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WorkshopDataGenerator {

    static {
        JSONSerializableDataHandler.map(TileEntityType.class, (type) -> new JsonPrimitive(type.getRegistryName().toString()),
                (element) -> ForgeRegistries.TILE_ENTITIES.getValue(new ResourceLocation(element.getAsString())));
        JSONSerializableDataHandler.map(ItemStack[].class, (stacks) -> {
            JsonArray array = new JsonArray();
            for (ItemStack stack : stacks) {
                array.add(JSONSerializableDataHandler.writeItemStack(stack));
            }
            return array;
        }, (element) -> {
            JsonArray array = element.getAsJsonArray();
            ItemStack[] stacks = new ItemStack[array.size()];
            for(int i = 0; i < array.size(); i++) {
                stacks[i] = JSONSerializableDataHandler.readItemStack(array.get(i).getAsJsonObject());
            }
            return stacks;
        });
        JSONSerializableDataHandler.map(LootTable.class,
                LootTableManager::toJson, json ->
                        //TODO!
                        ForgeHooks.loadLootTable(LootTableManager.GSON_INSTANCE, new ResourceLocation(Workshop.MOD_ID, "dummy"), (JsonObject) json, true, null));
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        final DataGenerator dataGenerator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            dataGenerator.addProvider(new WorkshopUSLanguageProvider(dataGenerator));
            dataGenerator.addProvider(new WorkshopGBLanguageProvider(dataGenerator));
            //dataGenerator.addProvider(new WorkshopBlockModelProvider(dataGenerator, existingFileHelper));
            dataGenerator.addProvider(new WorkshopItemModelProvider(dataGenerator, existingFileHelper));
            dataGenerator.addProvider(new WorkshopBlockstateProvider(dataGenerator, existingFileHelper));
        }

        if (event.includeServer()) {
            dataGenerator.addProvider(new WorkshopLootTableProvider(dataGenerator));

            dataGenerator.addProvider(new WorkshopRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new SeasoningBarrelRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new AlembicRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new PressRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new SinteringFurnaceRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new CollectorRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new SpinningWheelRecipeProvider(dataGenerator));

            dataGenerator.addProvider(new WorkshopItemTagsProvider(dataGenerator));
        }
    }
}
