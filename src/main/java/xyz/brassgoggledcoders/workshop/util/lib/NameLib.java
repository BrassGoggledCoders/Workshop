package xyz.brassgoggledcoders.workshop.util.lib;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import xyz.brassgoggledcoders.workshop.blocks.alembic.AlembicTile;

import static xyz.brassgoggledcoders.workshop.WorkShop.MOD_ID;

@ObjectHolder(MOD_ID)
public class NameLib
{
    //Icon
    @ObjectHolder(MOD_ID + ":workicon")
    public static final Item workicon = null;


    //Ingredients
    @ObjectHolder(MOD_ID + ":salt")
    public static final Item salt = null;

    @ObjectHolder(MOD_ID + ":ash")
    public static final Item ash = null;

    @ObjectHolder(MOD_ID + ":silt")
    public static final Item silt = null;

    @ObjectHolder(MOD_ID + ":tea")
    public static final Item tea = null;

    @ObjectHolder(MOD_ID + ":medicinalroots")
    public static final Item medicinalroots = null;

    @ObjectHolder(MOD_ID + ":chalk")
    public static final Item chalk = null;


    //Alembic Outputs

    @ObjectHolder(MOD_ID + ":adhesive_oils")
    public static final Item adhesive_oils = null;

    @ObjectHolder(MOD_ID + ":distilled_water")
    public static final Item distilled_water = null;

    @ObjectHolder(MOD_ID + ":tanin")
    public static final Item tanin = null;

    //Blocks

    @ObjectHolder(MOD_ID + ":alembic")
    public static final Block alembicblock = null;


    //TileEntities

    //Alembic
    @ObjectHolder(MOD_ID + ":alembic")
    public static final TileEntityType<AlembicTile> ALEMBICTILE = null;






    //


}
