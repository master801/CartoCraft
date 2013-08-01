package sidben.cartocraft.client;


import java.util.Iterator;

import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapCoord;
import net.minecraft.world.storage.MapData;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



@SideOnly(Side.CLIENT)
public class MapItemRenderer {
    private static final ResourceLocation field_111277_a = new ResourceLocation("textures/map/map_icons.png");
    private final DynamicTexture          bufferedImage;
    private int[]                         intArray       = new int[16384];
    @SuppressWarnings("unused")
    private GameSettings                  gameSettings;
    private final ResourceLocation        field_111276_e;


    public MapItemRenderer(GameSettings par1GameSettings, TextureManager par2TextureManager) {
        this.gameSettings = par1GameSettings;
        this.bufferedImage = new DynamicTexture(128, 128);
        this.field_111276_e = par2TextureManager.func_110578_a("map", this.bufferedImage);
        this.intArray = this.bufferedImage.func_110565_c();

        for (int i = 0; i < this.intArray.length; ++i) {
            this.intArray[i] = 0;
        }
    }


    @SuppressWarnings("rawtypes")
    public void renderMap(EntityPlayer par1EntityPlayer, TextureManager par2TextureManager, MapData par3MapData) {
        // Debug
        System.out.println(" ");
        System.out.println("CustomRenderMap()");


        for (int i = 0; i < 16384; ++i) {
            byte b0 = par3MapData.colors[i];

            if (b0 / 4 == 0) {
                this.intArray[i] = (i + i / 128 & 1) * 8 + 16 << 24;
            }
            else {
                int j = MapColor.mapColorArray[b0 / 4].colorValue;
                int k = b0 & 3;
                short short1 = 220;

                if (k == 2) {
                    short1 = 255;
                }

                if (k == 0) {
                    short1 = 180;
                }

                int l = (j >> 16 & 255) * short1 / 255;
                int i1 = (j >> 8 & 255) * short1 / 255;
                int j1 = (j & 255) * short1 / 255;
                this.intArray[i] = -16777216 | l << 16 | i1 << 8 | j1;
            }
        }

        this.bufferedImage.func_110564_a();
        byte b1 = 0;
        byte b2 = 0;
        Tessellator tessellator = Tessellator.instance;
        float f = 0.0F;
        par2TextureManager.func_110577_a(this.field_111276_e);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(b1 + 0 + f, b2 + 128 - f, -0.009999999776482582D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(b1 + 128 - f, b2 + 128 - f, -0.009999999776482582D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(b1 + 128 - f, b2 + 0 + f, -0.009999999776482582D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(b1 + 0 + f, b2 + 0 + f, -0.009999999776482582D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        par2TextureManager.func_110577_a(MapItemRenderer.field_111277_a);
        int k1 = 0;

        for (Iterator iterator = par3MapData.playersVisibleOnMap.values().iterator(); iterator.hasNext(); ++k1) {
            MapCoord mapcoord = (MapCoord) iterator.next();



            // Debug
            System.out.println("    playersVisibleOnMap | Icon: " + mapcoord.iconSize + " - x: " + mapcoord.centerX + " - z: " + mapcoord.centerZ);
            if (mapcoord.iconSize == 0) {
                mapcoord.iconSize = 4;
            }
            if (mapcoord.iconSize == 1) {
                mapcoord.iconSize = 5;
            }



            GL11.glPushMatrix();
            GL11.glTranslatef(b1 + mapcoord.centerX / 2.0F + 64.0F, b2 + mapcoord.centerZ / 2.0F + 64.0F, -0.02F);
            GL11.glRotatef(mapcoord.iconRotation * 360 / 16.0F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(4.0F, 4.0F, 3.0F);
            GL11.glTranslatef(-0.125F, 0.125F, 0.0F);
            float f1 = (mapcoord.iconSize % 4 + 0) / 4.0F;
            float f2 = (mapcoord.iconSize / 4 + 0) / 4.0F;
            float f3 = (mapcoord.iconSize % 4 + 1) / 4.0F;
            float f4 = (mapcoord.iconSize / 4 + 1) / 4.0F;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-1.0D, 1.0D, k1 * 0.001F, f1, f2);
            tessellator.addVertexWithUV(1.0D, 1.0D, k1 * 0.001F, f3, f2);
            tessellator.addVertexWithUV(1.0D, -1.0D, k1 * 0.001F, f3, f4);
            tessellator.addVertexWithUV(-1.0D, -1.0D, k1 * 0.001F, f1, f4);
            tessellator.draw();
            GL11.glPopMatrix();
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, -0.04F);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
}
