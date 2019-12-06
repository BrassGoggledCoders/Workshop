package xyz.brassgoggledcoders.workshop.assets;

import java.util.function.Function;
import java.util.function.Supplier;

public class WorkAssetType<T extends IWorkAsset> implements IWorkAssetType<T> {
    private Supplier<T> defaultAsset;
    private Class<T> tClass;

    public WorkAssetType(Supplier<T> defaultAsset, Class<T> tClass) {
        this.defaultAsset = defaultAsset;
        this.tClass = tClass;
    }

    public WorkAssetType(Function<IWorkAssetType<T>, T> defaultAsset, Class<T> tClass) {
        this.defaultAsset = () -> {
            return (IWorkAsset)defaultAsset.apply(this);
        };
        this.tClass = tClass;
    }

    public T getDefaultAsset() {
        return (IWorkAsset)this.defaultAsset.get();
    }

    public T castOrDefault(IWorkAsset asset) throws ClassCastException {
        return this.tClass.isInstance(asset) ? (IWorkAsset)this.tClass.cast(asset) : (IWorkAsset)this.defaultAsset.get();
    }
}
