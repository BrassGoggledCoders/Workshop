package xyz.brassgoggledcoders.workshop.content;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import xyz.brassgoggledcoders.workshop.Workshop;

public class WorkshopItems {

    //TODO: Allow Eating Speed up
    public static final ItemEntry<Item> PICKLE = Workshop.getRegistrate()
            .object("pickle")
            .item(Item::new)
            .properties(properties -> properties.food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationMod(0.5F)
                    .alwaysEat()
                    .fast()
                    .effect(() -> new MobEffectInstance(MobEffects.HEAL, 2), 0.1F)
                    .effect(() -> new MobEffectInstance(MobEffects.BAD_OMEN, 1), 0.01F)
                    .build()
            ))
            .register();

    public static void setup() {

    }
}
