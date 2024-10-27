package network.manu.manumod.items;

import net.minecraft.item.Item;
import network.manu.manumod.ManuMod;

public class ItemArmorUpgradeSmithingTemplateNetherite extends Item {
    public static final String ID = "armor_upgrade_smithing_template_netherite";

    public ItemArmorUpgradeSmithingTemplateNetherite() {
        setUnlocalizedName(ID);
        setTextureName(ManuMod.TEXTURE_PREFIX + ID);
    }
}
