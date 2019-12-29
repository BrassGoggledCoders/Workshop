package xyz.brassgoggledcoders.workshop.assets;

import com.hrznstudio.titanium.api.client.GenericAssetType;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;

public class WorkshopAssetTypes {
    public static final IAssetType<IAsset> THERMOMETER_VERTICAL_EMPTY;
    public static final IAssetType<IAsset> THERMOMETER_VERTICAL_FULL;

    public static final IAssetType<IAsset> THERMOMETER_HORIZONTAL_LEFT_EMPTY;
    public static final IAssetType<IAsset> THERMOMETER_HORIZONTAL_LEFT_FULL;

    public static final IAssetType<IAsset> THERMOMETER_HORIZONTAL_RIGHT_EMPTY;
    public static final IAssetType<IAsset> THERMOMETER_HORIZONTAL_RIGHT_FULL;

    public WorkshopAssetTypes() {
    }

    static {
        THERMOMETER_VERTICAL_EMPTY = new GenericAssetType<IAsset>(WorkshopAssetProvider.WORKSHOP_PROVIDER::getAsset, IAsset.class);
        THERMOMETER_VERTICAL_FULL = new GenericAssetType<IAsset>(WorkshopAssetProvider.WORKSHOP_PROVIDER::getAsset, IAsset.class);

        THERMOMETER_HORIZONTAL_LEFT_EMPTY = new GenericAssetType<IAsset>(WorkshopAssetProvider.WORKSHOP_PROVIDER::getAsset, IAsset.class);
        THERMOMETER_HORIZONTAL_LEFT_FULL = new GenericAssetType<IAsset>(WorkshopAssetProvider.WORKSHOP_PROVIDER::getAsset, IAsset.class);

        THERMOMETER_HORIZONTAL_RIGHT_EMPTY = new GenericAssetType<IAsset>(WorkshopAssetProvider.WORKSHOP_PROVIDER::getAsset, IAsset.class);
        THERMOMETER_HORIZONTAL_RIGHT_FULL = new GenericAssetType<IAsset>(WorkshopAssetProvider.WORKSHOP_PROVIDER::getAsset, IAsset.class);

    }
}
