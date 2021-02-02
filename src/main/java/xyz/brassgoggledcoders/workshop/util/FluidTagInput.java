/*
 * BluSunrize
 * Copyright (c) 2020
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 *
 */

package xyz.brassgoggledcoders.workshop.util;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//Stolen from ImmersiveEngineering :)
public class FluidTagInput implements Predicate<FluidStack> {
    // Generally left on the server, right on the client
    protected final Either<ITag<Fluid>, List<ResourceLocation>> fluidTag;
    protected final int amount;
    protected final CompoundNBT nbtTag;

    public FluidTagInput(Either<ITag<Fluid>, List<ResourceLocation>> matching, int amount, CompoundNBT nbtTag) {
        this.fluidTag = matching;
        this.amount = amount;
        this.nbtTag = nbtTag;
    }

    public FluidTagInput(ITag<Fluid> fluidTag, int amount, CompoundNBT nbtTag) {
        this(Either.left(fluidTag), amount, nbtTag);
    }

    public FluidTagInput(ResourceLocation resourceLocation, int amount, CompoundNBT nbtTag) {
        this(TagCollectionManager.getManager().getFluidTags().get(resourceLocation), amount, nbtTag);
    }

    public FluidTagInput(ResourceLocation resourceLocation, int amount) {
        this(resourceLocation, amount, null);
    }

    public FluidTagInput(ITag<Fluid> tag, int amount) {
        this(tag, amount, null);
    }

    public FluidTagInput withAmount(int amount) {
        return new FluidTagInput(this.fluidTag, amount, this.nbtTag);
    }

    @Override
    public boolean test(@Nullable FluidStack fluidStack) {
        return testIgnoringAmount(fluidStack) && fluidStack.getAmount() >= this.amount;
    }

    public boolean testIgnoringAmount(@Nullable FluidStack fluidStack) {
        if (fluidStack == null)
            return false;
        if (!fluidTag.map(
                t -> t.contains(fluidStack.getFluid()),
                l -> l.contains(fluidStack.getFluid().getRegistryName())
        ))
            return false;
        if (this.nbtTag != null)
            return fluidStack.hasTag() && fluidStack.getTag().equals(this.nbtTag);
        return true;
    }

    @Nonnull
    public List<FluidStack> getMatchingFluidStacks() {
        return fluidTag.map(
                t -> t.getAllElements().stream(),
                l -> l.stream().map(ForgeRegistries.FLUIDS::getValue)
        )
                .map(fluid -> new FluidStack(fluid, FluidTagInput.this.amount, FluidTagInput.this.nbtTag))
                .collect(Collectors.toList());
    }

    public int getAmount() {
        return amount;
    }

    public FluidStack getRandomizedExampleStack(int rand) {
        List<FluidStack> all = getMatchingFluidStacks();
        return all.get((rand / 20) % all.size());
    }

    public static JsonElement serialize(FluidTagInput tagInput) {
        if(tagInput == null) { //Where the input is optional this will be the case
            return null;
        }
        JsonObject jsonObject = new JsonObject();
        ITag<Fluid> unnamedTag = tagInput.fluidTag.orThrow();
        ResourceLocation name = TagCollectionManager.getManager().getFluidTags().getValidatedIdFromTag(unnamedTag);
        jsonObject.addProperty("tag", name.toString());
        jsonObject.addProperty("amount", tagInput.amount);
        if (tagInput.nbtTag != null) {
            jsonObject.addProperty("nbt", tagInput.nbtTag.toString());
        }
        return jsonObject;
    }

    public static FluidTagInput deserialize(JsonElement input) {
        Preconditions.checkArgument(input instanceof JsonObject, "FluidTagWithSize can only be deserialized from a JsonObject");
        JsonObject jsonObject = input.getAsJsonObject();
        ResourceLocation resourceLocation = new ResourceLocation(JSONUtils.getString(jsonObject, "tag"));
        if (!JSONUtils.hasField(jsonObject, "nbt")) {
            return new FluidTagInput(resourceLocation, JSONUtils.getInt(jsonObject, "amount"));
        }
        try {
            CompoundNBT nbt = InventoryUtil.parseNbtFromJson(jsonObject.get("nbt"));
            return new FluidTagInput(resourceLocation, JSONUtils.getInt(jsonObject, "amount"), nbt);
        } catch (CommandSyntaxException e) {
            throw new JsonParseException(e);
        }
    }

    public static FluidTagInput read(PacketBuffer input) {
        int numMatching = input.readVarInt();
        List<ResourceLocation> matching = new ArrayList<>(numMatching);
        for (int i = 0; i < numMatching; ++i)
            matching.add(input.readResourceLocation());
        int amount = input.readInt();
        CompoundNBT nbt = input.readBoolean() ? input.readCompoundTag() : null;
        return new FluidTagInput(Either.right(matching), amount, nbt);
    }

    public static void write(PacketBuffer out, FluidTagInput tagInput) {
        List<ResourceLocation> matching = tagInput.fluidTag.map(
                f -> f.getAllElements().stream().map(Fluid::getRegistryName).collect(Collectors.toList()),
                l -> l
        );
        out.writeVarInt(matching.size());
        for (ResourceLocation rl : matching) {
            out.writeResourceLocation(rl);
        }
        out.writeInt(tagInput.amount);
        out.writeBoolean(tagInput.nbtTag != null);
        if (tagInput.nbtTag != null) {
            out.writeCompoundTag(tagInput.nbtTag);
        }
    }
}
