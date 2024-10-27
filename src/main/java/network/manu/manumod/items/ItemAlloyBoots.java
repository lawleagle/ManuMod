package network.manu.manumod.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import network.manu.manumod.ManuMod;

public class ItemAlloyBoots extends ItemAlloyArmorBase {
    public static final String ID = "alloy_boots";

    public ItemAlloyBoots(ArmorMaterial material) {
        super(material, 3);
        setUnlocalizedName(ID);
        setTextureName(ManuMod.TEXTURE_PREFIX + ID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return ManuMod.TEXTURE_PREFIX + "textures/models/armor/alloy_layer_1.png";
    }
}
