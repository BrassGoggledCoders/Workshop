package xyz.brassgoggledcoders.workshop.assets;

public class WorkshopAssetTypes {
    public static final IWorkAssetType<IWorkAsset> THERMOMETER_EMPTY;
    public static final IWorkAssetType<IWorkAsset> THERMOMETER_FULL;

    public WorkshopAssetTypes() {
    }

    static {
        THERMOMETER_EMPTY = new WorkAssetType(WorkshopAssetProvider.WORKSHOP_PROVIDER::getAsset, IWorkAsset.class);
        THERMOMETER_FULL = new WorkAssetType(WorkshopAssetProvider.WORKSHOP_PROVIDER::getAsset, IWorkAsset.class);
    }
}
