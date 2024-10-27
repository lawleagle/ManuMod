package network.manu.manumod.worldgen;

import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import network.manu.manumod.ManuMod;

import java.util.Random;

public abstract class WorldGenerator {
    public static WorldGenMinable enderiteDebrisGenerator = new WorldGenMinable(ManuMod.blockEnderiteDebris, 3, Blocks.end_stone);

    public static void generateOre(WorldGenMinable generator, World world, Random random, int chunkX, int chunkZ, float chance, int minY, int maxY) {
		if (maxY <= 0 || minY < 0 || maxY < minY  || chance <= 0) return;

		for (int i = 0; i < (chance < 1 ? 1 : (int) chance); i++) {
            if (chance >= 1 || random.nextFloat() <= chance) {
                int xRand = (chunkX << 4) + random.nextInt(16);
                int yRand = MathHelper.getRandomIntegerInRange(random, minY, maxY);
                int zRand = (chunkZ << 4) + random.nextInt(16);

                generator.generate(world, random, xRand, yRand, zRand);
            }
        }
    }
}
