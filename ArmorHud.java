package com.ironclient.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class ArmorHud {

    private static final int X = 2;
    private static final int Y_START = 2;
    private static final int SLOT_SIZE = 18;

    public static void render(DrawContext ctx, MinecraftClient client) {
        if (client.player == null) return;

        EquipmentSlot[] slots = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
        };

        int y = Y_START;
        for (EquipmentSlot slot : slots) {
            ItemStack stack = client.player.getEquippedStack(slot);
            if (stack.isEmpty()) {
                y += SLOT_SIZE;
                continue;
            }

            // Draw item
            ctx.drawItem(stack, X, y);
            ctx.drawItemInSlot(client.textRenderer, stack, X, y);

            // Draw durability bar if damaged
            if (stack.isDamageable() && stack.isDamaged()) {
                int maxDur = stack.getMaxDamage();
                int curDur = maxDur - stack.getDamage();
                int percent = (int)((curDur / (float) maxDur) * 100);

                // Color: green > yellow > red
                int color;
                if (percent > 60)      color = 0xFF00FF00;
                else if (percent > 30) color = 0xFFFFFF00;
                else                   color = 0xFFFF0000;

                String durStr = curDur + "/" + maxDur;
                ctx.drawText(client.textRenderer, durStr,
                    X + SLOT_SIZE + 2, y + 4, color, true);
            } else {
                ctx.drawText(client.textRenderer, "Full",
                    X + SLOT_SIZE + 2, y + 4, 0xFF00FF00, true);
            }

            y += SLOT_SIZE;
        }
    }
}
