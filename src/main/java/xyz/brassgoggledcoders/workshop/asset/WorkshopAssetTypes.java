package xyz.brassgoggledcoders.workshop.asset;

import com.hrznstudio.titanium.api.client.GenericAssetType;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;

public class WorkshopAssetTypes {
    public static final IAssetType<IAsset> THERMOMETER_VERTICAL_EMPTY = new GenericAssetType<>(WorkshopAssetProvider.WORKSHOP_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> THERMOMETER_VERTICAL_FULL = new GenericAssetType<>(WorkshopAssetProvider.WORKSHOP_PROVIDER::getAsset, IAsset.class);

    public static final IAssetType<IAsset> THERMOMETER_HORIZONTAL_LEFT_EMPTY = new GenericAssetType<>(WorkshopAssetProvider.WORKSHOP_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> THERMOMETER_HORIZONTAL_LEFT_FULL = new GenericAssetType<>(WorkshopAssetProvider.WORKSHOP_PROVIDER::getAsset, IAsset.class);

    public static final IAssetType<IAsset> THERMOMETER_HORIZONTAL_RIGHT_EMPTY = new GenericAssetType<>(WorkshopAssetProvider.WORKSHOP_PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> THERMOMETER_HORIZONTAL_RIGHT_FULL = new GenericAssetType<>(WorkshopAssetProvider.WORKSHOP_PROVIDER::getAsset, IAsset.class);

    private WorkshopAssetTypes() {
    }
}
