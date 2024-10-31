package network.manu.manumod;

import net.minecraft.util.ResourceLocation;
import org.ejml.data.Complex64F;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.MinecraftForge;
import network.manu.manumod.blocks.BlockEnderite;
import network.manu.manumod.blocks.BlockEnderiteDebris;
import network.manu.manumod.blocks.BlockEnderiteDebrisCracked;
import network.manu.manumod.blocks.BlockEnderiteDebrisCrackedRenderer;
import network.manu.manumod.eventhandlers.ClientBusEventHandler;
import network.manu.manumod.eventhandlers.EventBusEventHandler;
import network.manu.manumod.items.*;
import network.manu.manumod.proxy.IProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import network.manu.manumod.worldgen.EnderiteDebrisWorldGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(
    modid = ManuMod.MODID,
    version = Tags.VERSION,
    name = ManuMod.MODNAME,
    acceptedMinecraftVersions = "[1.7.10]"
)
public class ManuMod {
    public static final String MODID = "manumod";
    public static final String MODGROUP = "network.manu.manumod";
    public static final String MODNAME = "ManuMod";
    public static Logger log = LogManager.getLogger(MODID);

    public static ResourceLocation getResource(String name) {
        return new ResourceLocation(ManuMod.MODID, name);
    }

    public static String TEXTURE_PREFIX = MODID + ":";

    public static Item.ToolMaterial enderiteToolMaterial;
    public static ItemArmor.ArmorMaterial enderiteArmorMaterial;

    public static ItemArmor.ArmorMaterial alloyArmorMaterial;

    public static Item itemPartAlloy;
    public static Item itemAlloyHelmet;
    public static Item itemAlloyLeggings;
    public static Item itemAlloyBoots;

    public static Item itemEnderiteIngot;
    public static Item itemEnderiteScrap;
    public static Item itemEnderiteSword;
    public static Item itemEnderiteShovel;
    public static Item itemEnderiteArmorHelmet;
    public static Item itemEnderiteArmorChestplate;
    public static Item itemEnderitearmorLeggings;
    public static Item itemEnderiteAxe;
    public static Item itemEnderitePickaxe;
    public static Item itemEnderiteHoe;
    public static Item itemEnderiteArmorBoots;
    public static Item itemArmorTrimSmithingTemplateSentry;
    public static Item itemArmorTrimSmithingTemplateVex;
    public static Item itemArmorTrimSmithingTemplateWild;
    public static Item itemArmorTrimSmithingTemplateCoast;
    public static Item itemArmorTrimSmithingTemplateDune;
    public static Item itemArmorTrimSmithingTemplateWayfinder;
    public static Item itemArmorTrimSmithingTemplateRaiser;
    public static Item itemArmorTrimSmithingTemplateShaper;
    public static Item itemArmorTrimSmithingTemplateHost;
    public static Item itemArmorTrimSmithingTemplateWard;
    public static Item itemArmorTrimSmithingTemplateSilence;
    public static Item itemArmorTrimSmithingTemplateTide;
    public static Item itemArmorTrimSmithingTemplateSnout;
    public static Item itemArmorTrimSmithingTemplateRib;
    public static Item itemArmorTrimSmithingTemplateEye;
    public static Item itemArmorTrimSmithingTemplateSpire;
    public static Item itemArmorUpgradeSmithingTemplateNetherite;
    public static Item itemArmorUpgradeSmithingTemplateEnderite;

    public static Block blockEnderite = new BlockEnderite();
    public static Block blockEnderiteDebris = new BlockEnderiteDebris();
    public static Block blockEnderiteDebrisCracked;

    public static int ENDERITE_DEBRIS_CRACKED_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
    public static BlockEnderiteDebrisCrackedRenderer blockEnderiteDebrisCrackedRenderer;

    public static EnderiteDebrisWorldGenerator enderiteDebrisWorldGenerator = new EnderiteDebrisWorldGenerator();

    @SidedProxy(
        clientSide= MODGROUP + ".proxy.ClientProxy",
        serverSide= MODGROUP + ".proxy.ServerProxy"
    )
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        log.info("preInit " + MODNAME + " version " + Tags.VERSION);
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
        proxy.preInit(event);

        enderiteToolMaterial =
            EnumHelper.addToolMaterial(
                "Enderite_Tool",
                5,
                2441,
                10,
                5.0f,
                20
            );
        enderiteArmorMaterial =
            EnumHelper.addArmorMaterial(
                "Enderite_Armor",
                41,
                new int[]{3, 8, 6, 3},
                20
            );
        alloyArmorMaterial =
            EnumHelper.addArmorMaterial(
                "Composite_Armor",
                39,
                new int[]{3, 7, 6, 3},
                7
            );

