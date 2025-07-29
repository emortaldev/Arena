package net.minestom.arena;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.arena.config.ConfigHandler;
import net.minestom.arena.config.ConfigurationReloadedEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.ping.Status;

import java.io.InputStream;
import java.util.List;

final class ServerList {
    private static final byte[] FAVICON = favicon();
    private static Component MOTD = motd();

    public static void hook(EventNode<Event> eventNode) {
        eventNode.addListener(ServerListPingEvent.class, event -> {
            int onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers().size();

            Status.Builder builder = Status.builder()
                    .description(MOTD)
                    .favicon(FAVICON)
                    .playerInfo(onlinePlayers, 100);

            if (FAVICON != null) builder.favicon(FAVICON);

            event.setStatus(builder.build());
        }).addListener(ConfigurationReloadedEvent.class, e -> MOTD = motd());
    }

    private static byte[] favicon() {
        byte[] favicon = null;
        try (InputStream stream = Main.class.getResourceAsStream("/favicon.png")) {
            if (stream != null)
                favicon = stream.readAllBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return favicon;
    }

    private static Component motd() {
        final MiniMessage miniMessage = MiniMessage.miniMessage();
        final List<String> motd = ConfigHandler.CONFIG.server().motd();
        return motd.stream()
            .map(miniMessage::deserialize)
            .reduce(Component.empty(), (a, b) -> a.append(b).appendNewline());
    }
}
