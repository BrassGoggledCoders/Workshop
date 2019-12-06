package xyz.brassgoggledcoders.workshop.assets;

import com.hrznstudio.titanium.api.client.AssetTypes;

import javax.annotation.Nullable;
import java.awt.*;

public class WorkshopAssetProvider implements IWorkAssetProvider {

    private final IWorkAsset THERMOMETER_EMPTY = new IWorkAsset() {
        @Override
        public Rectangle getArea() {
            return new Rectangle(0, 0, 13, 31);
        }
    };
    private final IWorkAsset THERMOMETER_FULL = new IWorkAsset() {
        @Override
        public Rectangle getArea() {
            return new Rectangle(16, 0, 13, 31);
        }
    };

    WorkshopAssetProvider() {
    }

    @Nullable
    @Override
    public <T extends IWorkAsset> T getAsset(IWorkAssetType<T> assetType) {
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
