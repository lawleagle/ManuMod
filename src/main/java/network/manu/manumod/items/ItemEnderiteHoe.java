package network.manu.manumod.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import network.manu.manumod.ManuMod;

public class ItemEnderiteHoe extends ItemHoe {
    public static final String ID = "enderite_hoe";

    public ItemEnderiteHoe(Item.ToolMaterial material) {
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
