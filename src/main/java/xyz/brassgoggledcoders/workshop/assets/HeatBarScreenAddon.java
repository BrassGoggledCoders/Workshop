package xyz.brassgoggledcoders.workshop.assets;

import com.hrznstudio.titanium.client.screen.addon.BasicScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TextFormatting;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HeatBarScreenAddon extends BasicScreenAddon {
    private HeatBarComponent heatBar;
    private IAssetProvider provider;

    public HeatBarScreenAddon(int posX, int posY, HeatBarComponent posHeatBar) {
        super(posX, posY);
        this.heatBar = posHeatBar;
    }

    public int getXSize() {
        return this.provider != null ? this.heatBar.getBarDirection().getXSize(this.provider) : 0;
    }

    public int getYSize() {
        return this.provider != null ? this.heatBar.getBarDirection().getYSize(this.provider) : 0;
    }

    public HeatBarComponent getHeatBar() {
        return this.heatBar;
    }

    @Override
    public void drawBackgroundLayer(Screen screen, IAssetProvider iAssetProvider, int guiX, int guiY, int mouseX,
                                    int mouseY, float partialTicks) {
        this.provider = provider;
        this.heatBar.getBarDirection().render(screen, guiX, guiY, provider, this);
    }

    @Override
    public void drawForegroundLayer(Screen screen, IAssetProvider iAssetProvider, int i, int i1, int i2, int i3) {

    }

    public List<String> getTooltipLines() {
        List<String> tooltip = new ArrayList<>();
        tooltip.add(TextFormatting.GOLD + "Temp: " + TextFormatting.WHITE
                + (new DecimalFormat()).format((long) this.heatBar.getTemp()) + TextFormatting.GOLD + "/"
                + TextFormatting.WHITE + (new DecimalFormat()).format(this.heatBar.getMaxTemp()));
        return tooltip;
    }
}
