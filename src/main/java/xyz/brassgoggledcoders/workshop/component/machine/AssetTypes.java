package xyz.brassgoggledcoders.workshop.component.machine;

import com.hrznstudio.titanium.api.client.GenericAssetType;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;

public class AssetTypes {
    public static final IAssetType<IAsset> FURNACE_FLAMES_BACKGROUND = new GenericAssetType<>(FurnaceAssetProvider.PROVIDER::getAsset, IAsset.class);
    public static final IAssetType<IAsset> FURNACE_FLAMES = new GenericAssetType<>(FurnaceAssetProvider.PROVIDER::getAsset, IAsset.class);
}
