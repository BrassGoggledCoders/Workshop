package xyz.brassgoggledcoders.workshop.api;

import net.minecraft.fluid.Fluid;

import java.util.HashMap;
import java.util.Map;

public class WorkshopAPI {
    private static final Map<Fluid, IDrinkableFluidBehaviour> DRINKABLE_FLUID_BEHAVIORS = new HashMap<>();

    //TODO Add IMC listener
    public static void addDrinkableFluidBehaviour(Fluid fluid, IDrinkableFluidBehaviour behaviour) {
        DRINKABLE_FLUID_BEHAVIORS.put(fluid, behaviour);
    }

    public static Map<Fluid, IDrinkableFluidBehaviour> getDrinkableFluidBehaviors() {
        return DRINKABLE_FLUID_BEHAVIORS;
    }
}
