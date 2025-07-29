package net.minestom.arena.lobby;

import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.ItemFrameMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.map.framebuffers.LargeGraphics2DFramebuffer;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.utils.Direction;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

final class Map {
    private static SendablePacket[] packets = null;

    private Map() {}

    public static SendablePacket[] packets() {
        if (packets != null) return packets;

        try {
            final LargeGraphics2DFramebuffer framebuffer = new LargeGraphics2DFramebuffer(5 * 128, 3 * 128);
            final InputStream imageStream = Lobby.class.getResourceAsStream("/minestom.png");
            assert imageStream != null;
            BufferedImage image = ImageIO.read(imageStream);
            framebuffer.getRenderer().drawRenderedImage(image, AffineTransform.getScaleInstance(1.0, 1.0));
            packets = mapPackets(framebuffer);

            return packets;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the maps on the board in the lobby
     */
    public static void create(@NotNull Instance instance, Point maximum) {
        final int maxX = maximum.blockX();
        final int maxY = maximum.blockY();
        final int z = maximum.blockZ();
        for (int id = 0; id < 15; id++) {
            final int x = maxX - id % 5;
            final int y = maxY - id / 5;

            final Entity itemFrame = new Entity(EntityType.ITEM_FRAME);
            final ItemFrameMeta meta = (ItemFrameMeta) itemFrame.getEntityMeta();
            itemFrame.setInstance(instance, new Pos(x, y, z, 180, 0));
            meta.setNotifyAboutChanges(false);
            meta.setDirection(Direction.NORTH);
            meta.setInvisible(true);
            meta.setItem(ItemStack.builder(Material.FILLED_MAP)
                    .set(DataComponents.MAP_ID, id)
                    .build());
            meta.setNotifyAboutChanges(true);
        }
    }

    /**
     * Creates packets for maps that will display an image on the board in the lobby
     */
    private static SendablePacket[] mapPackets(@NotNull LargeGraphics2DFramebuffer framebuffer) {
        final SendablePacket[] packets = new SendablePacket[15];
        for (int i = 0; i < 15; i++) {
            final int x = i % 5;
            final int y = i / 5;
            packets[i] = framebuffer.createSubView(x * 128, y * 128).preparePacket(i);
        }

        return packets;
    }
}
