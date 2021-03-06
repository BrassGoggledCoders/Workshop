package xyz.brassgoggledcoders.workshop.content;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.material.IResourceType;
import com.hrznstudio.titanium.material.ResourceMaterial;
import com.hrznstudio.titanium.material.ResourceTypeItem;
import com.hrznstudio.titanium.material.ResourceTypeProperties;
import com.hrznstudio.titanium.material.advancedtype.ItemAdvancedResourceType;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public enum WorkshopResourceType implements IResourceType, IStringSerializable {
    PIPE("pipes"),
    FILM("films");

    private final String tag;

    WorkshopResourceType(String tag) {
        this.tag = tag;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public IFactory<ForgeRegistryEntry> getInstanceFactory(ResourceMaterial material, @Nullable ResourceTypeProperties properties) {
        return () -> new ResourceTypeItem(material, this, ItemAdvancedResourceType.SIMPLE, properties);
    }

    @Override
    public String getString() {
        return name().toLowerCase();
    }
}
