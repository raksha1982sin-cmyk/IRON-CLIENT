package com.ironclient.gui;

import com.ironclient.util.IronConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class SpotifyWidget {

    private static String currentTrack = "Not connected";
    private static String currentArtist = "";
    private static boolean playing = false;

    public static void render(DrawContext ctx, MinecraftClient client) {
        if (!IronConfig.spotifyWidgetVisible) return;

        int x = 2;
        int y = client.getWindow().getScaledHeight() - 40;
        int w = 160;
        int h = 36;

        // Background
        ctx.fill(x, y, x + w, y + h, 0xCC1DB954); // Spotify green
        ctx.drawBorder(x, y, w, h, 0xFF1aa34a);

        // Spotify label
        ctx.drawText(client.textRenderer,
            Text.literal("§f♫ Spotify"), x + 4, y + 3, 0xFFFFFFFF, true);

        // Track info
        String track = currentTrack.length() > 18
            ? currentTrack.substring(0, 15) + "..." : currentTrack;
        ctx.drawText(client.textRenderer,
            Text.literal("§f" + track), x + 4, y + 14, 0xFFFFFFFF, false);

        String artist = currentArtist.length() > 22
            ? currentArtist.substring(0, 19) + "..." : currentArtist;
        ctx.drawText(client.textRenderer,
            Text.literal("§7" + artist), x + 4, y + 24, 0xFFCCCCCC, false);

        // Play/pause indicator
        ctx.drawText(client.textRenderer,
            Text.literal(playing ? "§a▶" : "§7⏸"), x + w - 14, y + 14, 0xFFFFFFFF, false);
    }

    public static void setTrack(String track, String artist, boolean isPlaying) {
        currentTrack  = track;
        currentArtist = artist;
        playing       = isPlaying;
    }
}
