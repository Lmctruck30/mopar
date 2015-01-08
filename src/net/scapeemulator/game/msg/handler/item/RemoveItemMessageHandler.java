package net.scapeemulator.game.msg.handler.item;

import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.interfaces.Interface;
import net.scapeemulator.game.model.player.skills.construction.furniture.FurnitureInterface;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.RemoveItemMessage;

public final class RemoveItemMessageHandler extends MessageHandler<RemoveItemMessage> {

    @Override
    public void handle(Player player, RemoveItemMessage message) {
        if (player.actionsBlocked()) {
            return;
        }
        if (message.getId() == Interface.EQUIPMENT && message.getChildId() == 28) {
            Item item = player.getEquipment().get(message.getItemSlot());
            if (item == null || item.getId() != message.getItemId()) {
                return;
            }
            Equipment.remove(player, message.getItemSlot());
        } else if (message.getId() == FurnitureInterface.NORMAL.getWindowId() || message.getId() == FurnitureInterface.EXTENDED.getWindowId()) {
            if (player.getHouse().getBuildingSession() != null) {
                player.getHouse().getBuildingSession().handleFurnitureBuildInterface(message.getItemSlot());
            }
        } else {
            System.out.println("Unhandled remove item message: " + message.getId() + ", " + message.getItemSlot() + " " + message.getChildId());
        }
    }

}
