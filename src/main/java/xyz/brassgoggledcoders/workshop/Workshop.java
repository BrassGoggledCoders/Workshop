package xyz.brassgoggledcoders.workshop;

import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.workshop.recipes.*;
import xyz.brassgoggledcoders.workshop.registries.Items;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

//Main Class
@Mod(MOD_ID)
public class Workshop {
    public static final String MOD_ID = "workshop";

    private static final Logger LOGGER = LogManager.getLogger();

    public Workshop() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();


        Items.register(modBus);

        EventManager.mod(RegistryEvent.Register.class).filter(register -> register.getGenericType().equals(IRecipeSerializer.class))
                .process(register -> register.getRegistry().registerAll(AlembicRecipe.SERIALIZER, SpinningWheelRecipe.SERIALIZER, SeasoningBarrelRecipe.SERIALIZER, SinteringFurnaceRecipe.SERIALIZER, PressRecipes.SERIALIZER))
                .subscribe();

    }

}
