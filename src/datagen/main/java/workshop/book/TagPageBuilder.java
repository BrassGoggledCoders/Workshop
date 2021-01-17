package workshop.book;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.patchouliprovider.AbstractPageBuilder;
import xyz.brassgoggledcoders.patchouliprovider.EntryBuilder;
import xyz.brassgoggledcoders.workshop.Workshop;

public class TagPageBuilder extends AbstractPageBuilder<TagPageBuilder> {

    private final ResourceLocation tagID;
    private String title;

    protected TagPageBuilder(EntryBuilder parent, ResourceLocation tagID) {
        super(Workshop.MOD_ID + ":tag", parent);
        this.tagID = tagID;
    }

    public TagPageBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    protected void serialize(JsonObject jsonObject) {
        jsonObject.addProperty("tag", tagID.toString());
        //TODO
        jsonObject.addProperty("registry", "block");
        jsonObject.addProperty("title", title);
    }
}
