package workshop;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.EffectsChangedTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.command.FunctionObject;
import net.minecraft.data.AdvancementProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.api.capabilities.BottleCapabilityProvider;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopEffects;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;

public class WorkshopAdvancementProvider extends AdvancementProvider {
    private final Path OUTPUT;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public WorkshopAdvancementProvider(DataGenerator gen) {
        super(gen);
        OUTPUT = gen.getOutputFolder();
    }

    @Override
    public void act(@Nonnull DirectoryCache cache) {
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (adv) -> {
            if (!set.add(adv.getId())) {
                throw new IllegalStateException("Duplicate advancement " + adv.getId());
            } else {
                Path path1 = getPath(OUTPUT, adv);

                try {
                    IDataProvider.save(GSON, cache, adv.copy().serialize(), path1);
                } catch (IOException ioexception) {
                    Workshop.LOGGER.error("Couldn't save advancement {}", path1, ioexception);
                }

            }
        };

        new WorkshopAdvancements().accept(consumer);
    }

    public static class WorkshopAdvancements implements Consumer<Consumer<Advancement>> {
        public void accept(Consumer<Advancement> consumer) {
            Advancement root = Advancement.Builder.builder()
                    .withDisplay(WorkshopBlocks.BROKEN_ANVIL.getItem(),
                            new TranslationTextComponent("advancement.workshop.root"),
                            new TranslationTextComponent("advancement.workshop.root.desc"),
                            new ResourceLocation(""),
                            FrameType.TASK, true, true, false)
                    .withCriterion("anvil", InventoryChangeTrigger.Instance.forItems(Items.ANVIL))
                    .register(consumer, "workshop:main/root");
            Advancement bellows = advancement(root, WorkshopBlocks.BELLOWS.get(), "bellows", FrameType.TASK, true, true, false)
                    .withCriterion("bellows", InventoryChangeTrigger.Instance.forItems(WorkshopBlocks.BELLOWS.getItem()))
                    .register(consumer, "workshop:main/bellows");
            Advancement drying_basin = advancement(root, WorkshopBlocks.DRYING_BASIN.get(), "drying_basin", FrameType.TASK, true, true, false)
                    .withCriterion("drying_basin", InventoryChangeTrigger.Instance.forItems(WorkshopBlocks.DRYING_BASIN.getItem()))
                    .register(consumer, "workshop:main/drying_basin");
            Advancement scrap_bin = advancement(root, WorkshopBlocks.SCRAP_BIN.get(), "scrap_bin", FrameType.TASK, true, true, false)
                    .withCriterion("scrap_bin", InventoryChangeTrigger.Instance.forItems(WorkshopBlocks.SCRAP_BIN.getItem()))
                    .register(consumer, "workshop:main/scrap_bin");
            Advancement mortar = advancement(root, WorkshopBlocks.MORTAR.get(), "mortar", FrameType.TASK, true, true, false)
                    .withCriterion("mortar", InventoryChangeTrigger.Instance.forItems(WorkshopBlocks.MORTAR.getItem()))
                    .register(consumer, "workshop:main/mortar");
            Advancement seasoning_barrel = advancement(root, WorkshopBlocks.SEASONING_BARREL.get(), "seasoning_barrel", FrameType.TASK, true, true, false)
                    .withCriterion("seasoning_barrel", InventoryChangeTrigger.Instance.forItems(WorkshopBlocks.SEASONING_BARREL.getItem()))
                    .register(consumer, "workshop:main/seasoning_barrel");
            Advancement drunk = advancement(seasoning_barrel, BottleCapabilityProvider.getFilledBottle(new FluidStack(WorkshopFluids.MEAD.getFluid(), WorkshopFluids.BOTTLE_VOLUME)),
                    "drunk", FrameType.GOAL, true, true, true)
                    .withCriterion("drunk", EffectsChangedTrigger.Instance.forEffect(new MobEffectsPredicate(Collections.singletonMap(WorkshopEffects.DRUNK.get(), new MobEffectsPredicate.InstancePredicate()))))
                    .register(consumer, "workshop:main/drunk");
        }
    }

    private static Path getPath(Path pathIn, Advancement advancementIn) {
        return pathIn.resolve("data/" + advancementIn.getId().getNamespace() + "/advancements/" + advancementIn.getId().getPath() + ".json");
    }

    protected static Advancement.Builder advancement(Advancement parent, IItemProvider display, String name, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return advancement(parent, new ItemStack(display), name, frame, showToast, announceToChat, hidden);
    }

    protected static Advancement.Builder advancement(Advancement parent, ItemStack display, String name, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return Advancement.Builder.builder().withParent(parent).withDisplay(display,
                new TranslationTextComponent(String.format("advancement.%s.%s", Workshop.MOD_ID, name)),
                new TranslationTextComponent(String.format("advancement.%s.%s.desc", Workshop.MOD_ID, name)),
                null, frame, showToast, announceToChat, hidden);
    }

    protected static AdvancementRewards reward(ResourceLocation loot) {
        return new AdvancementRewards(0, new ResourceLocation[]{loot}, new ResourceLocation[0], FunctionObject.CacheableFunction.EMPTY);
    }
}
