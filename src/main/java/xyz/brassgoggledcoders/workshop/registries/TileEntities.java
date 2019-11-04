package xyz.brassgoggledcoders.workshop.registries;


import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.blocks.alembic.AlembicTile;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;
import static xyz.brassgoggledcoders.workshop.registries.Blocks.ALEMBICBLOCK;

public class TileEntities {
    private static final DeferredRegister<TileEntityType<?>> TILEENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, MOD_ID);

    public static final RegistryObject<TileEntityType<?>> ALEMBICTILE = TILEENTITIES.register("alembic", () -> TileEntityType.Builder.create(AlembicTile::new, ALEMBICBLOCK.get()).build(null));

    public static void register(IEventBus bus) {
        TILEENTITIES.register(bus);
    }
}
