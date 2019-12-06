package xyz.brassgoggledcoders.workshop.assets;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.client.gui.addon.ProgressBarGuiAddon;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;

import java.awt.*;
import java.util.List;

public class PosHeatBar implements INBTSerializable<CompoundNBT>, IGuiAddonProvider {

    private int posX;
    private int posY;
    private int temp;
    private PosHeatBar.BarDirection barDirection;

    private DyeColor color;

    public PosHeatBar(int posX, int posY, int temp) {
        this.posX = posX;
        this.posY = posY;
        this.temp = temp;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public int getTemp() {
        return this.temp;
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        return null;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    public static enum BarDirection {

        VERTICAL_UP {
            public void render(Screen screen, int guiX, int guiY, IAssetProvider provider, ProgressBarGuiAddon addon) {
                IAsset assetBorder = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BORDER_VERTICAL);
                Point offset = assetBorder.getOffset();
                Rectangle area = assetBorder.getArea();
                screen.getMinecraft().getTextureManager().bindTexture(assetBorder.getResourceLocation());
                screen.blit(guiX + addon.getPosX() + offset.x, guiY + addon.getPosY() + offset.y, area.x, area.y, area.width, area.height);
                GlStateManager.color4f(addon.getProgressBar().getColor().getColorComponentValues()[0], addon.getProgressBar().getColor().getColorComponentValues()[1], addon.getProgressBar().getColor().getColorComponentValues()[2], 1.0F);
                IAsset assetBar = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND_VERTICAL);
                offset = assetBar.getOffset();
                area = assetBar.getArea();
                screen.getMinecraft().getTextureManager().bindTexture(assetBar.getResourceLocation());
                screen.blit(guiX + addon.getPosX() + offset.x, guiY + addon.getPosY() + offset.y, area.x, area.y, area.width, area.height);
                IAsset asset = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_VERTICAL);
                offset = asset.getOffset();
                area = asset.getArea();
                screen.getMinecraft().getTextureManager().bindTexture(asset.getResourceLocation());
                int temp = addon.getProgressBar().getProgress();
                screen.blit(addon.getPosX() + offset.x + guiX, addon.getPosY() + offset.y + area.height - temp + guiY, area.x, area.y + (area.height - temp), area.width, temp);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

            public int getXSize(IAssetProvider provider) {
                return (int)IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BORDER_VERTICAL).getArea().getWidth();
            }

            public int getYSize(IAssetProvider provider) {
                return (int)IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BORDER_VERTICAL).getArea().getHeight();
            }
        },
        HORIZONTAL_RIGHT {
            public void render(Screen screen, int guiX, int guiY, IAssetProvider provider, ProgressBarGuiAddon addon) {

            }

            public int getXSize(IAssetProvider provider) {
                return (int)IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BORDER_VERTICAL).getArea().getWidth();
            }

            public int getYSize(IAssetProvider provider) {
                return (int)IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BORDER_VERTICAL).getArea().getHeight();
            }
        };

        private BarDirection() {
        }

        @OnlyIn(Dist.CLIENT)
        public abstract void render(Screen var1, int var2, int var3, IAssetProvider var4, ProgressBarGuiAddon var5);

        @OnlyIn(Dist.CLIENT)
        public abstract int getXSize(IAssetProvider var1);

        @OnlyIn(Dist.CLIENT)
        public abstract int getYSize(IAssetProvider var1);
    }

}
