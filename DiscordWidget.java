package com.ironclient.gui;

import com.ironclient.util.IronConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class DiscordWidget {

    private static final List<String> messages = new ArrayList<>();
    private static String channelName = "#general";

    static {
        // Placeholder messages
        messages.add("§7Connect Discord in settings");
    }

    public static void render(DrawContext ctx, MinecraftClient client) {
        if (!IronConfig.discordWidgetVisible) return;

        int w = 180;
        int h = 100;
        int x = client.getWindow().getScaledWidth() - w - 2;
        int y = 2;

        // Background
        ctx.fill(x, y, x + w, y + h, 0xCC36393F); // Discord dark
        ctx.drawBorder(x, y, w, h, 0xFF202225);

        // Header
        ctx.fill(x, y, x + w, y + 14, 0xCC202225);
        ctx.drawText(client.textRenderer,
            Text.literal("§9§l⊕ Discord §7| §f" + channelName),
            x + 3, y + 3, 0xFFFFFFFF, false);

        // Messages
        int my = y + 16;
        int shown = 0;
        for (int i = Math.max(0, messages.size() - 4); i < messages.size(); i++) {
            String msg = messages.get(i);
            if (msg.length() > 25) msg = msg.substring(0, 22) + "...";
            ctx.drawText(client.textRenderer,
                Text.literal("§7" + msg), x + 3, my, 0xFFCCCCCC, false);
            my += 10;
            shown++;
        }

        // Footer
        ctx.drawText(client.textRenderer,
            Text.literal("§8[Connect in /ironclient discord]"),
            x + 3, y + h - 10, 0xFF666666, false);
    }

    public static void addMessage(String user, String content) {
        messages.add("§b" + user + "§f: " + content);
        if (messages.size() > 20) messages.remove(0);
    }

    public static void setChannel(String channel) {
        channelName = channel;
    }
}
