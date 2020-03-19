package xyz.brassgoggledcoders.workshop.util;

import static xyz.brassgoggledcoders.workshop.registries.Items.WORKICON;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;


public class WorkGroup extends ItemGroup {

    public static final WorkGroup instance = new WorkGroup(ItemGroup.GROUPS.length, "workshop");

    private WorkGroup(int index, String label)
    {
        super(index, label);
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(WORKICON.get());
    }

}