        itemEnderiteIngot = new ItemEnderiteIngot();
        itemEnderiteScrap = new ItemEnderiteScrap();
        itemEnderiteSword = new ItemEnderiteSword(enderiteToolMaterial);
        itemEnderiteAxe = new ItemEnderiteAxe(enderiteToolMaterial);
        itemEnderitePickaxe = new ItemEnderitePickaxe(enderiteToolMaterial);
        itemEnderiteHoe = new ItemEnderiteHoe(enderiteToolMaterial);
        itemEnderiteShovel = new ItemEnderiteShovel(enderiteToolMaterial);
        itemEnderiteArmorHelmet = new ItemEnderiteArmorHelmet(enderiteArmorMaterial);
        itemEnderiteArmorChestplate = new ItemEnderiteArmorChestplate(enderiteArmorMaterial);
        itemEnderitearmorLeggings = new ItemEnderiteArmorLeggings(enderiteArmorMaterial);
        itemEnderiteArmorBoots = new ItemEnderiteArmorBoots(enderiteArmorMaterial);
        itemAlloyHelmet = new ItemAlloyHelmet(alloyArmorMaterial);
        itemAlloyLeggings = new ItemAlloyLeggings(alloyArmorMaterial);
        itemAlloyBoots = new ItemAlloyBoots(alloyArmorMaterial);
        itemArmorTrimSmithingTemplateSentry = new ItemArmorTrimSmithingTemplateSentry();
        itemArmorTrimSmithingTemplateWild = new ItemArmorTrimSmithingTemplateWild();
        itemArmorTrimSmithingTemplateVex = new ItemArmorTrimSmithingTemplateVex();
        itemArmorTrimSmithingTemplateCoast = new ItemArmorTrimSmithingTemplateCoast();
        itemArmorTrimSmithingTemplateDune = new ItemArmorTrimSmithingTemplateDune();
        itemArmorTrimSmithingTemplateWayfinder = new ItemArmorTrimSmithingTemplateWayfinder();
        itemArmorTrimSmithingTemplateRaiser = new ItemArmorTrimSmithingTemplateRaiser();
        itemArmorTrimSmithingTemplateShaper = new ItemArmorTrimSmithingTemplateShaper();
        itemArmorTrimSmithingTemplateHost = new ItemArmorTrimSmithingTemplateHost();
        itemArmorTrimSmithingTemplateWard = new ItemArmorTrimSmithingTemplateWard();
        itemArmorTrimSmithingTemplateSilence = new ItemArmorTrimSmithingTemplateSilence();
        itemArmorTrimSmithingTemplateTide = new ItemArmorTrimSmithingTemplateTide();
        itemArmorTrimSmithingTemplateSnout = new ItemArmorTrimSmithingTemplateSnout();
        itemArmorTrimSmithingTemplateRib = new ItemArmorTrimSmithingTemplateRib();
        itemArmorTrimSmithingTemplateEye = new ItemArmorTrimSmithingTemplateEye();
        itemArmorTrimSmithingTemplateSpire = new ItemArmorTrimSmithingTemplateSpire();
        itemArmorUpgradeSmithingTemplateNetherite = new ItemArmorUpgradeSmithingTemplateNetherite();
        itemArmorUpgradeSmithingTemplateEnderite = new ItemArmorUpgradeSmithingTemplateEnderite();

