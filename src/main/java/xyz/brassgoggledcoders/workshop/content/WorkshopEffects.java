package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;

import java.util.ArrayList;
import java.util.List;

public class WorkshopEffects {
    private static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Workshop.MOD_ID);

    public static final RegistryObject<Effect> STINKY = EFFECTS.register("stinky", StinkyEffect::new);

    public static void register(IEventBus modBus) {
        EFFECTS.register(modBus);
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
}
