package network.manu.manumod.items;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import network.manu.manumod.ManuMod;

public class ItemEnderiteSword extends ItemSword {
    public static final String ID = "enderite_sword";

    public ItemEnderiteSword(ToolMaterial material) {
        super(material);
        this.setMaxDamage(material.getMaxUses());
        setUnlocalizedName(ID);
        setTextureName(ManuMod.TEXTURE_PREFIX + ID);
        setMaxStackSize(1);
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return ManuMod.itemEnderiteIngot == par2ItemStack.getItem() || super.getIsRepairable(par1ItemStack, par2ItemStack);
    }
}
