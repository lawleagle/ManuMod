package network.manu.manumod.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import network.manu.manumod.ManuMod;

public abstract class ItemAlloyArmorBase extends ItemArmor implements ISpecialArmor {
    public ItemAlloyArmorBase(ItemArmor.ArmorMaterial material, int type) {
        super(material, 0, type);
        setMaxStackSize(1);
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return ManuMod.itemPartAlloy == par2ItemStack.getItem() || super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

    @Override
    public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        ItemAlloyArmorBase armorItem = (ItemAlloyArmorBase) armor.getItem();
        return new ISpecialArmor.ArmorProperties(0, source.isUnblockable() ? 0 : armorItem.damageReduceAmount / 25D, armorItem.getMaxDamage() + 1 - armor.getItemDamage());
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return this.damageReduceAmount;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        if (!source.isUnblockable())
            if (!source.isFireDamage() && getUnlocalizedName().contains("alloy"))
                stack.damageItem(damage, entity);
    }
}
