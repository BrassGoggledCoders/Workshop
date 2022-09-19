package xyz.brassgoggledcoders.workshop.util;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Optional;

public class JSONHelper {


    public static <T extends ForgeRegistryEntry<T>> Either<T, TagKey<T>> getValueOrTag(JsonObject object, String fieldName, IForgeRegistry<T> registry) {
        String fieldValue = GsonHelper.getAsString(object, fieldName);
        if (fieldValue.startsWith("#")) {
            return Optional.ofNullable(ResourceLocation.tryParse(fieldValue.substring(1)))
                    .flatMap(name -> Optional.ofNullable(registry.tags())
                            .map(tagManager -> tagManager.createTagKey(name))
                    )
                    .<Either<T, TagKey<T>>>map(Either::right)
                    .orElseThrow(() -> new JsonParseException("'" + fieldName.substring(1) + "' is not a valid ResourceLocation"));
        } else {
            return Optional.ofNullable(ResourceLocation.tryParse(fieldValue))
                    .map(registry::getValue)
                    .<Either<T, TagKey<T>>>map(Either::left)
                    .orElseThrow(() -> new JsonParseException("'" + fieldName + "' is not a valid Registry value"));
        }
    }
}