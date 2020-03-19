package xyz.brassgoggledcoders.workshop.assets;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.hrznstudio.titanium.client.gui.addon.BasicGuiAddon;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TextFormatting;

public class HeatBarGuiAddon extends BasicGuiAddon {
    private PosHeatBar heatBar;
    private IAssetProvider provider;

    public HeatBarGuiAddon(int posX, int posY, PosHeatBar posHeatBar) {
        super(posX, posY);
        this.heatBar = posHeatBar;
    }

    public int getXSize() {
        return this.provider != null ? this.heatBar.getBarDirection().getXSize(this.provider) : 0;
    }

    public int getYSize() {
        return this.provider != null ? this.heatBar.getBarDirection().getYSize(this.provider) : 0;
    }

    public void drawGuiContainerBackgroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX,
            int mouseY, float partialTicks) {
        this.provider = provider;
        this.heatBar.getBarDirection().render(screen, guiX, guiY, provider, this);
    }

    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX,
            int mouseY) {
    }

    public PosHeatBar getHeatBar() {
        return this.heatBar;
    }

    public List<String> getTooltipLines() {
        List<String> tooltip = new ArrayList<>();
        tooltip.add(TextFormatting.GOLD + "Temp: " + TextFormatting.WHITE
                + (new DecimalFormat()).format((long) this.heatBar.getTemp()) + TextFormatting.GOLD + "/"
                + TextFormatting.WHITE + (new DecimalFormat()).format(this.heatBar.getMaxTemp()));
        return tooltip;
    }
}
