package xyz.brassgoggledcoders.workshop;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.client.screen.container.BasicContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.workshop.blocks.MachineTileContainer;

public class MachineScreen extends BasicContainerScreen<MachineTileContainer<?>> {
    public MachineScreen(MachineTileContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.getContainer().getTile().getMachineComponent().getScreenAddons().stream().map(IFactory::create)
                .forEach(this.getAddons()::add);
    }
}
