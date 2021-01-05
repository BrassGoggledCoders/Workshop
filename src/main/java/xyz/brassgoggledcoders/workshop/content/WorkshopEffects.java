package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.api.IStackingEffect;

import java.util.ArrayList;
import java.util.List;

public class WorkshopEffects {
    private static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Workshop.MOD_ID);

    public static final RegistryObject<Effect> STINKY = EFFECTS.register("stinky", StinkyEffect::new);
    public static final RegistryObject<Effect> INEBRIATED = EFFECTS.register("inebriated", InebriatedEffect::new);
    public static final RegistryObject<Effect> DRUNK = EFFECTS.register("drunk", DrunkEffect::new);
    public static final RegistryObject<Effect> MELLOW = EFFECTS.register("mellow", () -> new DummyEffect(EffectType.BENEFICIAL, 0));

    public static void register(IEventBus modBus) {
        EFFECTS.register(modBus);
    }

    private static class DummyEffect extends Effect {
        public DummyEffect(EffectType type, int color) {
            super(type, color);
        }
    }

    private static class StinkyEffect extends Effect {
        public StinkyEffect() {
            super(EffectType.HARMFUL, 13158600);
        }

        @Override
        public List<ItemStack> getCurativeItems() {
            ArrayList<ItemStack> ret = new ArrayList<>();
            ret.add(new ItemStack(WorkshopItems.SOAP.get()));
            return ret;
        }
    }

    private static class InebriatedEffect extends Effect implements IStackingEffect {
        public InebriatedEffect() {
            super(EffectType.NEUTRAL, 13158600);
        }

        @Override
        public List<ItemStack> getCurativeItems() {
            ArrayList<ItemStack> ret = new ArrayList<>();
            ret.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER));
            return ret;
        }

        @Override
        public int getMaxLevel() {
            return 5;
        }

        @Override
        public EffectInstance getPostMaxEffect() {
            return new EffectInstance(WorkshopEffects.DRUNK.get(), Integer.MAX_VALUE);
        }
    }

    private static class DrunkEffect extends Effect {
        public DrunkEffect() {
            super(EffectType.HARMFUL, 13158600);
        }

        @Override
        public List<ItemStack> getCurativeItems() {
            return new ArrayList<>();
        }
    }
}
