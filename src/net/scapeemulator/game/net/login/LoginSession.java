package net.scapeemulator.game.net.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.io.IOException;
import java.security.SecureRandom;
import net.burtleburtle.bob.rand.IsaacRandom;
import net.scapeemulator.cache.ChecksumTable;
import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.model.player.interfaces.InterfaceSet.DisplayMode;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.net.RsChannelHandler;
import net.scapeemulator.game.net.Session;
import net.scapeemulator.game.net.game.GameFrameDecoder;
import net.scapeemulator.game.net.game.GameFrameEncoder;
import net.scapeemulator.game.net.game.GameMessageDecoder;
import net.scapeemulator.game.net.game.GameMessageEncoder;
import net.scapeemulator.game.net.game.GameSession;

public final class LoginSession extends Session {

    private static final SecureRandom random = new SecureRandom();

    private final LoginService service;
    private final long serverSessionKey = random.nextLong();
    private int displayMode;
    private IsaacRandom inRandom, outRandom;

    public LoginSession(GameServer server, Channel channel) {
        super(server, channel);
        this.service = server.getLoginService();
        init();
    }

    private void init() {
        ByteBuf buf = channel.alloc().buffer(8);
        buf.writeLong(serverSessionKey);
        channel.write(new LoginResponse(LoginResponse.STATUS_EXCHANGE_KEYS, buf));
    }

    public void sendLoginFailure(int status) {
        LoginResponse response = new LoginResponse(status);
        channel.write(response).addListener(ChannelFutureListener.CLOSE);
    }

    public void sendLoginSuccess(int status, final Player player) {
        ByteBuf buf = channel.alloc().buffer(11);
        buf.writeByte(player.getRights());
        buf.writeByte(0);
        buf.writeByte(0);
        buf.writeByte(0);
        buf.writeByte(0);
        buf.writeByte(0);
        buf.writeByte(0);
        buf.writeShort(player.getId());
        buf.writeByte(1);
        buf.writeByte(1);
        if (displayMode == 0 || displayMode == 1) {
            player.getInterfaceSet().setDisplayMode(DisplayMode.FIXED);
        } else {
            player.getInterfaceSet().setDisplayMode(DisplayMode.RESIZABLE);
        }
        final ChannelPipeline pipeline = channel.pipeline();
        final GameSession session = new GameSession(server, channel, player);
        RsChannelHandler handler = pipeline.get(RsChannelHandler.class);
        handler.setSession(session);
        pipeline.remove(ReadTimeoutHandler.class);
        LoginResponse response = new LoginResponse(status, buf);
        ChannelFuture future = channel.write(response);
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) {
                if (future.isSuccess()) {
                    pipeline.addFirst(new GameFrameEncoder(outRandom), new GameMessageEncoder(server.getCodecRepository()), new GameFrameDecoder(inRandom),
                            new GameMessageDecoder(server.getCodecRepository()));

                    session.init();
                }
            }
        });
    }

    @Override
    public void messageReceived(Object message) throws IOException {
        LoginRequest request = (LoginRequest) message;
        if (request.getServerSessionKey() != serverSessionKey)
            throw new IOException("Server session key mismatch.");
        boolean versionMismatch = false;
        if (request.getVersion() != server.getVersion())
            versionMismatch = true;
        ChecksumTable table = server.getChecksumTable();
        int[] crc = request.getCrc();
        for (int i = 0; i < crc.length; i++) {
            if (table.getEntry(i).getCrc() != crc[i]) {
                versionMismatch = true;
                break;
            }
        }
        if (versionMismatch) {
            sendLoginFailure(LoginResponse.STATUS_GAME_UPDATED);
            return;
        }
        long clientSessionKey = request.getClientSessionKey();
        long serverSessionKey = request.getServerSessionKey();
        int[] seed = new int[4];
        seed[0] = (int) (clientSessionKey >> 32);
        seed[1] = (int) clientSessionKey;
        seed[2] = (int) (serverSessionKey >> 32);
        seed[3] = (int) serverSessionKey;
        inRandom = new IsaacRandom(seed);
        for (int i = 0; i < seed.length; i++)
            seed[i] += 50;
        outRandom = new IsaacRandom(seed);
        displayMode = request.getDisplayMode();

        service.addLoginRequest(this, request);
    }

}
