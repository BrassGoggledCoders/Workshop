package xyz.brassgoggledcoders.workshop.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.hrznstudio.titanium.recipe.serializer.JSONSerializableDataHandler;
import net.minecraft.data.DataGenerator;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.datagen.language.WorkshopGBLanguageProvider;
import xyz.brassgoggledcoders.workshop.datagen.language.WorkshopUSLanguageProvider;
import xyz.brassgoggledcoders.workshop.datagen.loot.WorkshopLootTableProvider;
import xyz.brassgoggledcoders.workshop.datagen.models.WorkshopBlockstateProvider;
import xyz.brassgoggledcoders.workshop.datagen.models.WorkshopItemModelProvider;
import xyz.brassgoggledcoders.workshop.datagen.models.WorkshopResourceBlockstateProvider;
import xyz.brassgoggledcoders.workshop.datagen.models.WorkshopResourceModelProvider;
import xyz.brassgoggledcoders.workshop.datagen.recipe.*;
import xyz.brassgoggledcoders.workshop.datagen.tags.WorkshopFluidTagsProvider;
import xyz.brassgoggledcoders.workshop.datagen.tags.WorkshopItemTagsProvider;
import xyz.brassgoggledcoders.workshop.util.RangedItemStack;

import java.util.Collections;
import java.util.Iterator;

@Mod.EventBusSubscriber(modid = Workshop.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WorkshopDataGenerator {

    static {
        JSONSerializableDataHandler.map(TileEntityType.class, (type) -> new JsonPrimitive(type.getRegistryName().toString()),
                (element) -> ForgeRegistries.TILE_ENTITIES.getValue(new ResourceLocation(element.getAsString())));
        JSONSerializableDataHandler.map(RangedItemStack.class, (object) -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("min", object.min);
            jsonObject.addProperty("max", object.max);
            jsonObject.add("stack", JSONSerializableDataHandler.writeItemStack(object.stack));
            return jsonObject;
        }, (json) -> {
            JsonObject jsonObject = json.getAsJsonObject();
            return new RangedItemStack(JSONSerializableDataHandler.readItemStack(jsonObject.get("stack").getAsJsonObject()), jsonObject.get("min").getAsInt(), jsonObject.get("max").getAsInt());
        });
        JSONSerializableDataHandler.map(RangedItemStack[].class, (type) -> {
            JsonArray array = new JsonArray();
            for (RangedItemStack rStack : type) {
                array.add(JSONSerializableDataHandler.write(RangedItemStack.class, rStack));
            }
            return array;
        }, (element) -> {
            RangedItemStack[] stacks = new RangedItemStack[element.getAsJsonArray().size()];
            int i = 0;
            Iterator<JsonElement> iterator;
            for (iterator = element.getAsJsonArray().iterator(); iterator.hasNext(); i++) {
                JsonElement jsonElement = iterator.next();
                stacks[i] = JSONSerializableDataHandler.read(RangedItemStack.class, jsonElement);
            }
            return stacks;
        });
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        final DataGenerator dataGenerator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = new ExistingFileHelper(Collections.emptyList(), false);

        if (event.includeClient()) {
            dataGenerator.addProvider(new WorkshopUSLanguageProvider(dataGenerator));
            dataGenerator.addProvider(new WorkshopGBLanguageProvider(dataGenerator));
            dataGenerator.addProvider(new WorkshopBlockstateProvider(dataGenerator, existingFileHelper));
            dataGenerator.addProvider(new WorkshopItemModelProvider(dataGenerator, existingFileHelper));
            dataGenerator.addProvider(new WorkshopResourceModelProvider(dataGenerator, existingFileHelper));
            dataGenerator.addProvider(new WorkshopResourceBlockstateProvider(dataGenerator, existingFileHelper));
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
            dataGenerator.addProvider(new MoltenChamberRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new MortarRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new DryingBasinRecipeProvider(dataGenerator));

            dataGenerator.addProvider(new WorkshopItemTagsProvider(dataGenerator));
            dataGenerator.addProvider(new WorkshopFluidTagsProvider(dataGenerator));
            //dataGenerator.addProvider(new WorkshopBlockTagsProvider(dataGenerator));

            dataGenerator.addProvider(new WorkshopBookProvider(dataGenerator));
        }
    }
}
