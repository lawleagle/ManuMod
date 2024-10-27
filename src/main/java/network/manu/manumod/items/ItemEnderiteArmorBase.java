package network.manu.manumod.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import network.manu.manumod.ManuMod;

public abstract class ItemEnderiteArmorBase extends ItemArmor implements ISpecialArmor {

    public ItemEnderiteArmorBase(ArmorMaterial material, int type) {
        super(material, 0, type);
        setMaxStackSize(1);
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return ManuMod.itemEnderiteIngot == par2ItemStack.getItem() || super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        ItemEnderiteArmorBase armorItem = (ItemEnderiteArmorBase) armor.getItem();
        return new ArmorProperties(0, source.isUnblockable() ? 0 : armorItem.damageReduceAmount / 25D, armorItem.getMaxDamage() + 1 - armor.getItemDamage());
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return this.damageReduceAmount;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        if (!source.isUnblockable())
            if (!source.isFireDamage() && getUnlocalizedName().contains("enderite"))
                stack.damageItem(damage, entity);
    }
}
