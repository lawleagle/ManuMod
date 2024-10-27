package network.manu.manumod.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import network.manu.manumod.ManuMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockEnderiteDebrisCracked extends Block {
    public static final String ID = "enderite_debris_cracked";
    public IIcon iconTop;

    public BlockEnderiteDebrisCracked() {
        super(Material.iron);
        setHarvestLevel("pickaxe", 4);
        setHardness(20.0F);
        setResistance(1200.0F);
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
        ManuMod.blockEnderiteDebrisCrackedRenderer.jsonModel.registerIcons(register);
        blockIcon = register.registerIcon(this.getTextureName() + "_side");
        iconTop = register.registerIcon(this.getTextureName() + "_top");
    }

    @Override
    public int getRenderType() {
        return ManuMod.ENDERITE_DEBRIS_CRACKED_RENDER_ID;
    }

}
