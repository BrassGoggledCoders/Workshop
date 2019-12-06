package xyz.brassgoggledcoders.workshop.assets;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

public interface IWorkAssetProvider {
    ResourceLocation WORKSHOP_LOCATION = new ResourceLocation(MOD_ID, "textures/gui/workshopassets.png");
    WorkshopAssetProvider WORKSHOP_PROVIDER = new WorkshopAssetProvider();

    @Nonnull
    static <T extends IWorkAsset> T getAsset(IWorkAssetProvider provider, IWorkAssetType<T> type) {
        T asset = provider.getAsset(type);
        if (asset == null && provider != WORKSHOP_PROVIDER) {
            asset = WORKSHOP_PROVIDER.getAsset(type);
        }

        return asset != null ? asset : type.getDefaultAsset();
    }

    @Nullable
    <T extends IWorkAsset> T getAsset(IWorkAssetType<T> var1);
}
