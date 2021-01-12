package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.api.IStackingEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WorkshopEffects {
    private static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Workshop.MOD_ID);

    public static final RegistryObject<Effect> STINKY = EFFECTS.register("stinky", StinkyEffect::new);
    public static final RegistryObject<Effect> INEBRIATED = EFFECTS.register("inebriated", InebriatedEffect::new);
    public static final RegistryObject<Effect> DRUNK = EFFECTS.register("drunk", DrunkEffect::new);
    public static final RegistryObject<Effect> MELLOW = EFFECTS.register("mellow", () -> new DummyEffect(EffectType.BENEFICIAL, 0));
    public static final RegistryObject<Effect> RUSH = EFFECTS.register("rush", () -> new RushEffect()
            .addAttributesModifier(Attributes.ATTACK_DAMAGE, "da9d143d-6282-460f-b0e3-748b0ac1675c", 0.0D, AttributeModifier.Operation.ADDITION)
            .addAttributesModifier(Attributes.ATTACK_SPEED, "14ccf670-e32a-4e62-a17a-0f835eaf5879", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<Effect> WIRED = EFFECTS.register("wired", () -> new DummyEffect(EffectType.NEUTRAL, 5000)
            .addAttributesModifier(Attributes.ATTACK_SPEED, "52e6b391-5ec8-434e-b209-e22d0d3c9dd5", 0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static void register(IEventBus modBus) {
        EFFECTS.register(modBus);
    }

    private static class DummyEffect extends Effect {
        public DummyEffect(EffectType type, int color) {
            super(type, color);
        }
    }

    private static class RushEffect extends Effect implements IStackingEffect {
        public RushEffect() {
            super(EffectType.NEUTRAL, 100);
        }

        @Override
        public double getAttributeModifierAmount(int amplifier, @Nonnull AttributeModifier modifier) {
            return 4 * (double) (amplifier + 1);
        }

        @Override
        public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, @Nonnull LivingEntity entityLivingBaseIn, int amplifier, double health) {
            if (entityLivingBaseIn.isSprinting()) {
                entityLivingBaseIn.setMotionMultiplier(entityLivingBaseIn.getBlockState(), new Vector3d(amplifier + 1, 0, amplifier + 1));
            }
        }

        @Override
        public int getMaxLevel() {
            return 5;
        }

        @Override
        public EffectInstance getPostMaxEffect() {
            return new EffectInstance(WorkshopEffects.WIRED.get(), 3000);
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
