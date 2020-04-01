package xyz.brassgoggledcoders.workshop.content;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.material.IResourceType;
import com.hrznstudio.titanium.material.ResourceMaterial;
import com.hrznstudio.titanium.material.ResourceTypeBlock;
import com.hrznstudio.titanium.material.ResourceTypeItem;
import com.hrznstudio.titanium.material.ResourceTypeProperties;
import com.hrznstudio.titanium.material.advancedtype.BlockAdvancedResourceType;
import com.hrznstudio.titanium.material.advancedtype.ItemAdvancedResourceType;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public enum WorkshopResourceType implements IResourceType, IStringSerializable {
    PIPE,
    FILM;

    private final String tag;

    WorkshopResourceType(String tag) {
        this.tag = tag;
    }

    WorkshopResourceType() {
        this.tag = getName();
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
    public String getName() {
        return name().toLowerCase();
    }
}
