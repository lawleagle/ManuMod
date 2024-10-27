package network.manu.manumod.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import network.manu.manumod.ManuMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockEnderiteDebris extends Block {
    public static final String ID = "enderite_debris";
    public IIcon iconTop;

    public BlockEnderiteDebris() {
        super(Material.iron);
        setHarvestLevel("pickaxe", 5);
        setHardness(-1.0F);
        setResistance(15.0F);
        setStepSound(Block.soundTypeStone);
        setBlockTextureName(ManuMod.TEXTURE_PREFIX + ID);
        setBlockName(ID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int p_149691_2_) {
        return side > 1 ? blockIcon : iconTop;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        blockIcon = register.registerIcon(this.getTextureName() + "_side");
        iconTop = register.registerIcon(this.getTextureName() + "_top");
    }

}
