package network.manu.manumod.items;

import network.manu.manumod.ManuMod;
import net.minecraft.item.Item;

public class ItemEnderiteIngot extends Item {
    public static final String ID = "enderite_ingot";

    public ItemEnderiteIngot() {
        setUnlocalizedName(ID);
        setTextureName(ManuMod.TEXTURE_PREFIX + ID);
    }
}
