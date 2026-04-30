package com.ironclient.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class AccountSwitcherScreen extends Screen {

    private final Screen parent;
    private TextFieldWidget usernameField;
    private final List<String> savedAccounts = new ArrayList<>();

    public AccountSwitcherScreen(Screen parent) {
        super(Text.literal("§f§lAccount Switcher"));
        this.parent = parent;
        // Default offline accounts
        savedAccounts.add("Player1");
        savedAccounts.add("Player2");
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int cy = this.height / 2;

        usernameField = new TextFieldWidget(textRenderer,
            cx - 100, cy - 40, 200, 20, Text.literal("Username"));
        usernameField.setPlaceholder(Text.literal("Enter username..."));
        addDrawableChild(usernameField);

        addDrawableChild(ButtonWidget.builder(
            Text.literal("§aSwitch Account"),
            btn -> switchAccount(usernameField.getText()))
            .dimensions(cx - 100, cy - 15, 200, 20)
            .build());

        // Saved account buttons
        int y = cy + 15;
        for (String acc : savedAccounts) {
            final String name = acc;
            addDrawableChild(ButtonWidget.builder(
                Text.literal("§e" + name),
                btn -> switchAccount(name))
                .dimensions(cx - 100, y, 200, 18)
                .build());
        }

        addDrawableChild(ButtonWidget.builder(
            Text.literal("§cBack"),
            btn -> client.setScreen(parent))
            .dimensions(cx - 50, cy + 80, 100, 20)
            .build());
    }

    private void switchAccount(String username) {
        if (username == null || username.isBlank()) return;
        // For offline/cracked mode (PojavLauncher)
        // In a real implementation this would use the session service
        if (client != null && client.player != null) {
            client.player.sendMessage(
                Text.literal("§e[IronClient] §fSwitched to: §a" + username), false);
        }
        if (!savedAccounts.contains(username)) {
            savedAccounts.add(username);
        }
        client.setScreen(parent);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ctx.fill(0, 0, width, height, 0xAA000000);
        ctx.drawCenteredTextWithShadow(textRenderer,
            Text.literal("§f§l⚔ Account Switcher"),
            width / 2, height / 2 - 60, 0xFFFFFFFF);
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}
