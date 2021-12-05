package xyz.brassgoggledcoders.workshop.content;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.block.SeasoningBarrelBlock;
import xyz.brassgoggledcoders.workshop.blockentity.SeasoningBarrelBlockEntity;

public class WorkshopBlocks {
    public static final BlockEntry<SeasoningBarrelBlock> SEASONING_BARREL = Workshop.getRegistrate()
            .object("seasoning_barrel")
            .block(SeasoningBarrelBlock::new)
            .initialProperties(Material.WOOD)
            .properties(AbstractBlock.Properties::noOcclusion)
            .blockstate((context, provider) -> provider.directionalBlock(
                    context.get(),
                    provider.models()
                            .getExistingFile(provider.blockTexture(context.get()))
            ))
            .tileEntity(SeasoningBarrelBlockEntity::new)
            .build()
            .item()
            .build()
            .register();

    public static void setup() {

    }
}
