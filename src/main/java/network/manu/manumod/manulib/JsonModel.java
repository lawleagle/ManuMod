package network.manu.manumod.manulib;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import network.manu.manumod.ManuMod;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



@Data
public class JsonModel {
    public String filePath;
    public Map<String, String> _textures = new HashMap<>();
    public Map<String, IIcon> textures = new HashMap<>();
    public ArrayList<Element> elements = new ArrayList<>();

    public JsonModel(ArrayList<Element> elements) {
        this.elements = elements;
    }

    public void readFromJson() {
        _textures = new HashMap<>();
        elements = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        try (InputStream inputStream = ManuMod.class.getResourceAsStream(filePath)) {
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            JSONObject jsonObject = (JSONObject) jsonParser.parse(content);
            JSONObject jsonTextures = (JSONObject) jsonObject.get("textures");
            JSONArray jsonElements = (JSONArray) jsonObject.get("elements");

            jsonTextures.keySet().stream().forEach(key -> {
                _textures.put(
                    '#' + key.toString(),
                    jsonTextures.get(key).toString()
                );
            });

            for (Object _jsonElement : jsonElements) {
                JSONObject jsonElement = (JSONObject) _jsonElement;

                JSONArray fromJsonArray = (JSONArray) jsonElement.get("from");
                V3 from =
                    new V3(
                        Double.parseDouble(fromJsonArray.get(0).toString()),
                        Double.parseDouble(fromJsonArray.get(1).toString()),
                        Double.parseDouble(fromJsonArray.get(2).toString())
                    );

                JSONArray toJsonArray = (JSONArray) jsonElement.get("to");
                V3 to =
                    new V3(
                        Double.parseDouble(toJsonArray.get(0).toString()),
                        Double.parseDouble(toJsonArray.get(1).toString()),
                        Double.parseDouble(toJsonArray.get(2).toString())
                    );

                Map<String, Element.Face> faces = new HashMap<>();
                JSONObject jsonFaces = (JSONObject) jsonElement.get("faces");
                jsonFaces.keySet().stream().forEach(key -> {
                    JSONArray jsonUV = (JSONArray)((JSONObject)jsonFaces.get(key)).get("uv");
                    V2 minUV =
                        new V2(
                            Double.parseDouble(jsonUV.get(0).toString()),
                            Double.parseDouble(jsonUV.get(1).toString())
                        );
                    V2 maxUV =
                        new V2(
                            Double.parseDouble(jsonUV.get(2).toString()),
                            Double.parseDouble(jsonUV.get(3).toString())
                        );
                    String textureLink = ((JSONObject)jsonFaces.get(key)).get("texture").toString();
                    String cullface = ((JSONObject)jsonFaces.get(key)).get("cullface").toString();
                    int rotation = 0;
                    try { rotation = Integer.parseInt(((JSONObject)jsonFaces.get(key)).get("rotation").toString());
                    } catch (Exception e) {}
                    faces.put(
                        key.toString(),
                        new JsonModel.Element.Face(
                            minUV,
                            maxUV,
                            textureLink,
                            rotation,
                            cullface
                        )
                    );
                });

                elements.add(new JsonModel.Element(from, to, faces));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public JsonModel(String filePath) {
        this.filePath = filePath;
        readFromJson();
    }

    public void registerIcons(IIconRegister register) {
        textures = new HashMap<>();
        for (Map.Entry<String, String> _texture : _textures.entrySet()) {
            textures.put(
                _texture.getKey(),
                register.registerIcon(ManuMod.TEXTURE_PREFIX + _texture.getValue())
            );
        }
    }

    @Data
    @AllArgsConstructor
    static public class Element {
        public V3 from;
        public V3 to;
        public Map<String, Face> faces;

        @Data
        static public class Face {
            public V2 minUV;
            public V2 maxUV;
            public String textureLink;
            public int rotation;
            public String cullface;

            public Face(V2 minUV, V2 maxUV, String textureLink, int rotation, String cullface) {
                this.minUV = minUV;
                this.maxUV = maxUV;
                this.textureLink = textureLink;
                this.rotation = rotation;
                this.cullface = cullface;
            }
        }
    }

    public void render(RenderBlocks renderer, Block block, V3 location) {
        Tessellator tessellator = Tessellator.instance;

        for (Element element : elements) {
            V3 from = element.from;
            V3 to = element.to;

            renderer.setRenderBounds(
                from.x / 16.0, from.y / 16.0, from.z / 16.0,
                to.x / 16.0, to.y / 16.0, to.z / 16.0
            );

            // NOTE(manu): renderStandardBlock
            int colorMultiplier = block.colorMultiplier(renderer.blockAccess, (int)location.x, (int)location.y, (int)location.z);
            float colorMultiplierLow = (float)(colorMultiplier >> 16 & 255) / 255.0F;
            float colorMultiplierMiddle = (float)(colorMultiplier >> 8 & 255) / 255.0F;
            float colorMultiplierHigh = (float)(colorMultiplier & 255) / 255.0F;
            if (EntityRenderer.anaglyphEnable)
            {
                float f3 = (colorMultiplierLow * 30.0F + colorMultiplierMiddle * 59.0F + colorMultiplierHigh * 11.0F) / 100.0F;
                float f4 = (colorMultiplierLow * 30.0F + colorMultiplierMiddle * 70.0F) / 100.0F;
                float f5 = (colorMultiplierLow * 30.0F + colorMultiplierHigh * 70.0F) / 100.0F;
                colorMultiplierLow = f3;
                colorMultiplierMiddle = f4;
                colorMultiplierHigh = f5;
            }

            // NOTE(manu): renderStandardBlockWithColorMultiplier
            renderer.enableAO = false;
            float f3 = 0.5F;
            float f4 = 1.0F;
            float f5 = 0.8F;
            float f6 = 0.6F;
            float f7 = f4 * colorMultiplierLow;
            float f8 = f4 * colorMultiplierMiddle;
            float f9 = f4 * colorMultiplierHigh;
            float f10 = f3;
            float f11 = f5;
            float f12 = f6;
            float f13 = f3;
            float f14 = f5;
            float f15 = f6;
            float f16 = f3;
            float f17 = f5;
            float f18 = f6;
            f10 = f3 * colorMultiplierLow;
            f11 = f5 * colorMultiplierLow;
            f12 = f6 * colorMultiplierLow;
            f13 = f3 * colorMultiplierMiddle;
            f14 = f5 * colorMultiplierMiddle;
            f15 = f6 * colorMultiplierMiddle;
            f16 = f3 * colorMultiplierHigh;
            f17 = f5 * colorMultiplierHigh;
            f18 = f6 * colorMultiplierHigh;
            int l = block.getMixedBrightnessForBlock(renderer.blockAccess, (int)location.x, (int)location.y, (int)location.z);

            for (Map.Entry<String, Element.Face> entry : element.faces.entrySet()) {
                String orientation = entry.getKey();
                Element.Face face = entry.getValue();

                IIcon texture;
                if (textures.size() == 0) {
                    texture = block.getBlockTextureFromSide(0);
                } else {
                    texture = textures.get(face.textureLink);
                }

                HashMap<String, Integer> sideMap = new HashMap<>(){{
                    put("down", 0);
                    put("up", 1);
                    put("north", 2);
                    put("south", 3);
                    put("west", 4);
                    put("east", 5);
                }};

                HashMap<String, V3> directionMap = new HashMap<>() {{
                    put("down", new V3(0, -1, 0));
                    put("up", new V3(0, 1, 0));
                    put("north", new V3(0, 0, -1));
                    put("south", new V3(0, 0, 1));
                    put("west", new V3(-1, 0, 0));
                    put("east", new V3(1, 0, 0));
                }};

                boolean shouldSideBeRendered =
                    block.shouldSideBeRendered(
                        renderer.blockAccess,
                        (int)location.x + (int)directionMap.get(orientation).x,
                        (int)location.y + (int)directionMap.get(orientation).y,
                        (int)location.z + (int)directionMap.get(orientation).z,
                        sideMap.get(face.cullface)
                    );
                shouldSideBeRendered = true;
                if (!face.cullface.equals("") && shouldSideBeRendered) {
                    switch (orientation) {
                        case "north": {
                            tessellator.setBrightness(
                                renderer.renderMinZ > 0.0D ?
                                    l :
                                    block.getMixedBrightnessForBlock(
                                        renderer.blockAccess,
                                        (int) location.x,
                                        (int) location.y,
                                        (int) location.z - 1
                                    )
                            );
                            tessellator.setColorOpaque_F(f11, f14, f17);
                            renderNorthFace(renderer, location, face, texture);
                        }
                        break;

                        case "south": {
                            tessellator.setBrightness(
                                renderer.renderMaxZ < 1.0D ?
                                    l :
                                    block.getMixedBrightnessForBlock(
                                        renderer.blockAccess,
                                        (int) location.x,
                                        (int) location.y,
                                        (int) location.z + 1
                                    )
                            );
                            tessellator.setColorOpaque_F(f11, f14, f17);
                            renderSouthFace(renderer, location, face, texture);
                        }
                        break;

                        case "east": {
                            tessellator.setBrightness(renderer.renderMaxX < 1.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, (int) location.x + 1, (int) location.y, (int) location.z));
                            tessellator.setColorOpaque_F(f12, f15, f18);
                            renderEastFace(renderer, location, face, texture);
                        }
                        break;

                        case "west": {
                            tessellator.setBrightness(renderer.renderMinX > 0.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, (int) location.x - 1, (int) location.y, (int) location.z));
                            tessellator.setColorOpaque_F(f12, f15, f18);
                            renderWestFace(renderer, location, face, texture);
                        }
                        break;

                        case "up": {
                            tessellator.setBrightness(renderer.renderMaxY < 1.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, (int) location.x, (int) location.y + 1, (int) location.z));
                            tessellator.setColorOpaque_F(f7, f8, f9);
                            renderUpFace(renderer, location, face, texture);
                        }
                        break;

                        case "down": {
                            tessellator.setBrightness(renderer.renderMinY > 0.0D ? l : block.getMixedBrightnessForBlock(renderer.blockAccess, (int) location.x, (int) location.y - 1, (int) location.z));
                            tessellator.setColorOpaque_F(f10, f13, f16);
                            renderDownFace(renderer, location, face, texture);
                        }
                        break;
                    }
                }
            }
        }
    }

