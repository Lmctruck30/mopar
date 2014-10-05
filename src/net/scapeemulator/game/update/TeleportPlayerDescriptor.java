package net.scapeemulator.game.update;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.impl.PlayerUpdateMessage;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class TeleportPlayerDescriptor extends PlayerDescriptor {

    public TeleportPlayerDescriptor(Player player, int[] tickets) {
        super(player, tickets);
    }

    @Override
    public void encodeDescriptor(PlayerUpdateMessage message, GameFrameBuilder builder, GameFrameBuilder blockBuilder) {
        Position lastKnownRegion = message.getLastKnownRegion();
        Position position = message.getPosition();

        int x = position.getLocalX(lastKnownRegion.getRegionX());
        int y = position.getLocalY(lastKnownRegion.getRegionY());
        int height = position.getHeight() % 4;

        builder.putBits(1, 1);
        builder.putBits(2, 3);
        builder.putBits(7, y);
        builder.putBits(1, 1);
        builder.putBits(2, height);
        builder.putBits(1, isBlockUpdatedRequired() ? 1 : 0);
        builder.putBits(7, x);
    }

}
