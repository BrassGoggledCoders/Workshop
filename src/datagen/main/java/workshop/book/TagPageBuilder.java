package workshop.book;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.patchouliprovider.AbstractPageBuilder;
import xyz.brassgoggledcoders.patchouliprovider.EntryBuilder;
import xyz.brassgoggledcoders.workshop.Workshop;

public class TagPageBuilder extends AbstractPageBuilder<TagPageBuilder> {

    private final ResourceLocation tagID;
    private String title = "";
    private String registry = "block";

    protected TagPageBuilder(EntryBuilder parent, ResourceLocation tagID) {
        super(Workshop.MOD_ID + ":tag", parent);
        this.tagID = tagID;
    }

    public TagPageBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public TagPageBuilder setRegistry(String registry) {
        this.registry = registry;
        return this;
    }

    @Override
    protected void serialize(JsonObject jsonObject) {
        jsonObject.addProperty("tag", tagID.toString());
        jsonObject.addProperty("registry", registry);
        jsonObject.addProperty("title", title);
    }
}