        GameRegistry.registerItem(itemEnderiteIngot, ItemEnderiteIngot.ID);
        GameRegistry.registerItem(itemEnderiteScrap, ItemEnderiteScrap.ID);
        GameRegistry.registerItem(itemEnderiteSword, ItemEnderiteSword.ID);
        GameRegistry.registerItem(itemEnderiteAxe, ItemEnderiteAxe.ID);
        GameRegistry.registerItem(itemEnderitePickaxe, ItemEnderitePickaxe.ID);
        GameRegistry.registerItem(itemEnderiteHoe, ItemEnderiteHoe.ID);
        GameRegistry.registerItem(itemEnderiteShovel, ItemEnderiteShovel.ID);
        GameRegistry.registerItem(itemEnderiteArmorHelmet, ItemEnderiteArmorHelmet.ID);
        GameRegistry.registerItem(itemEnderiteArmorChestplate, ItemEnderiteArmorChestplate.ID);
        GameRegistry.registerItem(itemEnderitearmorLeggings, ItemEnderiteArmorLeggings.ID);
        GameRegistry.registerItem(itemEnderiteArmorBoots, ItemEnderiteArmorBoots.ID);
        GameRegistry.registerItem(itemAlloyHelmet, ItemAlloyHelmet.ID);
        GameRegistry.registerItem(itemAlloyLeggings, ItemAlloyLeggings.ID);
        GameRegistry.registerItem(itemAlloyBoots, ItemAlloyBoots.ID);
        GameRegistry.registerItem(itemArmorTrimSmithingTemplateSentry, ItemArmorTrimSmithingTemplateSentry.ID);
        GameRegistry.registerItem(itemArmorTrimSmithingTemplateWild, ItemArmorTrimSmithingTemplateWild.ID);
        GameRegistry.registerItem(itemArmorTrimSmithingTemplateVex, ItemArmorTrimSmithingTemplateVex.ID);
        GameRegistry.registerItem(itemArmorTrimSmithingTemplateCoast, ItemArmorTrimSmithingTemplateCoast.ID);
        GameRegistry.registerItem(itemArmorTrimSmithingTemplateDune, ItemArmorTrimSmithingTemplateDune.ID);
        GameRegistry.registerItem(itemArmorTrimSmithingTemplateWayfinder, ItemArmorTrimSmithingTemplateWayfinder.ID);
        GameRegistry.registerItem(itemArmorTrimSmithingTemplateRaiser, ItemArmorTrimSmithingTemplateRaiser.ID);
        GameRegistry.registerItem(itemArmorTrimSmithingTemplateShaper, ItemArmorTrimSmithingTemplateShaper.ID);
        GameRegistry.registerItem(itemArmorTrimSmithingTemplateHost, ItemArmorTrimSmithingTemplateHost.ID);
        GameRegistry.registerItem(itemArmorTrimSmithingTemplateWard, ItemArmorTrimSmithingTemplateWard.ID);
        GameRegistry.registerItem(itemArmorTrimSmithingTemplateSilence, ItemArmorTrimSmithingTemplateSilence.ID);
        GameRegistry.registerItem(itemArmorTrimSmithingTemplateTide, ItemArmorTrimSmithingTemplateTide.ID);
        GameRegistry.registerItem(itemArmorTrimSmithingTemplateSnout, ItemArmorTrimSmithingTemplateSnout.ID);
        GameRegistry.registerItem(itemArmorTrimSmithingTemplateRib, ItemArmorTrimSmithingTemplateRib.ID);
        GameRegistry.registerItem(itemArmorTrimSmithingTemplateEye, ItemArmorTrimSmithingTemplateEye.ID);
        GameRegistry.registerItem(itemArmorTrimSmithingTemplateSpire, ItemArmorTrimSmithingTemplateSpire.ID);
        GameRegistry.registerItem(itemArmorUpgradeSmithingTemplateNetherite, ItemArmorUpgradeSmithingTemplateNetherite.ID);
        GameRegistry.registerItem(itemArmorUpgradeSmithingTemplateEnderite, ItemArmorUpgradeSmithingTemplateEnderite.ID);

        blockEnderite = new BlockEnderite();
        blockEnderiteDebris = new BlockEnderiteDebris();
        ENDERITE_DEBRIS_CRACKED_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
        enderiteDebrisWorldGenerator = new EnderiteDebrisWorldGenerator();

        GameRegistry.registerBlock(blockEnderite, BlockEnderite.ID);
        GameRegistry.registerBlock(blockEnderiteDebris, BlockEnderiteDebris.ID);
        GameRegistry.registerWorldGenerator(enderiteDebrisWorldGenerator, 100);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        log.info("init " + MODNAME + " version " + Tags.VERSION);
        proxy.init(event);

        blockEnderiteDebrisCrackedRenderer = new BlockEnderiteDebrisCrackedRenderer(ENDERITE_DEBRIS_CRACKED_RENDER_ID);
        RenderingRegistry.registerBlockHandler(blockEnderiteDebrisCrackedRenderer);
        blockEnderiteDebrisCracked = new BlockEnderiteDebrisCracked();
        GameRegistry.registerBlock(blockEnderiteDebrisCracked, BlockEnderiteDebrisCracked.ID);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        log.info("postInit " + MODNAME + " version " + Tags.VERSION);
        proxy.postInit(event);

        MinecraftForge.EVENT_BUS.register(new EventBusEventHandler());

        GameRegistry.addSmelting(
            new ItemStack(blockEnderiteDebrisCracked),
            new ItemStack(itemEnderiteScrap),
            2.0F
        );

        itemPartAlloy = GameRegistry.findItem("IC2", "itemPartAlloy");
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        log.info("serverStarting " + MODNAME + " version " + Tags.VERSION);
        proxy.serverStarting(event);
    }
}
