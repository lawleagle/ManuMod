package network.manu.manumod.items;

import network.manu.manumod.ManuMod;
import net.minecraft.item.Item;

public class ItemEnderiteScrap extends Item {
    public static final String ID = "enderite_scrap";

    public ItemEnderiteScrap() {
        setUnlocalizedName(ID);
        setTextureName(ManuMod.TEXTURE_PREFIX + ID);
    }
}

