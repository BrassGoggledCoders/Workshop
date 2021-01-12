package xyz.brassgoggledcoders.workshop.api;

import net.minecraft.potion.EffectInstance;

public interface IStackingEffect {
    int getMaxLevel();

    //Effect to be applied if this potion is stacked past the above max
    EffectInstance getPostMaxEffect();
}
