package network.manu.manumod.eventhandlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.event.world.ExplosionEvent;
import network.manu.manumod.ManuMod;
import network.manu.manumod.blocks.BlockEnderiteDebris;

public class EventBusEventHandler {

    @SubscribeEvent
    public void onExplosionEvent(ExplosionEvent event) {
        for (int i = event.explosion.affectedBlockPositions.size() - 1; i >= 0; i--) {
            ChunkPosition blockPosition = event.explosion.affectedBlockPositions.get(i);
            if (event.world.getBlock(blockPosition.chunkPosX, blockPosition.chunkPosY, blockPosition.chunkPosZ) instanceof BlockEnderiteDebris) {

                event.world.setBlock(blockPosition.chunkPosX, blockPosition.chunkPosY, blockPosition.chunkPosZ, ManuMod.blockEnderiteDebrisCracked);
                event.explosion.affectedBlockPositions.remove(i);
            }
        }
    }
}

