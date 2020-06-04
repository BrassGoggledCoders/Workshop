package xyz.brassgoggledcoders.workshop.network;

import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.brassgoggledcoders.workshop.tileentity.ChalkWritingTileEntity;

import java.util.function.Supplier;

public class UpdateChalkPacket {

    public BlockPos pos;
    public String[] lines;

    public UpdateChalkPacket(BlockPos pos, String... lines) {
        this.pos = pos;
        this.lines = lines;
    }

    public static void encode(UpdateChalkPacket packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
        for (String string : packet.lines) {
            buffer.writeString(string);
        }
    }

    public static UpdateChalkPacket decode(PacketBuffer packetBuffer) {
        return new UpdateChalkPacket(packetBuffer.readBlockPos(), packetBuffer.readString(), packetBuffer.readString(), packetBuffer.readString(), packetBuffer.readString());
    }

    public static void handle(UpdateChalkPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = ctx.get().getSender().getServerWorld();
            BlockPos blockpos = msg.pos;
            TileEntity te = world.getTileEntity(blockpos);
            if (world.isBlockLoaded(blockpos) && te instanceof ChalkWritingTileEntity) {
                ChalkWritingTileEntity chalkWritingTileEntity = (ChalkWritingTileEntity) te;
                for (int i = 0; i < 4; i++) {
                    chalkWritingTileEntity.setText(i, new StringTextComponent(TextFormatting.getTextWithoutFormattingCodes(msg.lines[i])));
                }
                chalkWritingTileEntity.markDirty();
                BlockState state = world.getBlockState(blockpos);
                world.notifyBlockUpdate(blockpos, state, state, 3);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
