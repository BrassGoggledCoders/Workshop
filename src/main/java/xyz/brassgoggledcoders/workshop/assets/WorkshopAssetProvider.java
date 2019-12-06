package xyz.brassgoggledcoders.workshop.assets;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;
import com.hrznstudio.titanium.client.gui.asset.DefaultAssetProvider;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.*;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

public class WorkshopAssetProvider implements IAssetProvider {

    ResourceLocation WORKSHOP_LOCATION = new ResourceLocation(MOD_ID, "textures/gui/workshopassets.png");
    static WorkshopAssetProvider WORKSHOP_PROVIDER = new WorkshopAssetProvider();

    private final IAsset THERMOMETER_EMPTY = new IAsset() {
        @Override
        public Rectangle getArea() {
            return new Rectangle(0, 0, 13, 31);
        }
    };
    private final IAsset THERMOMETER_FULL = new IAsset() {
        @Override
        public Rectangle getArea() {
            return new Rectangle(16, 0, 13, 31);
        }
    };

    WorkshopAssetProvider() {
    }

    @Nullable
    @Override
    public <T extends IAsset> T getAsset(IAssetType<T> assetType) {
        if (assetType == WorkshopAssetTypes.THERMOMETER_EMPTY) {
            return assetType.castOrDefault(this.THERMOMETER_EMPTY);
        }
        if (assetType == WorkshopAssetTypes.THERMOMETER_FULL) {
            return assetType.castOrDefault(this.THERMOMETER_FULL);
        } else {
            return assetType == WorkshopAssetTypes.THERMOMETER_EMPTY ? assetType.castOrDefault(this.THERMOMETER_EMPTY) : null;
        }
    }
}
