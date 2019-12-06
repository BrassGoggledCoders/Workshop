package xyz.brassgoggledcoders.workshop.assets;

import com.hrznstudio.titanium.api.client.GenericAssetType;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;

public class WorkshopAssetTypes {
    public static final IAssetType<IAsset> THERMOMETER_EMPTY;
    public static final IAssetType<IAsset> THERMOMETER_FULL;

    public WorkshopAssetTypes() {
    }

    static {
        THERMOMETER_EMPTY = new GenericAssetType(WorkshopAssetProvider.WORKSHOP_PROVIDER::getAsset, IAsset.class);
        THERMOMETER_FULL = new GenericAssetType(WorkshopAssetProvider.WORKSHOP_PROVIDER::getAsset, IAsset.class);
    }
}
