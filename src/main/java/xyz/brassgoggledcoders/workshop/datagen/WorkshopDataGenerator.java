package xyz.brassgoggledcoders.workshop.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hrznstudio.titanium.recipe.serializer.JSONSerializableDataHandler;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.datagen.langauge.WorkshopGBLanguageProvider;
import xyz.brassgoggledcoders.workshop.datagen.langauge.WorkshopUSLanguageProvider;
import xyz.brassgoggledcoders.workshop.datagen.loot.WorkshopLootTableProvider;
import xyz.brassgoggledcoders.workshop.datagen.models.WorkshopBlockModelProvider;
import xyz.brassgoggledcoders.workshop.datagen.models.WorkshopBlockstateProvider;
import xyz.brassgoggledcoders.workshop.datagen.models.WorkshopItemModelProvider;
import xyz.brassgoggledcoders.workshop.datagen.recipe.AlembicRecipeProvider;
import xyz.brassgoggledcoders.workshop.datagen.recipe.PressRecipeProvider;
import xyz.brassgoggledcoders.workshop.datagen.recipe.SeasoningBarrelRecipeProvider;
import xyz.brassgoggledcoders.workshop.datagen.recipe.WorkshopRecipeProvider;

import java.util.Iterator;

@Mod.EventBusSubscriber(modid = Workshop.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WorkshopDataGenerator {

    //TODO Move to Titanium?
    static {
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
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        final DataGenerator dataGenerator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            dataGenerator.addProvider(new WorkshopUSLanguageProvider(dataGenerator));
            dataGenerator.addProvider(new WorkshopGBLanguageProvider(dataGenerator));
            dataGenerator.addProvider(new WorkshopItemModelProvider(dataGenerator, existingFileHelper));
            dataGenerator.addProvider(new WorkshopBlockModelProvider(dataGenerator, existingFileHelper));
            dataGenerator.addProvider(new WorkshopBlockstateProvider(dataGenerator, existingFileHelper));
        }

        if (event.includeServer()) {
            dataGenerator.addProvider(new WorkshopLootTableProvider(dataGenerator));

            dataGenerator.addProvider(new WorkshopRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new SeasoningBarrelRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new AlembicRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new PressRecipeProvider(dataGenerator));
        }
    }
}
