package xyz.brassgoggledcoders.workshop.content;

import com.hrznstudio.titanium.block.tile.MachineTile;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.blocks.MachineTileContainer;

public class WorkshopContainers {
    private static final DeferredRegister<ContainerType<?>> CONTAINERS =
            new DeferredRegister<>(ForgeRegistries.CONTAINERS, Workshop.MOD_ID);

    public static final RegistryObject<ContainerType<MachineTileContainer<?>>> MACHINE = CONTAINERS.register("machine",
            () -> IForgeContainerType.create(MachineTileContainer::create));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}