    public void renderNorthFace(RenderBlocks renderer, V3 location, Element.Face face, IIcon texture) {
        Tessellator tessellator = Tessellator.instance;

        double textureMinU = texture.getInterpolatedU(face.minUV.getU());
        double textureMinV = texture.getInterpolatedV(face.minUV.getV());
        double textureMaxU = texture.getInterpolatedU(face.maxUV.getU());
        double textureMaxV = texture.getInterpolatedV(face.maxUV.getV());

        if (renderer.flipTexture)
        {
            double aux = textureMinU;
            textureMinU = textureMaxU;
            textureMaxU = aux;
        }

        double _textureMaxU = textureMaxU;
        double _textureMinU = textureMinU;
        double _textureMaxV = textureMaxV;
        double _textureMinV = textureMinV;

        if (face.rotation == 90) // 2
        {
//            textureMinU = texture.getInterpolatedU(renderer.renderMinY * 16.0D);
//            textureMaxU = texture.getInterpolatedU(renderer.renderMaxY * 16.0D);
//            textureMaxV = texture.getInterpolatedV(16.0D - renderer.renderMinX * 16.0D);
//            textureMinV = texture.getInterpolatedV(16.0D - renderer.renderMaxX * 16.0D);
            _textureMaxV = textureMaxV;
            _textureMinV = textureMinV;
            _textureMaxU = textureMinU;
            _textureMinU = textureMaxU;
            textureMaxV = textureMinV;
            textureMinV = _textureMaxV;
        }
        else if (face.rotation == 90) // 1
        {
            double aux = textureMinU;
            textureMinU = textureMaxU;
            textureMaxU = aux;
//            textureMinU = texture.getInterpolatedU(16.0D - renderer.renderMaxY * 16.0D);
//            textureMaxU = texture.getInterpolatedU(16.0D - renderer.renderMinY * 16.0D);
//            textureMaxV = texture.getInterpolatedV(renderer.renderMaxX * 16.0D);
//            textureMinV = texture.getInterpolatedV(renderer.renderMinX * 16.0D);
            _textureMaxU = textureMaxU;
            _textureMinU = textureMinU;
            textureMinU = textureMaxU;
            textureMaxU = _textureMinU;
            _textureMaxV = textureMinV;
            _textureMinV = textureMaxV;
        }
        else if (face.rotation == 90) // 3
        {
            double aux = textureMaxV;
            textureMaxV = textureMinV;
            textureMinV = aux;
//            textureMinU = texture.getInterpolatedU(16.0D - renderer.renderMinX * 16.0D);
//            textureMaxU = texture.getInterpolatedU(16.0D - renderer.renderMaxX * 16.0D);
//            textureMaxV = texture.getInterpolatedV(renderer.renderMaxY * 16.0D);
//            textureMinV = texture.getInterpolatedV(renderer.renderMinY * 16.0D);
            _textureMaxU = textureMaxU;
            _textureMinU = textureMinU;
            _textureMaxV = textureMaxV;
            _textureMinV = textureMinV;
        }

        double locationMinX = location.x + renderer.renderMinX;
        double locationMaxX = location.x + renderer.renderMaxX;
        double locationMinY = location.y + renderer.renderMinY;
        double locationMaxY = location.y + renderer.renderMaxY;
        double locationMinZ = location.z + renderer.renderMinZ;

        if (renderer.renderFromInside)
        {
            locationMinX = location.x + renderer.renderMaxX;
            locationMaxX = location.x + renderer.renderMinX;
        }

        if (renderer.enableAO)
        {
            tessellator.setColorOpaque_F(renderer.colorRedTopLeft, renderer.colorGreenTopLeft, renderer.colorBlueTopLeft);
            tessellator.setBrightness(renderer.brightnessTopLeft);
            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMinZ, _textureMaxU, _textureMaxV);
            tessellator.setColorOpaque_F(renderer.colorRedBottomLeft, renderer.colorGreenBottomLeft, renderer.colorBlueBottomLeft);
            tessellator.setBrightness(renderer.brightnessBottomLeft);
            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMinZ, textureMinU, textureMaxV);
            tessellator.setColorOpaque_F(renderer.colorRedBottomRight, renderer.colorGreenBottomRight, renderer.colorBlueBottomRight);
            tessellator.setBrightness(renderer.brightnessBottomRight);
            tessellator.addVertexWithUV(locationMaxX, locationMinY, locationMinZ, _textureMinU, _textureMinV);
            tessellator.setColorOpaque_F(renderer.colorRedTopRight, renderer.colorGreenTopRight, renderer.colorBlueTopRight);
            tessellator.setBrightness(renderer.brightnessTopRight);
            tessellator.addVertexWithUV(locationMinX, locationMinY, locationMinZ, textureMaxU, textureMinV);
        }
        else
        {
            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMinZ, _textureMaxU, _textureMaxV);
            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMinZ, textureMinU, textureMaxV);
            tessellator.addVertexWithUV(locationMaxX, locationMinY, locationMinZ, _textureMinU, _textureMinV);
            tessellator.addVertexWithUV(locationMinX, locationMinY, locationMinZ, textureMaxU, textureMinV);
        }
    }

    public void renderSouthFace(RenderBlocks renderer, V3 location, Element.Face face, IIcon texture) {
        Tessellator tessellator = Tessellator.instance;

        double textureMinU = texture.getInterpolatedU(face.minUV.getU());
        double textureMinV = texture.getInterpolatedV(face.minUV.getV());
        double textureMaxU = texture.getInterpolatedU(face.maxUV.getU());
        double textureMaxV = texture.getInterpolatedV(face.maxUV.getV());

        double _textureMaxU = textureMaxU;
        double _textureMinU = textureMinU;
        double _textureMinV = textureMinV;
        double _textureMaxV = textureMaxV;

        if (renderer.uvRotateWest == 1)
        {
            textureMinU = texture.getInterpolatedU(renderer.renderMinY * 16.0D);
            textureMaxV = texture.getInterpolatedV(16.0D - renderer.renderMinX * 16.0D);
            textureMaxU = texture.getInterpolatedU(renderer.renderMaxY * 16.0D);
            textureMinV = texture.getInterpolatedV(16.0D - renderer.renderMaxX * 16.0D);
            _textureMinV = textureMinV;
            _textureMaxV = textureMaxV;
            _textureMaxU = textureMinU;
            _textureMinU = textureMaxU;
            textureMinV = textureMaxV;
            textureMaxV = _textureMinV;
        }
        else if (renderer.uvRotateWest == 2)
        {
            textureMinU = texture.getInterpolatedU(16.0D - renderer.renderMaxY * 16.0D);
            textureMinV = texture.getInterpolatedV(renderer.renderMinX * 16.0D);
            textureMaxU = texture.getInterpolatedU(16.0D - renderer.renderMinY * 16.0D);
            textureMaxV = texture.getInterpolatedV(renderer.renderMaxX * 16.0D);
            _textureMaxU = textureMaxU;
            _textureMinU = textureMinU;
            textureMinU = textureMaxU;
            textureMaxU = _textureMinU;
            _textureMinV = textureMaxV;
            _textureMaxV = textureMinV;
        }
        else if (renderer.uvRotateWest == 3)
        {
            textureMinU = texture.getInterpolatedU(16.0D - renderer.renderMinX * 16.0D);
            textureMaxU = texture.getInterpolatedU(16.0D - renderer.renderMaxX * 16.0D);
            textureMinV = texture.getInterpolatedV(renderer.renderMaxY * 16.0D);
            textureMaxV = texture.getInterpolatedV(renderer.renderMinY * 16.0D);
            _textureMaxU = textureMaxU;
            _textureMinU = textureMinU;
            _textureMinV = textureMinV;
            _textureMaxV = textureMaxV;
        }

        double locationMinX = location.x + renderer.renderMinX;
        double locationMaxX = location.x + renderer.renderMaxX;
        double locationMinY = location.y + renderer.renderMinY;
        double locationMaxY = location.y + renderer.renderMaxY;
        double locationMaxZ = location.z + renderer.renderMaxZ;

        if (renderer.renderFromInside)
        {
            locationMinX = location.x + renderer.renderMaxX;
            locationMaxX = location.x + renderer.renderMinX;
        }

        if (renderer.enableAO)
        {
            tessellator.setColorOpaque_F(renderer.colorRedTopLeft, renderer.colorGreenTopLeft, renderer.colorBlueTopLeft);
            tessellator.setBrightness(renderer.brightnessTopLeft);
            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMaxZ, textureMinU, textureMinV);
            tessellator.setColorOpaque_F(renderer.colorRedBottomLeft, renderer.colorGreenBottomLeft, renderer.colorBlueBottomLeft);
            tessellator.setBrightness(renderer.brightnessBottomLeft);
            tessellator.addVertexWithUV(locationMinX, locationMinY, locationMaxZ, _textureMinU, _textureMaxV);
            tessellator.setColorOpaque_F(renderer.colorRedBottomRight, renderer.colorGreenBottomRight, renderer.colorBlueBottomRight);
            tessellator.setBrightness(renderer.brightnessBottomRight);
            tessellator.addVertexWithUV(locationMaxX, locationMinY, locationMaxZ, textureMaxU, textureMaxV);
            tessellator.setColorOpaque_F(renderer.colorRedTopRight, renderer.colorGreenTopRight, renderer.colorBlueTopRight);
            tessellator.setBrightness(renderer.brightnessTopRight);
            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMaxZ, _textureMaxU, _textureMinV);
        }
        else
        {
            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMaxZ, textureMinU, textureMinV);
            tessellator.addVertexWithUV(locationMinX, locationMinY, locationMaxZ, _textureMinU, _textureMaxV);
            tessellator.addVertexWithUV(locationMaxX, locationMinY, locationMaxZ, textureMaxU, textureMaxV);
            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMaxZ, _textureMaxU, _textureMinV);
        }
    }

    public void renderWestFace(RenderBlocks renderer, V3 location, Element.Face face, IIcon texture) {
        Tessellator tessellator = Tessellator.instance;

        double textureMinU = texture.getInterpolatedU(face.minUV.getU());
        double textureMinV = texture.getInterpolatedV(face.minUV.getV());
        double textureMaxU = texture.getInterpolatedU(face.maxUV.getU());
        double textureMaxV = texture.getInterpolatedV(face.maxUV.getV());

        if (renderer.flipTexture) {
            double aux = textureMinU;
            textureMinU = textureMaxU;
            textureMaxU = aux;
        }

        double _textureMaxU = textureMaxU;
        double _textureMinU = textureMinU;
        double _textureMaxV = textureMaxV;
        double _textureMinV = textureMinV;

        if (renderer.uvRotateNorth == 1)
        {
            textureMinU = texture.getInterpolatedU(renderer.renderMinY * 16.0D);
            textureMaxV = texture.getInterpolatedV(16.0D - renderer.renderMaxZ * 16.0D);
            textureMaxU = texture.getInterpolatedU(renderer.renderMaxY * 16.0D);
            textureMinV = texture.getInterpolatedV(16.0D - renderer.renderMinZ * 16.0D);
            _textureMaxV = textureMaxV;
            _textureMinV = textureMinV;
            _textureMaxU = textureMinU;
            _textureMinU = textureMaxU;
            textureMaxV = textureMinV;
            textureMinV = _textureMaxV;
        }
        else if (renderer.uvRotateNorth == 2)
        {
            textureMinU = texture.getInterpolatedU(16.0D - renderer.renderMaxY * 16.0D);
            textureMaxV = texture.getInterpolatedV(renderer.renderMinZ * 16.0D);
            textureMaxU = texture.getInterpolatedU(16.0D - renderer.renderMinY * 16.0D);
            textureMinV = texture.getInterpolatedV(renderer.renderMaxZ * 16.0D);
            _textureMaxU = textureMaxU;
            _textureMinU = textureMinU;
            textureMinU = textureMaxU;
            textureMaxU = _textureMinU;
            _textureMaxV = textureMinV;
            _textureMinV = textureMaxV;
        }
        else if (renderer.uvRotateNorth == 3)
        {
            textureMinU = texture.getInterpolatedU(16.0D - renderer.renderMinZ * 16.0D);
            textureMaxU = texture.getInterpolatedU(16.0D - renderer.renderMaxZ * 16.0D);
            textureMaxV = texture.getInterpolatedV(renderer.renderMaxY * 16.0D);
            textureMinV = texture.getInterpolatedV(renderer.renderMinY * 16.0D);
            _textureMaxU = textureMaxU;
            _textureMinU = textureMinU;
            _textureMaxV = textureMaxV;
            _textureMinV = textureMinV;
        }

        double locationMinX = location.x + renderer.renderMinX;
        double locationMinY = location.y + renderer.renderMinY;
        double locationMaxY = location.y + renderer.renderMaxY;
        double locationMinZ = location.z + renderer.renderMinZ;
        double locationMaxZ = location.z + renderer.renderMaxZ;

        if (renderer.renderFromInside)
        {
            locationMinZ = location.z + renderer.renderMaxZ;
            locationMaxZ = location.z + renderer.renderMinZ;
        }

        if (renderer.enableAO)
        {
            tessellator.setColorOpaque_F(renderer.colorRedTopLeft, renderer.colorGreenTopLeft, renderer.colorBlueTopLeft);
            tessellator.setBrightness(renderer.brightnessTopLeft);
            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMaxZ, _textureMaxU, _textureMaxV);
            tessellator.setColorOpaque_F(renderer.colorRedBottomLeft, renderer.colorGreenBottomLeft, renderer.colorBlueBottomLeft);
            tessellator.setBrightness(renderer.brightnessBottomLeft);
            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMinZ, textureMinU, textureMaxV);
            tessellator.setColorOpaque_F(renderer.colorRedBottomRight, renderer.colorGreenBottomRight, renderer.colorBlueBottomRight);
            tessellator.setBrightness(renderer.brightnessBottomRight);
            tessellator.addVertexWithUV(locationMinX, locationMinY, locationMinZ, _textureMinU, _textureMinV);
            tessellator.setColorOpaque_F(renderer.colorRedTopRight, renderer.colorGreenTopRight, renderer.colorBlueTopRight);
            tessellator.setBrightness(renderer.brightnessTopRight);
            tessellator.addVertexWithUV(locationMinX, locationMinY, locationMaxZ, textureMaxU, textureMinV);
        }
        else
        {
            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMaxZ, _textureMaxU, _textureMaxV);
            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMinZ, textureMinU, textureMaxV);
            tessellator.addVertexWithUV(locationMinX, locationMinY, locationMinZ, _textureMinU, _textureMinV);
            tessellator.addVertexWithUV(locationMinX, locationMinY, locationMaxZ, textureMaxU, textureMinV);
        }
    }

    public void renderEastFace(RenderBlocks renderer, V3 location, Element.Face face, IIcon texture) {
        Tessellator tessellator = Tessellator.instance;

        double textureMinU = texture.getInterpolatedU(face.minUV.getU());
        double textureMinV = texture.getInterpolatedV(face.minUV.getV());
        double textureMaxU = texture.getInterpolatedU(face.maxUV.getU());
        double textureMaxV = texture.getInterpolatedV(face.maxUV.getV());

        double _textureMaxU = textureMaxU;
        double _textureMinU = textureMinU;
        double _textureMinV = textureMinV;
        double _textureMaxV = textureMaxV;

        if (renderer.uvRotateSouth == 2)
        {
            textureMinU = texture.getInterpolatedU(renderer.renderMinY * 16.0D);
            textureMinV = texture.getInterpolatedV(16.0D - renderer.renderMinZ * 16.0D);
            textureMaxU = texture.getInterpolatedU(renderer.renderMaxY * 16.0D);
            textureMaxV = texture.getInterpolatedV(16.0D - renderer.renderMaxZ * 16.0D);
            _textureMinV = textureMinV;
            _textureMaxV = textureMaxV;
            _textureMaxU = textureMinU;
            _textureMinU = textureMaxU;
            textureMinV = textureMaxV;
            textureMaxV = _textureMinV;
        }
        else if (renderer.uvRotateSouth == 1)
        {
            textureMinU = texture.getInterpolatedU(16.0D - renderer.renderMaxY * 16.0D);
            textureMinV = texture.getInterpolatedV(renderer.renderMaxZ * 16.0D);
            textureMaxU = texture.getInterpolatedU(16.0D - renderer.renderMinY * 16.0D);
            textureMaxV = texture.getInterpolatedV(renderer.renderMinZ * 16.0D);
            _textureMaxU = textureMaxU;
            _textureMinU = textureMinU;
            textureMinU = textureMaxU;
            textureMaxU = _textureMinU;
            _textureMinV = textureMaxV;
            _textureMaxV = textureMinV;
        }
        else if (renderer.uvRotateSouth == 3)
        {
            textureMinU = texture.getInterpolatedU(16.0D - renderer.renderMinZ * 16.0D);
            textureMaxU = texture.getInterpolatedU(16.0D - renderer.renderMaxZ * 16.0D);
            textureMinV = texture.getInterpolatedV(renderer.renderMaxY * 16.0D);
            textureMaxV = texture.getInterpolatedV(renderer.renderMinY * 16.0D);
            _textureMaxU = textureMaxU;
            _textureMinU = textureMinU;
            _textureMinV = textureMinV;
            _textureMaxV = textureMaxV;
        }

        double locationMaxX = location.x + renderer.renderMaxX;
        double locationMinY = location.y + renderer.renderMinY;
        double locationMaxY = location.y + renderer.renderMaxY;
        double locationMinZ = location.z + renderer.renderMinZ;
        double locationMaxZ = location.z + renderer.renderMaxZ;

        if (renderer.renderFromInside)
        {
            locationMinZ = location.z + renderer.renderMaxZ;
            locationMaxZ = location.z + renderer.renderMinZ;
        }

        if (renderer.enableAO)
        {
            tessellator.setColorOpaque_F(renderer.colorRedTopLeft, renderer.colorGreenTopLeft, renderer.colorBlueTopLeft);
            tessellator.setBrightness(renderer.brightnessTopLeft);
            tessellator.addVertexWithUV(locationMaxX, locationMinY, locationMaxZ, _textureMinU, _textureMaxV);
            tessellator.setColorOpaque_F(renderer.colorRedBottomLeft, renderer.colorGreenBottomLeft, renderer.colorBlueBottomLeft);
            tessellator.setBrightness(renderer.brightnessBottomLeft);
            tessellator.addVertexWithUV(locationMaxX, locationMinY, locationMinZ, textureMaxU, textureMaxV);
            tessellator.setColorOpaque_F(renderer.colorRedBottomRight, renderer.colorGreenBottomRight, renderer.colorBlueBottomRight);
            tessellator.setBrightness(renderer.brightnessBottomRight);
            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMinZ, _textureMaxU, _textureMinV);
            tessellator.setColorOpaque_F(renderer.colorRedTopRight, renderer.colorGreenTopRight, renderer.colorBlueTopRight);
            tessellator.setBrightness(renderer.brightnessTopRight);
            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMaxZ, textureMinU, textureMinV);
        }
        else
        {
            tessellator.addVertexWithUV(locationMaxX, locationMinY, locationMaxZ, _textureMinU, _textureMaxV);
            tessellator.addVertexWithUV(locationMaxX, locationMinY, locationMinZ, textureMaxU, textureMaxV);
            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMinZ, _textureMaxU, _textureMinV);
            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMaxZ, textureMinU, textureMinV);
        }
    }

