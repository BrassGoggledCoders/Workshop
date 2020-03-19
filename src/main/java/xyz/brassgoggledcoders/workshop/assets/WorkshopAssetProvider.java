package xyz.brassgoggledcoders.workshop.assets;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

import java.awt.Rectangle;

import javax.annotation.Nullable;

import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;

import net.minecraft.util.ResourceLocation;

public class WorkshopAssetProvider implements IAssetProvider {

    ResourceLocation WORKSHOP_LOCATION = new ResourceLocation(MOD_ID, "textures/gui/workshopassets.png");
    static WorkshopAssetProvider WORKSHOP_PROVIDER = new WorkshopAssetProvider();

    private final IAsset THERMOMETER_VERTICAL_EMPTY = new IAsset() {
        @Override
        public Rectangle getArea() {
            return new Rectangle(0, 0, 13, 31);
        }
    };
    private final IAsset THERMOMETER_VERTICAL_FULL = new IAsset() {
        @Override
        public Rectangle getArea() {
            return new Rectangle(16, 0, 13, 31);
        }
    };
    private final IAsset THERMOMETER_HORIZONTAL_LEFT_EMPTY = new IAsset() {
        @Override
        public Rectangle getArea() {
            return new Rectangle(0, 0, 13, 31);
        }
    };
    private final IAsset THERMOMETER_HORIZONTAL_LEFT_FULL = new IAsset() {
        @Override
        public Rectangle getArea() {
            return new Rectangle(16, 0, 13, 31);
        }
    };
    private final IAsset THERMOMETER_HORIZONTAL_RIGHT_EMPTY = new IAsset() {
        @Override
        public Rectangle getArea() {
            return new Rectangle(0, 0, 13, 31);
        }
    };
    private final IAsset THERMOMETER_HORIZONTAL_RIGHT_FULL = new IAsset() {
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
        if (assetType == WorkshopAssetTypes.THERMOMETER_VERTICAL_EMPTY) {
            return assetType.castOrDefault(this.THERMOMETER_VERTICAL_EMPTY);
        }
        else if (assetType == WorkshopAssetTypes.THERMOMETER_VERTICAL_FULL) {
            return assetType.castOrDefault(this.THERMOMETER_VERTICAL_FULL);

        } else if (assetType == WorkshopAssetTypes.THERMOMETER_HORIZONTAL_LEFT_EMPTY) {
            return assetType.castOrDefault(this.THERMOMETER_HORIZONTAL_LEFT_EMPTY);

        }else if (assetType == WorkshopAssetTypes.THERMOMETER_HORIZONTAL_LEFT_FULL) {
            return assetType.castOrDefault(this.THERMOMETER_HORIZONTAL_LEFT_FULL);

        }else if (assetType == WorkshopAssetTypes.THERMOMETER_HORIZONTAL_RIGHT_EMPTY) {
            return assetType.castOrDefault(this.THERMOMETER_HORIZONTAL_RIGHT_EMPTY);

        }else if (assetType == WorkshopAssetTypes.THERMOMETER_HORIZONTAL_RIGHT_FULL) {
            return assetType.castOrDefault(this.THERMOMETER_HORIZONTAL_RIGHT_FULL);

        }else {
            return assetType == WorkshopAssetTypes.THERMOMETER_VERTICAL_EMPTY ? assetType.castOrDefault(this.THERMOMETER_VERTICAL_EMPTY) : null;
        }
    }
}
