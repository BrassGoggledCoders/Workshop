package xyz.brassgoggledcoders.workshop;


import com.tterrag.registrate.Registrate;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.fml.common.Mod;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;

import javax.annotation.Nonnull;

@Mod(Workshop.ID)
public class Workshop {
    public static final String ID = "workshop";

    private static final NonNullLazy<Registrate> REGISTRATE = NonNullLazy.of(() -> Registrate.create(ID)
            .itemGroup(() -> new ItemGroup(ID) {
                @Override
                @Nonnull
                public ItemStack makeIcon() {
                    return WorkshopBlocks.SEASONING_BARREL.asStack();
                }
            })
    );

    public Workshop() {
        WorkshopBlocks.setup();
    }

    public static Registrate getRegistrate() {
        return REGISTRATE.get();
    }
}
