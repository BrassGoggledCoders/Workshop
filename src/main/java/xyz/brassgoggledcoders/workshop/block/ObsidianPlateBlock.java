package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.tileentity.ObsidianPlateTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ObsidianPlateBlock extends PressurePlateBlock {

    public ObsidianPlateBlock() {
        super(PressurePlateBlock.Sensitivity.EVERYTHING, Block.Properties.create(Material.ROCK));
    }

    @Override
    protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(pos);
        List<PlayerEntity> list = worldIn.getEntitiesWithinAABB(PlayerEntity.class, axisalignedbb);
        if (!list.isEmpty() && te instanceof ObsidianPlateTileEntity) {
            for (Entity entity : list) {
                if (!entity.doesEntityNotTriggerPressurePlate() && !((ObsidianPlateTileEntity) te).getPlayerNames().contains(entity.getUniqueID())) {
                    return 15;
                }
            }
        }
        return 0;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (!worldIn.isRemote && te instanceof ObsidianPlateTileEntity) {
            if (player.isCrouching()) {
                StringBuilder text = new StringBuilder();
                for (UUID component : ((ObsidianPlateTileEntity) te).getPlayerNames()) {
                    text.append(component.toString()).append(",");
                }
                player.sendStatusMessage(new StringTextComponent("Excluded Players: " + text.toString()), true);
                return ActionResultType.SUCCESS;
            }
            ItemStack heldItem = player.getHeldItem(handIn);
            if (Items.NAME_TAG.equals(heldItem.getItem())) {
                ((ObsidianPlateTileEntity) te).addPlayerName(PlayerEntity.getOfflineUUID(heldItem.getDisplayName().getString()));
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ObsidianPlateTileEntity();
    }
}
