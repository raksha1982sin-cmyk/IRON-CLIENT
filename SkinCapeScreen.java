package com.ironclient.gui;

import com.ironclient.util.IronConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class SkinCapeScreen extends Screen {

    private final Screen parent;
    private TextFieldWidget skinUrlField;
    private TextFieldWidget capeUrlField;

    public SkinCapeScreen(Screen parent) {
        super(Text.literal("§f§lSkin & Cape Changer"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int cy = this.height / 2;

        // Skin URL field
        skinUrlField = new TextFieldWidget(textRenderer,
            cx - 120, cy - 50, 240, 20, Text.literal("Skin URL"));
        skinUrlField.setPlaceholder(Text.literal("Skin URL (https://...)"));
        skinUrlField.setText(IronConfig.customSkinUrl);
        addDrawableChild(skinUrlField);

        // Cape URL field
        capeUrlField = new TextFieldWidget(textRenderer,
            cx - 120, cy - 20, 240, 20, Text.literal("Cape URL"));
        capeUrlField.setPlaceholder(Text.literal("Cape URL (https://...)"));
        capeUrlField.setText(IronConfig.customCapeUrl);
        addDrawableChild(capeUrlField);

        addDrawableChild(ButtonWidget.builder(
            Text.literal("§aApply Skin & Cape"),
            btn -> applySkinCape())
            .dimensions(cx - 80, cy + 10, 160, 20)
            .build());

        addDrawableChild(ButtonWidget.builder(
            Text.literal("§cReset to Default"),
            btn -> resetSkinCape())
            .dimensions(cx - 80, cy + 35, 160, 20)
            .build());

        addDrawableChild(ButtonWidget.builder(
            Text.literal("§7Back"),
            btn -> client.setScreen(parent))
            .dimensions(cx - 50, cy + 65, 100, 20)
            .build());
    }

    private void applySkinCape() {
        IronConfig.customSkinUrl = skinUrlField.getText();
        IronConfig.customCapeUrl = capeUrlField.getText();
        IronConfig.save();

        // Notify player
        if (client != null && client.player != null) {
            client.player.sendMessage(
                Text.literal("§e[IronClient] §fSkin & Cape applied! (client-side)"), false);
        }
        client.setScreen(parent);
    }

    private void resetSkinCape() {
        IronConfig.customSkinUrl = "";
        IronConfig.customCapeUrl = "";
        IronConfig.save();
        skinUrlField.setText("");
        capeUrlField.setText("");
        if (client != null && client.player != null) {
            client.player.sendMessage(
                Text.literal("§e[IronClient] §fSkin & Cape reset to default."), false);
        }
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ctx.fill(0, 0, width, height, 0xAA000000);
        ctx.drawCenteredTextWithShadow(textRenderer,
            Text.literal("§f§l⚔ Skin & Cape Changer"),
            width / 2, height / 2 - 70, 0xFFFFFFFF);
        ctx.drawCenteredTextWithShadow(textRenderer,
            Text.literal("§7Paste direct image URLs (.png)"),
            width / 2, height / 2 - 58, 0xFFAAAAAA);
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}
