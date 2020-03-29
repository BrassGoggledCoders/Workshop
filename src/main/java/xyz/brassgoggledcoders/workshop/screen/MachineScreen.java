package xyz.brassgoggledcoders.workshop.screen;

import com.hrznstudio.titanium.client.screen.container.BasicContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.workshop.container.MachineContainer;

public class MachineScreen extends BasicContainerScreen<MachineContainer> {
    public MachineScreen(MachineContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.getContainer()
                .getMachine()
                .getScreenAddons()
                .forEach(screenAddon ->
                        this.getAddons().add(screenAddon.create()));
    }
}
