package network.manu.manumod.eventhandlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import network.manu.manumod.items.ItemArmorTrimSmithingTemplateCoast;
import network.manu.manumod.items.ItemArmorTrimSmithingTemplateSentry;

public class ClientBusEventHandler {

    @SubscribeEvent
    public void onItemTooltipEvent(ItemTooltipEvent event) {
        Item item = event.itemStack.getItem();
        if (item instanceof ItemArmorTrimSmithingTemplateCoast) {
            event.toolTip.add(I18n.format("item.armor_trim_smithing_template_coast.line2"));
        }
        if (item instanceof ItemArmorTrimSmithingTemplateSentry) {
            event.toolTip.add("dstn");
        }
    }
}
