package network.manu.manumod.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import network.manu.manumod.ManuMod;
import network.manu.manumod.manulib.JsonModel;
import network.manu.manumod.manulib.V2;
import network.manu.manumod.manulib.V3;

import java.util.HashMap;
import java.util.Map;

public class BlockEnderiteDebrisCrackedRenderer extends BlockRendererBase {
    public JsonModel jsonModel;

    public BlockEnderiteDebrisCrackedRenderer(int renderId) {
        super(renderId);

        Map<String, JsonModel.Element.Face> faces = new HashMap<>();
        faces.put("north",
            new JsonModel.Element.Face(
                new V2(0, 0),
                new V2(16, 16),
                "#0",
                0,
                ""
            )
        );
        faces.put("east",
            new JsonModel.Element.Face(
                new V2(0, 0),
                new V2(16, 16),
                "#0",
                0,
                ""
            )
        );
        faces.put("south",
            new JsonModel.Element.Face(
                new V2(0, 0),
                new V2(16, 16),
                "#0",
                0,
                ""
            )
        );
        faces.put("west",
            new JsonModel.Element.Face(
                new V2(0, 0),
                new V2(16, 16),
                "#0",
                0,
                ""
            )
        );
        faces.put("up",
            new JsonModel.Element.Face(
                new V2(0, 0),
                new V2(16, 16),
                "#1",
                0,
                ""
            )
        );
        faces.put("down",
            new JsonModel.Element.Face(
                new V2(0, 0),
                new V2(16, 16),
                "#1",
                0,
                ""
            )
        );

        JsonModel.Element element = new JsonModel.Element(
            new V3(0, 0, 0),
            new V3(16, 16, 16),
            faces
        );

        Map<String, JsonModel.Element.Face> faces2 = new HashMap<>();
        faces2.put("north",
            new JsonModel.Element.Face(
                new V2(11, 3),
                new V2(14, 4),
                "#0",
                0,
                ""
            )
        );
        faces2.put("east",
            new JsonModel.Element.Face(
                new V2(3, 0),
                new V2(4, 1),
                "#0",
                0,
                ""
            )
        );
        faces2.put("west",
            new JsonModel.Element.Face(
                new V2(2, 0),
                new V2(3, 1),
                "#0",
                0,
                ""
            )
        );
        faces2.put("up",
            new JsonModel.Element.Face(
                new V2(2, 1),
                new V2(4, 2),
                "#1",
                0,
                ""
            )
        );
        faces2.put("down",
            new JsonModel.Element.Face(
                new V2(2, 2),
                new V2(4, 3),
                "#1",
                0,
                ""
            )
        );

        JsonModel.Element element2 = new JsonModel.Element(
            new V3(1, 11, -1),
            new V3(4, 12, 0),
            faces2
        );

        Map<String, String> textures = new HashMap<>();
        textures.put("0", "enderiterefined:block/enderite_debris_cracked_side");
        textures.put("1", "enderiterefined:block/enderite_debris_cracked_top");

//        jsonModel = new JsonModel(new ArrayList<>(){{add(element);}});
        jsonModel = new JsonModel("/assets/" + ManuMod.MODID + "/models/block/enderite_debris_cracked.json");
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int renderId, RenderBlocks renderer) {
        jsonModel.render(renderer, block, new V3(x, y, z));
        return true;
    }
}
