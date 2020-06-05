package xyz.brassgoggledcoders.workshop.component.machine;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;

import java.util.Collections;
import java.util.List;

public class BurnTimerComponent<T extends IComponentHarness> extends ProgressBarComponent<T> {
    public BurnTimerComponent(int posX, int posY, int maxProgress) {
        super(posX, posY, maxProgress);
        this.setIncreaseType(false);
        this.setCanIncrease((tile) -> true);
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return Collections.singletonList(() -> new BurnTimerScreenAddon<>(this.getPosX(), this.getPosY(), this));
    }
}
