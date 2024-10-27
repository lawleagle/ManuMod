package network.manu.manumod.blocks;

import network.manu.manumod.ManuMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockEnderite extends Block {
    public static String ID = "enderite_block";

    public BlockEnderite() {
        super(Material.iron);
        setHarvestLevel("pickaxe", 4);
        setHardness(66.0F);
        setResistance(1200F);
//        setStepSound(new SoundType(ID, 1.0f, 1.0f));
        setStepSound(Block.soundTypeMetal);
        setBlockTextureName(ManuMod.TEXTURE_PREFIX + ID);
        setBlockName(ID);
    }

    @Override
    public boolean isBeaconBase(IBlockAccess world, int x, int y, int z, int bX, int bY, int bZ) {
        return true;
    }

}
