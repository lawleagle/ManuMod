package network.manu.manumod.worldgen;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import network.manu.manumod.ManuMod;

import java.util.Random;

public class EnderiteDebrisWorldGenerator implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.dimensionId == 1) {
            WorldGenerator.generateOre(WorldGenerator.enderiteDebrisGenerator, world, random, chunkX, chunkZ, 1, 8, 44);
        }
    }
}
