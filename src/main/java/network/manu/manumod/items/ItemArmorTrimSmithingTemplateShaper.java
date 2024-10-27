package network.manu.manumod.items;

import net.minecraft.item.Item;
import network.manu.manumod.ManuMod;

public class ItemArmorTrimSmithingTemplateShaper extends Item {
    public static final String ID = "armor_trim_smithing_template_shaper";

    public ItemArmorTrimSmithingTemplateShaper() {
        setUnlocalizedName(ID);
        setTextureName(ManuMod.TEXTURE_PREFIX + ID);
    }
}
