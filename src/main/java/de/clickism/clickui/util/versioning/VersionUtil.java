package de.clickism.clickui.util.versioning;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

//? if >= 1.21.1 {
/*import net.minecraft.util.StringUtil;
*///?} else {
import net.minecraft.SharedConstants;
//?}

/**
 * Utility class for version-specific operations.
 */
@ApiStatus.Internal
public class VersionUtil {
    /**
     * Applies the default filter to a string, removing invalid characters.
     *
     * @param string    the string to filter
     * @param multiline whether to allow multiline strings
     * @return the filtered string
     */
    public static String applyDefaultFilter(String string, boolean multiline) {
        // Remove invalid characters
        //? if >= 1.21.1 {
        /*return StringUtil.filterText(string, multiline);
        *///?} else {
        return SharedConstants.filterText(string, multiline);
        //?}
    }

    public static OffsetTexture buttonTexture(boolean disabled) {
        //? if >= 1.21.1 {
        /*var texture = disabled
            ? ResourceLocation.withDefaultNamespace("widget/button_disabled")
            : ResourceLocation.withDefaultNamespace("widget/button");
        return new OffsetTexture(texture, 0);
        *///?} else {
        int textureY = disabled ? 46 : 66;
        var texture = new ResourceLocation("textures/gui/widgets.png");
        return new OffsetTexture(texture, textureY);
        //?}
    }

    public static OffsetTexture checkboxTexture(boolean checked) {
        //? if >= 1.21.1 {
        /*var texture = checked
            ? ResourceLocation.withDefaultNamespace("widget/checkbox_selected")
            : ResourceLocation.withDefaultNamespace("widget/checkbox");
        return new OffsetTexture(texture, 0);
        *///?} else {
        var texture = new ResourceLocation("textures/gui/checkbox.png");
        int textureY = checked ? 20 : 0;
        return new OffsetTexture(texture, textureY);
        //?}
    }

    public record OffsetTexture(
        ResourceLocation texture,
        int textureY
    ) {
    }
}
