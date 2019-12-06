package xyz.brassgoggledcoders.workshop.assets;

public interface IWorkAssetType<T extends IWorkAsset> {
    T getDefaultAsset();

    T castOrDefault(IWorkAsset var1) throws ClassCastException;
}