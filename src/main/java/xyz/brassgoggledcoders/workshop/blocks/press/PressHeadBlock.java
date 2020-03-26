package xyz.brassgoggledcoders.workshop.blocks.press;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.RotatableBlock;

public class PressHeadBlock extends RotatableBlock<PressTile> {

    public PressHeadBlock(String name, Properties properties, Class<PressTile> tileClass) {
        super(name, properties, tileClass);
    }

    @Override
    public IFactory<PressTile> getTileEntityFactory() {
        return null;
    }
}
