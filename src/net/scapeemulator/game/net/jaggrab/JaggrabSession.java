package net.scapeemulator.game.net.jaggrab;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.FileRegion;

import java.io.IOException;

import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.net.Session;
import net.scapeemulator.game.net.file.FileProvider;

public final class JaggrabSession extends Session {

    private static final FileProvider provider = new FileProvider("data/game/jaggrab/", true);

    public JaggrabSession(GameServer server, Channel channel) {
        super(server, channel);
    }

    @Override
    public void messageReceived(Object message) throws IOException {
        JaggrabRequest request = (JaggrabRequest) message;
        FileRegion file = provider.serve(request.getPath());
        if (file != null) {
            channel.write(file).addListener(ChannelFutureListener.CLOSE);
        } else {
            channel.close();
        }
    }

}
