package xyz.brassgoggledcoders.workshop.component.machine;

import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.client.screen.addon.BasicScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(value = Dist.CLIENT)
public class BurnTimerScreenAddon<T extends IComponentHarness> extends BasicScreenAddon {

    private final BurnTimerComponent<T> progressBar;
    private IAssetProvider provider;

    public BurnTimerScreenAddon(int posX, int posY, BurnTimerComponent<T> progressBarComponent) {
        super(posX, posY);
        this.progressBar = progressBarComponent;
    }

    @Override
    public int getXSize() {
        return (provider != null ? progressBar.getBarDirection().getXSize(provider) : 0);
    }

    @Override
    public int getYSize() {
        return (provider != null ? progressBar.getBarDirection().getYSize(provider) : 0);
    }

    public BurnTimerComponent<T> getProgressBar() {
        return progressBar;
    }

    @Override
    public void drawBackgroundLayer(MatrixStack matrixStack, Screen screen, IAssetProvider iAssetProvider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        this.provider = iAssetProvider;
        render(matrixStack, screen, guiX, guiY, provider, this);
    }

    @Override
    public void drawForegroundLayer(MatrixStack matrixStack, Screen screen, IAssetProvider iAssetProvider, int guiX, int guiY, int mouseX, int mouseY) {
    }

    @Override
    public List<ITextComponent> getTooltipLines() {
        List<ITextComponent> tooltip = new ArrayList<>();
        tooltip.add(new StringTextComponent(TextFormatting.RED + "Ticks Remaining: " + TextFormatting.WHITE + new DecimalFormat().format(progressBar.getProgress()) + TextFormatting.GOLD + "/" + TextFormatting.WHITE + new DecimalFormat().format(progressBar.getMaxProgress())));
        int progress = (progressBar.getMaxProgress() - progressBar.getProgress()) / progressBar.getProgressIncrease();
        if (!progressBar.getIncreaseType()) progress = progressBar.getMaxProgress() - progress;
        tooltip.add(new StringTextComponent(TextFormatting.RED + "ETA: " + TextFormatting.WHITE + new DecimalFormat().format(Math.ceil(progress * progressBar.getTickingTime() / 20D)) + TextFormatting.DARK_AQUA + "s"));
        return tooltip;
    }

    public <H extends IComponentHarness> void render(MatrixStack stack, Screen screen, int guiX, int guiY, IAssetProvider provider, BurnTimerScreenAddon<H> addon) {
        IAsset assetBorder = IAssetProvider.getAsset(provider, AssetTypes.FURNACE_FLAMES_BACKGROUND);
        Point offset = assetBorder.getOffset();
        Rectangle area = assetBorder.getArea();
        screen.getMinecraft().getTextureManager().bindTexture(assetBorder.getResourceLocation());

        screen.blit(stack, guiX + addon.getPosX() + offset.x, guiY + addon.getPosY() + offset.y, area.x, area.y, area.width, area.height);
        RenderSystem.color4f(addon.getProgressBar().getColor().getColorComponentValues()[0], addon.getProgressBar().getColor().getColorComponentValues()[1], addon.getProgressBar().getColor().getColorComponentValues()[2], 1);
        IAsset asset = IAssetProvider.getAsset(provider, AssetTypes.FURNACE_FLAMES);
        offset = asset.getOffset();
        area = asset.getArea();
        screen.getMinecraft().getTextureManager().bindTexture(asset.getResourceLocation());
        int progress = addon.getProgressBar().getProgress();
        int maxProgress = addon.getProgressBar().getMaxProgress();
        int progressOffset = progress * area.height / Math.max(maxProgress, 1);
        screen.blit(stack, addon.getPosX() + offset.x + guiX,
                addon.getPosY() + offset.y + area.height - progressOffset + guiY,
                area.x,
                area.y + (area.height - progressOffset),
                area.width,
                progressOffset);
        RenderSystem.color4f(1, 1, 1, 1);
    }
}