//    public void renderUpFace(RenderBlocks renderer, V3 location, Element.Face face, IIcon texture) {
//        Tessellator tessellator = Tessellator.instance;
//
//        double textureMinU = texture.getInterpolatedU(face.minUV.getU());
//        double textureMinV = texture.getInterpolatedV(face.minUV.getV());
//        double textureMaxU = texture.getInterpolatedU(face.maxUV.getU());
//        double textureMaxV = texture.getInterpolatedV(face.maxUV.getV());
//
//        double _textureMinU = textureMinU;
//        double _textureMinV = textureMinV;
//        double _textureMaxU = textureMaxU;
//        double _textureMaxV = textureMaxV;
//
//        _textureMinU = textureMinU;
//        _textureMinV = textureMinV;
//        _textureMaxU = textureMaxU;
//        _textureMaxV = textureMaxV;
//
//        switch (face.rotation) {
//            case 90: {
//                V2 textureStart =
//                    new V2(
//                        texture.getInterpolatedU(0),
//                        texture.getInterpolatedV(0)
//                    );
//
//                V2 textureEnd =
//                    new V2(
//                        texture.getInterpolatedU(16),
//                        texture.getInterpolatedV(16)
//                    );
//
//                V2 textureSize = textureEnd.minus(textureStart);
//
//                V2 textureMin = new V2(textureMinU, textureMinV);
//                V2 textureMax = new V2(textureMaxU, textureMaxV);
//
//                textureMin = textureMin.minus(textureStart);
//                textureMax = textureMax.minus(textureStart);
//
//                V2 _textureMin = textureMin.copy();
//                V2 _textureMax = textureMax.copy();
//
//                textureMin.setU(-_textureMax.getV() + textureSize.getV());
//                textureMin.setV(_textureMax.getU());
//                textureMax.setU(-_textureMin.getV() + textureSize.getV());
//                textureMax.setV(_textureMin.getU());
//
//                textureMin = textureMin.plus(textureStart);
//                textureMax = textureMax.plus(textureStart);
//
//                textureMinU = textureMin.getU();
//                textureMinV = textureMin.getV();
//                textureMaxU = textureMax.getU();
//                textureMaxV = textureMax.getV();
//            } break;
//
//            case 180: {
//            } break;
//
//            case 270: {
////                textureMinU = texture.getInterpolatedU(face.minUV.getV());
////                textureMinV = texture.getInterpolatedV(face.minUV.getU());
////                textureMaxU = texture.getInterpolatedU(face.maxUV.getV());
////                textureMaxV = texture.getInterpolatedV(face.maxUV.getU());
//            } break;
//        }
//
////        if (renderer.uvRotateTop == 1)
////        {
////            textureMinU = texture.getInterpolatedU(renderer.renderMinZ * 16.0D);
////            textureMinV = texture.getInterpolatedV(16.0D - renderer.renderMaxX * 16.0D);
////            textureMaxU = texture.getInterpolatedU(renderer.renderMaxZ * 16.0D);
////            textureMaxV = texture.getInterpolatedV(16.0D - renderer.renderMinX * 16.0D);
////            _textureMinV = textureMinV;
////            _textureMaxV = textureMaxV;
////            _textureMaxU = textureMinU;
////            _textureMinU = textureMaxU;
////            textureMinV = textureMaxV;
////            textureMaxV = _textureMinV;
////        }
////        else if (renderer.uvRotateTop == 2)
////        {
////            textureMinU = texture.getInterpolatedU(16.0D - renderer.renderMaxZ * 16.0D);
////            textureMinV = texture.getInterpolatedV(renderer.renderMinX * 16.0D);
////            textureMaxU = texture.getInterpolatedU(16.0D - renderer.renderMinZ * 16.0D);
////            textureMaxV = texture.getInterpolatedV(renderer.renderMaxX * 16.0D);
////            _textureMaxU = textureMaxU;
////            _textureMinU = textureMinU;
////            textureMinU = textureMaxU;
////            textureMaxU = _textureMinU;
////            _textureMinV = textureMaxV;
////            _textureMaxV = textureMinV;
////        }
////        else if (renderer.uvRotateTop == 3)
////        {
////            textureMinU = texture.getInterpolatedU(16.0D - renderer.renderMinX * 16.0D);
////            textureMaxU = texture.getInterpolatedU(16.0D - renderer.renderMaxX * 16.0D);
////            textureMinV = texture.getInterpolatedV(16.0D - renderer.renderMinZ * 16.0D);
////            textureMaxV = texture.getInterpolatedV(16.0D - renderer.renderMaxZ * 16.0D);
////            _textureMaxU = textureMaxU;
////            _textureMinU = textureMinU;
////            _textureMinV = textureMinV;
////            _textureMaxV = textureMaxV;
////        }
//
//        double locationMinX = location.x + renderer.renderMinX;
//        double locationMaxX = location.x + renderer.renderMaxX;
//        double locationMaxY = location.y + renderer.renderMaxY;
//        double locationMinZ = location.z + renderer.renderMinZ;
//        double locationMaxZ = location.z + renderer.renderMaxZ;
//
//        if (renderer.renderFromInside)
//        {
//            locationMinX = location.x + renderer.renderMaxX;
//            locationMaxX = location.x + renderer.renderMinX;
//        }
//
//        if (renderer.enableAO)
//        {
//            tessellator.setColorOpaque_F(renderer.colorRedTopLeft, renderer.colorGreenTopLeft, renderer.colorBlueTopLeft);
//            tessellator.setBrightness(renderer.brightnessTopLeft);
//            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMaxZ, textureMaxU, textureMaxV);
//            tessellator.setColorOpaque_F(renderer.colorRedBottomLeft, renderer.colorGreenBottomLeft, renderer.colorBlueBottomLeft);
//            tessellator.setBrightness(renderer.brightnessBottomLeft);
//            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMinZ, _textureMaxU, _textureMinV);
//            tessellator.setColorOpaque_F(renderer.colorRedBottomRight, renderer.colorGreenBottomRight, renderer.colorBlueBottomRight);
//            tessellator.setBrightness(renderer.brightnessBottomRight);
//            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMinZ, textureMinU, textureMinV);
//            tessellator.setColorOpaque_F(renderer.colorRedTopRight, renderer.colorGreenTopRight, renderer.colorBlueTopRight);
//            tessellator.setBrightness(renderer.brightnessTopRight);
//            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMaxZ, _textureMinU, _textureMaxV);
//        }
//        else
//        {
////            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMaxZ, textureMaxU, textureMaxV);
////            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMinZ, _textureMaxU, _textureMinV);
////            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMinZ, textureMinU, textureMinV);
////            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMaxZ, _textureMinU, _textureMaxV);
//
//            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMaxZ, textureMaxU, textureMaxV);
//            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMinZ, textureMaxU, textureMinV);
//            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMinZ, textureMinU, textureMinV);
//            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMaxZ, textureMinU, textureMaxV);
//        }
//    }

    public void renderDownFace(RenderBlocks renderer, V3 location, Element.Face face, IIcon texture) {
        Tessellator tessellator = Tessellator.instance;

        double textureMinU = texture.getInterpolatedU(face.minUV.getU());
        double textureMinV = texture.getInterpolatedV(face.minUV.getV());
        double textureMaxU = texture.getInterpolatedU(face.maxUV.getU());
        double textureMaxV = texture.getInterpolatedV(face.maxUV.getV());

        double _textureMaxU = textureMaxU;
        double _textureMinU = textureMinU;
        double _textureMinV = textureMinV;
        double _textureMaxV = textureMaxV;

        if (renderer.uvRotateBottom == 2)
        {
            textureMinU = texture.getInterpolatedU(renderer.renderMinZ * 16.0D);
            textureMinV = texture.getInterpolatedV(16.0D - renderer.renderMaxX * 16.0D);
            textureMaxU = texture.getInterpolatedU(renderer.renderMaxZ * 16.0D);
            textureMaxV = texture.getInterpolatedV(16.0D - renderer.renderMinX * 16.0D);
            _textureMinV = textureMinV;
            _textureMaxV = textureMaxV;
            _textureMaxU = textureMinU;
            _textureMinU = textureMaxU;
            textureMinV = textureMaxV;
            textureMaxV = _textureMinV;
        }
        else if (renderer.uvRotateBottom == 1)
        {
            textureMinU = texture.getInterpolatedU(16.0D - renderer.renderMaxZ * 16.0D);
            textureMinV = texture.getInterpolatedV(renderer.renderMinX * 16.0D);
            textureMaxU = texture.getInterpolatedU(16.0D - renderer.renderMinZ * 16.0D);
            textureMaxV = texture.getInterpolatedV(renderer.renderMaxX * 16.0D);
            _textureMaxU = textureMaxU;
            _textureMinU = textureMinU;
            textureMinU = textureMaxU;
            textureMaxU = _textureMinU;
            _textureMinV = textureMaxV;
            _textureMaxV = textureMinV;
        }
        else if (renderer.uvRotateBottom == 3)
        {
            textureMinU = texture.getInterpolatedU(16.0D - renderer.renderMinX * 16.0D);
            textureMaxU = texture.getInterpolatedU(16.0D - renderer.renderMaxX * 16.0D);
            textureMinV = texture.getInterpolatedV(16.0D - renderer.renderMinZ * 16.0D);
            textureMaxV = texture.getInterpolatedV(16.0D - renderer.renderMaxZ * 16.0D);
            _textureMaxU = textureMaxU;
            _textureMinU = textureMinU;
            _textureMinV = textureMinV;
            _textureMaxV = textureMaxV;
        }

        double locationMinX = location.x + renderer.renderMinX;
        double locationMaxX = location.x + renderer.renderMaxX;
        double locationMinY = location.y + renderer.renderMinY;
        double locationMinZ = location.z + renderer.renderMinZ;
        double locationMaxZ = location.z + renderer.renderMaxZ;

        if (renderer.renderFromInside)
        {
            locationMinX = location.x + renderer.renderMaxX;
            locationMaxX = location.x + renderer.renderMinX;
        }

        if (renderer.enableAO)
        {
            tessellator.setColorOpaque_F(renderer.colorRedTopLeft, renderer.colorGreenTopLeft, renderer.colorBlueTopLeft);
            tessellator.setBrightness(renderer.brightnessTopLeft);
            tessellator.addVertexWithUV(locationMinX, locationMinY, locationMaxZ, _textureMinU, _textureMaxV);
            tessellator.setColorOpaque_F(renderer.colorRedBottomLeft, renderer.colorGreenBottomLeft, renderer.colorBlueBottomLeft);
            tessellator.setBrightness(renderer.brightnessBottomLeft);
            tessellator.addVertexWithUV(locationMinX, locationMinY, locationMinZ, textureMinU, textureMinV);
            tessellator.setColorOpaque_F(renderer.colorRedBottomRight, renderer.colorGreenBottomRight, renderer.colorBlueBottomRight);
            tessellator.setBrightness(renderer.brightnessBottomRight);
            tessellator.addVertexWithUV(locationMaxX, locationMinY, locationMinZ, _textureMaxU, _textureMinV);
            tessellator.setColorOpaque_F(renderer.colorRedTopRight, renderer.colorGreenTopRight, renderer.colorBlueTopRight);
            tessellator.setBrightness(renderer.brightnessTopRight);
            tessellator.addVertexWithUV(locationMaxX, locationMinY, locationMaxZ, textureMaxU, textureMaxV);
        }
        else
        {
            tessellator.addVertexWithUV(locationMinX, locationMinY, locationMaxZ, _textureMinU, _textureMaxV);
            tessellator.addVertexWithUV(locationMinX, locationMinY, locationMinZ, textureMinU, textureMinV);
            tessellator.addVertexWithUV(locationMaxX, locationMinY, locationMinZ, _textureMaxU, _textureMinV);
            tessellator.addVertexWithUV(locationMaxX, locationMinY, locationMaxZ, textureMaxU, textureMaxV);
        }
    }

    public void renderUpFace(RenderBlocks renderer, V3 location, Element.Face face, IIcon texture)
    {
        Tessellator tessellator = Tessellator.instance;

        double textureMinU = texture.getInterpolatedU(face.minUV.getU());
        double textureMaxU = texture.getInterpolatedU(face.maxUV.getU());
        double textureMinV = texture.getInterpolatedV(face.minUV.getV());
        double textureMaxV = texture.getInterpolatedV(face.maxUV.getV());

        double _textureMaxU = textureMaxU;
        double _textureMinU = textureMinU;
        double _textureMinV = textureMinV;
        double _textureMaxV = textureMaxV;

        switch (face.rotation) {
            case 90: {
//                textureMinU = texture.getInterpolatedU(16D - face.minUV.getU());
//                textureMaxU = texture.getInterpolatedU(16D - face.maxUV.getV());
//                textureMinV = texture.getInterpolatedV(16D - face.minUV.getU());
//                textureMaxV = texture.getInterpolatedV(16D - face.maxUV.getV());
            } break;
        }
        _textureMaxU = textureMaxU;
        _textureMinU = textureMinU;
        _textureMinV = textureMinV;
        _textureMaxV = textureMaxV;
//
//        if (face.rotation == 270)
//        {
//            textureMinU = texture.getInterpolatedU(renderer.renderMinZ * 16.0D);
//            textureMinV = texture.getInterpolatedV(16.0D - renderer.renderMaxX * 16.0D);
//            textureMaxU = texture.getInterpolatedU(renderer.renderMaxZ * 16.0D);
//            textureMaxV = texture.getInterpolatedV(16.0D - renderer.renderMinX * 16.0D);
//            _textureMinV = textureMinV;
//            _textureMaxV = textureMaxV;
//            _textureMaxU = textureMinU;
//            _textureMinU = textureMaxU;
//            textureMinV = textureMaxV;
//            textureMaxV = _textureMinV;
//        }
//        if (face.rotation == 270)
//        {
//            textureMinU = texture.getInterpolatedU(16.0D - renderer.renderMaxZ * 16.0D);
//            textureMinV = texture.getInterpolatedV(renderer.renderMinX * 16.0D);
//            textureMaxU = texture.getInterpolatedU(16.0D - renderer.renderMinZ * 16.0D);
//            textureMaxV = texture.getInterpolatedV(renderer.renderMaxX * 16.0D);
//            _textureMaxU = textureMaxU;
//            _textureMinU = textureMinU;
//            textureMinU = textureMaxU;
//            textureMaxU = _textureMinU;
//            _textureMinV = textureMaxV;
//            _textureMaxV = textureMinV;
//        }
//        if (face.rotation == 270)
//        {
//            textureMinU = texture.getInterpolatedU(16.0D - face.minUV.getU());
//            textureMaxU = texture.getInterpolatedU(16.0D - face.maxUV.getU());
//            textureMinV = texture.getInterpolatedV(16.0D - face.minUV.getV());
//            textureMaxV = texture.getInterpolatedV(16.0D - face.maxUV.getV());
//            _textureMaxU = textureMaxU;
//            _textureMinU = textureMinU;
//            _textureMinV = textureMinV;
//            _textureMaxV = textureMaxV;
//        }

        double locationMinX = location.x + renderer.renderMinX;
        double locationMaxX = location.x + renderer.renderMaxX;
        double locationMaxY = location.y + renderer.renderMaxY;
        double locationMinZ = location.z + renderer.renderMinZ;
        double locationMaxZ = location.z + renderer.renderMaxZ;

        if (renderer.renderFromInside)
        {
            locationMinX = location.x + renderer.renderMaxX;
            locationMaxX = location.x + renderer.renderMinX;
        }

        if (renderer.enableAO)
        {
            tessellator.setColorOpaque_F(renderer.colorRedTopLeft, renderer.colorGreenTopLeft, renderer.colorBlueTopLeft);
            tessellator.setBrightness(renderer.brightnessTopLeft);
            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMaxZ, textureMaxU, textureMaxV);
            tessellator.setColorOpaque_F(renderer.colorRedBottomLeft, renderer.colorGreenBottomLeft, renderer.colorBlueBottomLeft);
            tessellator.setBrightness(renderer.brightnessBottomLeft);
            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMinZ, _textureMaxU, _textureMinV);
            tessellator.setColorOpaque_F(renderer.colorRedBottomRight, renderer.colorGreenBottomRight, renderer.colorBlueBottomRight);
            tessellator.setBrightness(renderer.brightnessBottomRight);
            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMinZ, textureMinU, textureMinV);
            tessellator.setColorOpaque_F(renderer.colorRedTopRight, renderer.colorGreenTopRight, renderer.colorBlueTopRight);
            tessellator.setBrightness(renderer.brightnessTopRight);
            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMaxZ, _textureMinU, _textureMaxV);
        }
        else
        {
            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMaxZ, textureMaxU, textureMaxV);
            tessellator.addVertexWithUV(locationMaxX, locationMaxY, locationMinZ, _textureMaxU, _textureMinV);
            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMinZ, textureMinU, textureMinV);
            tessellator.addVertexWithUV(locationMinX, locationMaxY, locationMaxZ, _textureMinU, _textureMaxV);
        }
    }
}

