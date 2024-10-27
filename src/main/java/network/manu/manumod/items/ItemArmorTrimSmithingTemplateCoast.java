package network.manu.manumod.items;

import net.minecraft.item.Item;
import network.manu.manumod.ManuMod;

public class ItemArmorTrimSmithingTemplateCoast extends Item {
    public static final String ID = "armor_trim_smithing_template_coast";

    public ItemArmorTrimSmithingTemplateCoast() {
        setUnlocalizedName(ID);
        setTextureName(ManuMod.TEXTURE_PREFIX + ID);
    }
}
