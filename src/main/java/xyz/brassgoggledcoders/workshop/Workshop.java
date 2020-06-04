package xyz.brassgoggledcoders.workshop;

import com.hrznstudio.titanium.tab.TitaniumTab;
import net.minecraft.block.ComposterBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Foods;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.workshop.api.WorkshopAPI;
import xyz.brassgoggledcoders.workshop.api.impl.FoodFluidBehaviour;
import xyz.brassgoggledcoders.workshop.api.impl.PotionDrinkableFluidBehaviour;
import xyz.brassgoggledcoders.workshop.content.*;
import xyz.brassgoggledcoders.workshop.network.WorkshopPacketHandler;
import xyz.brassgoggledcoders.workshop.renderer.ChalkWritingTileEntityRenderer;
import xyz.brassgoggledcoders.workshop.renderer.PressTileEntityRenderer;
import xyz.brassgoggledcoders.workshop.renderer.SinteringTileEntityRenderer;

@Mod(Workshop.MOD_ID)
public class Workshop {
    public static final String MOD_ID = "workshop";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final ItemGroup ITEM_GROUP = new TitaniumTab(MOD_ID,
            () -> new ItemStack(WorkshopBlocks.SEASONING_BARREL.getBlock()));

    public Workshop() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::clientSetup);
        modBus.addListener(this::commonSetup);

        WorkshopMaterials.init();
        WorkshopRecipes.register(modBus);
        WorkshopFluids.register(modBus);
        WorkshopItems.register(modBus);
        WorkshopBlocks.register(modBus);
        WorkshopEffects.register(modBus);

        WorkshopPacketHandler.register();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WorkshopConfig.COMMON_SPEC);
    }

    @OnlyIn(Dist.CLIENT)
    private void clientSetup(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.PRESS.getTileEntityType(), PressTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.SINTERING_FURNACE.getTileEntityType(), SinteringTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.CHALK_WRITING.getTileEntityType(), ChalkWritingTileEntityRenderer::new);

        RenderTypeLookup.setRenderLayer(WorkshopBlocks.SINTERING_FURNACE.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WorkshopBlocks.PRESS.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WorkshopBlocks.ALEMBIC.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WorkshopBlocks.TEA_PLANT.getBlock(), RenderType.getCutout());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        WorkshopAPI.addDrinkableFluidBehaviour(WorkshopFluids.TEA.getFluid(), new PotionDrinkableFluidBehaviour(new EffectInstance(Effects.SPEED, 300)));
        WorkshopAPI.addDrinkableFluidBehaviour(WorkshopFluids.CIDER.getFluid(), new PotionDrinkableFluidBehaviour(new EffectInstance(Effects.STRENGTH, 200),
                new EffectInstance(Effects.NAUSEA, 100)));
        WorkshopAPI.addDrinkableFluidBehaviour(WorkshopFluids.APPLE_JUICE.getFluid(), new FoodFluidBehaviour(Foods.COD));
        //Just because you *can* doesn't mean you should...
        WorkshopAPI.addDrinkableFluidBehaviour(WorkshopFluids.ADHESIVE_OILS.getFluid(), new PotionDrinkableFluidBehaviour(new EffectInstance(Effects.SLOWNESS, 300)));
        WorkshopAPI.addDrinkableFluidBehaviour(WorkshopFluids.BRINE.getFluid(), new PotionDrinkableFluidBehaviour(new EffectInstance(Effects.NAUSEA, 100)));
        WorkshopAPI.addDrinkableFluidBehaviour(WorkshopFluids.HELLBLOOD.getFluid(), (stack, worldIn, entityLiving) -> {
            entityLiving.setFire(10);
        });
        ComposterBlock.registerCompostable(0.2F, WorkshopItems.ASH.get());
    }
}
