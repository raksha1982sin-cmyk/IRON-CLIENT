package com.ironclient.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class KeystrokesHud {

    // Position: bottom-right area
    private static final int KEY_SIZE = 14;
    private static final int GAP = 2;

    public static void render(DrawContext ctx, MinecraftClient client) {
        if (client.player == null) return;

        int screenW = client.getWindow().getScaledWidth();
        int screenH = client.getWindow().getScaledHeight();

        int baseX = screenW - (KEY_SIZE * 3 + GAP * 2) - 4;
        int baseY = screenH - (KEY_SIZE * 3 + GAP * 2) - 4;

        boolean w = client.options.forwardKey.isPressed();
        boolean a = client.options.leftKey.isPressed();
        boolean s = client.options.backKey.isPressed();
        boolean d = client.options.rightKey.isPressed();
        boolean lmb = GLFW.glfwGetMouseButton(client.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean rmb = GLFW.glfwGetMouseButton(client.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
        boolean space = client.options.jumpKey.isPressed();

        // W key (top center)
        drawKey(ctx, client, "W", baseX + KEY_SIZE + GAP, baseY, w);
        // A key (middle left)
        drawKey(ctx, client, "A", baseX, baseY + KEY_SIZE + GAP, a);
        // S key (middle center)
        drawKey(ctx, client, "S", baseX + KEY_SIZE + GAP, baseY + KEY_SIZE + GAP, s);
        // D key (middle right)
        drawKey(ctx, client, "D", baseX + (KEY_SIZE + GAP) * 2, baseY + KEY_SIZE + GAP, d);
        // Space bar (bottom, wide)
        drawWideKey(ctx, client, "___", baseX, baseY + (KEY_SIZE + GAP) * 2, space);
        // LMB
        drawKey(ctx, client, "L", baseX - KEY_SIZE - GAP, baseY, lmb);
        // RMB
        drawKey(ctx, client, "R", baseX + (KEY_SIZE + GAP) * 3, baseY, rmb);
    }

    private static void drawKey(DrawContext ctx, MinecraftClient client,
                                 String label, int x, int y, boolean pressed) {
        int bg    = pressed ? 0xCCFFFFFF : 0x88000000;
        int text  = pressed ? 0xFF000000 : 0xFFFFFFFF;
        ctx.fill(x, y, x + KEY_SIZE, y + KEY_SIZE, bg);
        ctx.drawBorder(x, y, KEY_SIZE, KEY_SIZE, 0xFF555555);
        int tx = x + (KEY_SIZE - client.textRenderer.getWidth(label)) / 2;
        int ty = y + (KEY_SIZE - client.textRenderer.fontHeight) / 2;
        ctx.drawText(client.textRenderer, label, tx, ty, text, false);
    }

    private static void drawWideKey(DrawContext ctx, MinecraftClient client,
                                     String label, int x, int y, boolean pressed) {
        int w     = KEY_SIZE * 3 + GAP * 2;
        int bg    = pressed ? 0xCCFFFFFF : 0x88000000;
        int text  = pressed ? 0xFF000000 : 0xFFFFFFFF;
        ctx.fill(x, y, x + w, y + KEY_SIZE, bg);
        ctx.drawBorder(x, y, w, KEY_SIZE, 0xFF555555);
        int tx = x + (w - client.textRenderer.getWidth("SPC")) / 2;
        int ty = y + (KEY_SIZE - client.textRenderer.fontHeight) / 2;
        ctx.drawText(client.textRenderer, "SPC", tx, ty, text, false);
    }
}
