package xyz.brassgoggledcoders.workshop.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.StateContainer;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class HoneyFluid extends ForgeFlowingFluid {

    protected HoneyFluid(Properties properties) {
        super(properties);
    }

    @Override
    public int getSlopeFindDistance(IWorldReader worldIn) {
        return worldIn.getDimensionType().isUltrawarm() ? 4 : 2;
    }

    @Override
    public int getLevelDecreasePerBlock(IWorldReader worldIn) {
        return worldIn.getDimensionType().isUltrawarm() ? 1 : 3;
    }

    @Override
    public int getTickRate(IWorldReader p_205569_1_) {
        return p_205569_1_.getDimensionType().isUltrawarm() ? 5 : 20;
    }

    @Override
    protected boolean canSourcesMultiply() {
        return false;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    public static class Flowing extends HoneyFluid {
        public Flowing(Properties properties) {
            super(properties);
        }

        protected void fillStateContainer(StateContainer.Builder<Fluid, FluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        public int getLevel(FluidState state) {
            return state.get(LEVEL_1_8);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends HoneyFluid {
        public Source(Properties properties) {
            super(properties);
        }

        public int getLevel(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
