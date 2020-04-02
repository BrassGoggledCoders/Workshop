package xyz.brassgoggledcoders.workshop.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;

public interface GUITile {
    public ActionResultType onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit);
}
