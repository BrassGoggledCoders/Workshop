package xyz.brassgoggledcoders.workshop.component.machine;

import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.*;

public class FurnaceAssetProvider implements IAssetProvider {
    public static final FurnaceAssetProvider PROVIDER = new FurnaceAssetProvider();
    public final ResourceLocation LOCATION = new ResourceLocation("minecraft", "textures/gui/container/furnace.png");
    private final IAsset FURNACE_FLAMES_BACKGROUND = new IAsset() {
        @Override
        public ResourceLocation getResourceLocation() {
            return LOCATION;
        }

        @Override
        public Rectangle getArea() {
            return new Rectangle(57, 37, 13, 13);
        }
    };
    private final IAsset FURNACE_FLAMES = new IAsset() {
        @Override
        public ResourceLocation getResourceLocation() {
            return LOCATION;
        }

        @Override
        public Rectangle getArea() {
            return new Rectangle(176, 0, 14, 14);
        }
    };

    @Nullable
    @Override
    public <T extends IAsset> T getAsset(IAssetType<T> assetType) {
        if (assetType == AssetTypes.FURNACE_FLAMES) {
            return assetType.castOrDefault(FURNACE_FLAMES);
        } else if (assetType == AssetTypes.FURNACE_FLAMES_BACKGROUND) {
            return assetType.castOrDefault(FURNACE_FLAMES_BACKGROUND);
        }
        return null;
    }
}
