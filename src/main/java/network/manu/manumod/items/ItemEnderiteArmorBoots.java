package network.manu.manumod.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import network.manu.manumod.ManuMod;

public class ItemEnderiteArmorBoots extends ItemEnderiteArmorBase {
    public static final String ID = "enderite_boots";

    public ItemEnderiteArmorBoots(ArmorMaterial material) {
        super(material, 3);
        setUnlocalizedName(ID);
        setTextureName(ManuMod.TEXTURE_PREFIX + ID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return ManuMod.TEXTURE_PREFIX + "textures/models/armor/enderite_layer_1.png";
    }
}
