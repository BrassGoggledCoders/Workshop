package xyz.brassgoggledcoders.workshop.network;

import com.hrznstudio.titanium.network.CompoundSerializableDataHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.util.RangedItemStack;

public class WorkshopPacketHandler {

    //READ then WRITE
    static {
        CompoundSerializableDataHandler.map(ItemStack[].class, (buf) -> {
            ItemStack[] stacks = new ItemStack[buf.readInt()];
            for(int i = 0; i < stacks.length; i++) {
                stacks[i] = buf.readItemStack();
            }
            return stacks;
        }, (buf, itemStacks) -> {
            buf.writeInt(itemStacks.length);
            for(ItemStack stack : itemStacks) {
                buf.writeItemStack(stack);
            }
        });
        CompoundSerializableDataHandler.map(TileEntityType.class, (buf) -> ForgeRegistries.TILE_ENTITIES.getValue(buf.readResourceLocation()),
                (buf, tileEntityType) -> buf.writeResourceLocation(tileEntityType.getRegistryName()));
        CompoundSerializableDataHandler.map(RangedItemStack.class, (buf) -> new RangedItemStack(buf.readItemStack(), buf.readInt(), buf.readInt()), ((buf, rangedItemStack) -> {
            buf.writeItemStack(rangedItemStack.stack);
            buf.writeInt(rangedItemStack.min);
            buf.writeInt(rangedItemStack.max);
        }));
        CompoundSerializableDataHandler.map(RangedItemStack[].class,(buf) -> {
            RangedItemStack[] stacks = new RangedItemStack[buf.readInt()];
            for(int i = 0; i < stacks.length; i++) {
                stacks[i] = new RangedItemStack(buf.readItemStack(), buf.readInt(), buf.readInt());
            }
            return stacks;
        }, ((buf, rangedItemStacks) -> {
            buf.writeInt(rangedItemStacks.length);
            for(RangedItemStack rangedItemStack : rangedItemStacks) {
                buf.writeItemStack(rangedItemStack.stack);
                buf.writeInt(rangedItemStack.min);
                buf.writeInt(rangedItemStack.max);
            }
        }));
    }

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Workshop.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        //INSTANCE.registerMessage(id++, UpdateChalkPacket.class, UpdateChalkPacket::encode, UpdateChalkPacket::decode, UpdateChalkPacket::handle);
    }
}
