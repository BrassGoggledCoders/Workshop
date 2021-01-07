package xyz.brassgoggledcoders.workshop;

import com.hrznstudio.titanium.network.CompoundSerializableDataHandler;
import com.hrznstudio.titanium.tab.TitaniumTab;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.Foods;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.workshop.api.WorkshopAPI;
import xyz.brassgoggledcoders.workshop.api.impl.FoodFluidBehaviour;
import xyz.brassgoggledcoders.workshop.api.impl.PotionDrinkableFluidBehaviour;
import xyz.brassgoggledcoders.workshop.content.*;
import xyz.brassgoggledcoders.workshop.util.RangedItemStack;

@Mod(Workshop.MOD_ID)
public class Workshop {
    public static final String MOD_ID = "workshop";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final ItemGroup ITEM_GROUP = new TitaniumTab(MOD_ID,
            () -> new ItemStack(WorkshopBlocks.SEASONING_BARREL.getBlock()));

    public static final String SCRAP_BAG_DESC = "description.jei.scrap_bag";

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

    public Workshop() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::commonSetup);
        WorkshopFluids.register(modBus);
        WorkshopItems.register(modBus);
        WorkshopBlocks.register(modBus);
        WorkshopRecipes.register(modBus);
        WorkshopEffects.register(modBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WorkshopConfig.COMMON_SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        WorkshopAPI.addDrinkableFluidBehaviour(WorkshopFluids.TEA.getFluid(), new PotionDrinkableFluidBehaviour(new EffectInstance(WorkshopEffects.RUSH.get(), 3000)));
        WorkshopAPI.addDrinkableFluidBehaviour(WorkshopFluids.APPLE_JUICE.getFluid(), new FoodFluidBehaviour(Foods.COD));
        WorkshopAPI.addDrinkableFluidBehaviour(WorkshopFluids.MEAD.getFluid(), new PotionDrinkableFluidBehaviour(new EffectInstance(WorkshopEffects.INEBRIATED.get(), 1000),
                new EffectInstance(WorkshopEffects.MELLOW.get(), 500)));
        //Just because you *can* doesn't mean you should...
        WorkshopAPI.addDrinkableFluidBehaviour(WorkshopFluids.ADHESIVE_OILS.getFluid(), new PotionDrinkableFluidBehaviour(new EffectInstance(Effects.SLOWNESS, 300)));
        WorkshopAPI.addDrinkableFluidBehaviour(WorkshopFluids.BRINE.getFluid(), new PotionDrinkableFluidBehaviour(new EffectInstance(Effects.NAUSEA, 100)));
        WorkshopAPI.addDrinkableFluidBehaviour(WorkshopFluids.HELLBLOOD.getFluid(), (stack, worldIn, entityLiving) -> {
            entityLiving.setFire(10);
        });
        event.enqueueWork(() -> {
            ComposterBlock.registerCompostable(0.2F, WorkshopItems.ASH.get());
            ComposterBlock.registerCompostable(0.05F, WorkshopBlocks.TEA_PLANT.getItem());
            ComposterBlock.registerCompostable(0.1F, WorkshopItems.TEA_LEAVES.get());
        });
    }
}
