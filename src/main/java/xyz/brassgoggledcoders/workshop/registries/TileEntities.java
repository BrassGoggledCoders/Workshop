package xyz.brassgoggledcoders.workshop.registries;


import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.blocks.alembic.AlembicTile;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;
import static xyz.brassgoggledcoders.workshop.registries.Blocks.*;

public class TileEntities {
    /*
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, MOD_ID);

    public static final RegistryObject<TileEntityType<?>> ALEMBIC_TILE = TILE_ENTITIES.register("alembic", () -> TileEntityType.Builder.create(AlembicTile::new, ALEMBIC_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<?>> PRESS_TILE = TILE_ENTITIES.register("press", () -> TileEntityType.Builder.create(AlembicTile::new, PRESS_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<?>> SEASONING_BARREL_TILE = TILE_ENTITIES.register("seaoning_barrel", () -> TileEntityType.Builder.create(AlembicTile::new, SEASONING_BARREL_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<?>> SINTERING_FURNACE_TILE = TILE_ENTITIES.register("sintering_furnace", () -> TileEntityType.Builder.create(AlembicTile::new, SINTERING_FURNACE_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<?>> SPINNING_WHEEL_TILE = TILE_ENTITIES.register("spinning_wheel", () -> TileEntityType.Builder.create(AlembicTile::new, SPINNING_WHEEL_BLOCK.get()).build(null));

    public static void register(IEventBus bus) {
        TILE_ENTITIES.register(bus);
    }

    */
}
