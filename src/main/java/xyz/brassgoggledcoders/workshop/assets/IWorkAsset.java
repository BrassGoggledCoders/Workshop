package xyz.brassgoggledcoders.workshop.assets;

import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public interface IWorkAsset {
    default ResourceLocation getResourceLocation() {
        return IAssetProvider.DEFAULT_LOCATION;
    }

    Rectangle getArea();

    default Point getOffset() {
        return new Point();
    }
}