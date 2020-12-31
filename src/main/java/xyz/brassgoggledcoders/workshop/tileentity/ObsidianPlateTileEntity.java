package xyz.brassgoggledcoders.workshop.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ObsidianPlateTileEntity extends TileEntity {
    private final List<UUID> playerNames = new ArrayList<>();

    public ObsidianPlateTileEntity() {
        super(WorkshopBlocks.OBSIDIAN_PLATE.getTileEntityType());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        int i = 0;
        playerNames.forEach(name -> compound.putUniqueId("name" + i, name));
        compound.putInt("size", playerNames.size());
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        for (int i = 0; i < compound.getInt("size"); i++) {
            playerNames.set(i, compound.getUniqueId("name" + i));
        }
    }

    public void addPlayerName(UUID name) {
        if (!playerNames.contains(name)) {
            playerNames.add(name);
        }
    }

    public List<UUID> getPlayerNames() {
        return playerNames;
    }
}
