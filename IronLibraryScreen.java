package com.ironclient.gui;

import com.ironclient.util.IronConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class IronLibraryScreen extends Screen {

    // Categories
    private static final String[] CATEGORIES = {"Combat", "Visual", "HUD", "World", "Social", "Util"};
    private int selectedCategory = 0;

    // Scroll
    private int scrollOffset = 0;

    // Panel sizes
    private int panelX, panelY, panelW, panelH;
    private int catW = 90;
    private int itemH = 22;

    private final List<FeatureEntry> features = new ArrayList<>();

    public IronLibraryScreen() {
        super(Text.literal("§8§l⚔ §f§lIRON CLIENT §8§l⚔"));
    }

    @Override
    protected void init() {
        features.clear();
        panelW = Math.min(500, this.width - 40);
        panelH = Math.min(300, this.height - 60);
        panelX = (this.width - panelW) / 2;
        panelY = (this.height - panelH) / 2;

        buildFeatureList();
        buildWidgets();
    }

    private void buildFeatureList() {
        features.add(new FeatureEntry("combat", "Combat Toggle",
            "Toggle FOV Effect OFF + No Crystal Particles + No Damage Tilt",
            () -> IronConfig.combatToggle, v -> {
                IronConfig.combatToggle       = v;
                IronConfig.fovEffectDisabled  = v;
                IronConfig.noCrystalParticles = v;
                IronConfig.damageTiltDisabled = v;
            }));

        features.add(new FeatureEntry("combat", "Toggle Scoreboard",
            "Show or hide the scoreboard sidebar",
            () -> IronConfig.scoreboardVisible, v -> IronConfig.scoreboardVisible = v));

        features.add(new FeatureEntry("visual", "Motion Blur",
            "Smooth motion blur while moving",
            () -> IronConfig.motionBlurEnabled, v -> IronConfig.motionBlurEnabled = v));

        features.add(new FeatureEntry("visual", "Fullbright + Night Vision",
            "Max brightness and permanent night vision",
            () -> IronConfig.fullbrightEnabled, v -> {
                IronConfig.fullbrightEnabled  = v;
                IronConfig.nightVisionEnabled = v;
            }));

        features.add(new FeatureEntry("visual", "Custom Block Highlight",
            "Enable custom block outline color",
            () -> IronConfig.blockHighlightEnabled, v -> IronConfig.blockHighlightEnabled = v));

        features.add(new FeatureEntry("hud", "Armor HUD",
            "Shows armor pieces and durability on screen",
            () -> IronConfig.armorHudEnabled, v -> IronConfig.armorHudEnabled = v));

        features.add(new FeatureEntry("hud", "Keystrokes",
            "Shows WASD and mouse clicks on screen",
            () -> IronConfig.keystrokesEnabled, v -> IronConfig.keystrokesEnabled = v));

        features.add(new FeatureEntry("world", "Time: Day",
            "Set world time to day (6000)",
            () -> IronConfig.timeOfDay == 6000, v -> { if (v) setTime(6000); }));

        features.add(new FeatureEntry("world", "Time: Night",
            "Set world time to night (18000)",
            () -> IronConfig.timeOfDay == 18000, v -> { if (v) setTime(18000); }));

        features.add(new FeatureEntry("world", "Time: Noon",
            "Set world time to noon (12000)",
            () -> IronConfig.timeOfDay == 12000, v -> { if (v) setTime(12000); }));

        features.add(new FeatureEntry("social", "Spotify Widget",
            "Show Spotify now-playing widget",
            () -> IronConfig.spotifyWidgetVisible, v -> IronConfig.spotifyWidgetVisible = v));

        features.add(new FeatureEntry("social", "Discord Widget",
            "Show Discord chat overlay",
            () -> IronConfig.discordWidgetVisible, v -> IronConfig.discordWidgetVisible = v));

        features.add(new FeatureEntry("util", "Texture Pack Installer",
            "Open texture pack browser",
            () -> false, v -> openTexturePacks()));

        features.add(new FeatureEntry("util", "Account Switcher",
            "Switch Minecraft accounts in-game",
            () -> false, v -> openAccountSwitcher()));

        features.add(new FeatureEntry("util", "Skin & Cape Changer",
            "Change your skin/cape (client + server side)",
            () -> false, v -> openSkinChanger()));
    }

    private void buildWidgets() {
        clearChildren();

        // Category buttons
        for (int i = 0; i < CATEGORIES.length; i++) {
            final int idx = i;
            int bx = panelX + 4 + i * (catW - 10);
            // Fit all categories in one row
            int catBtnW = (panelW - 8) / CATEGORIES.length;
            bx = panelX + 4 + i * catBtnW;
            addDrawableChild(ButtonWidget.builder(
                Text.literal(CATEGORIES[i]),
                btn -> { selectedCategory = idx; scrollOffset = 0; buildWidgets(); })
                .dimensions(bx, panelY + 22, catBtnW - 2, 16)
                .build());
        }

        // Feature toggle buttons for selected category
        String cat = CATEGORIES[selectedCategory].toLowerCase();
        int y = panelY + 42 - scrollOffset;
        for (FeatureEntry f : features) {
            if (!f.category.equals(cat)) continue;
            if (y + itemH > panelY + panelH - 4 || y < panelY + 42) {
                y += itemH + 2;
                continue;
            }
            final FeatureEntry fe = f;
            boolean on = fe.getter.get();
            addDrawableChild(ButtonWidget.builder(
                Text.literal((on ? "§a■ " : "§c□ ") + fe.name),
                btn -> {
                    fe.setter.accept(!fe.getter.get());
                    IronConfig.save();
                    buildWidgets();
                })
                .dimensions(panelX + 4, y, panelW - 90, itemH)
                .build());
            y += itemH + 2;
        }

        // Fullbright slider (only in visual tab)
        if (CATEGORIES[selectedCategory].equalsIgnoreCase("visual")) {
            int sliderY = panelY + panelH - 28;
            addDrawableChild(new SliderWidget(
                panelX + 4, sliderY, panelW - 8, 18,
                Text.literal("Brightness: " + (int)(IronConfig.fullbrightLevel * 100) + "%"),
                IronConfig.fullbrightLevel) {
                @Override protected void updateMessage() {
                    setMessage(Text.literal("Brightness: " + (int)(this.value * 100) + "%"));
                }
                @Override protected void applyValue() {
                    IronConfig.fullbrightLevel = (float) this.value;
                    if (IronConfig.fullbrightEnabled) {
                        client.options.getGamma().setValue(this.value * 16.0);
                    }
                    IronConfig.save();
                }
            });
        }

        // Close button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("§cClose"),
            btn -> this.close())
            .dimensions(panelX + panelW - 54, panelY + 4, 50, 14)
            .build());

        // Scroll buttons
        addDrawableChild(ButtonWidget.builder(
            Text.literal("▲"),
            btn -> { scrollOffset = Math.max(0, scrollOffset - (itemH + 2)); buildWidgets(); })
            .dimensions(panelX + panelW - 14, panelY + 42, 12, 14)
            .build());
        addDrawableChild(ButtonWidget.builder(
            Text.literal("▼"),
            btn -> { scrollOffset += (itemH + 2); buildWidgets(); })
            .dimensions(panelX + panelW - 14, panelY + panelH - 18, 12, 14)
            .build());
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // Dim background
        ctx.fill(0, 0, this.width, this.height, 0x88000000);

        // Panel background
        ctx.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xDD101010);
        // Panel border
        ctx.drawBorder(panelX, panelY, panelW, panelH, 0xFF444444);

        // Title
        ctx.drawCenteredTextWithShadow(textRenderer,
            Text.literal("§8§l⚔ §f§lIRON CLIENT §8§l⚔"),
            panelX + panelW / 2, panelY + 6, 0xFFFFFFFF);

        // Selected category highlight
        int catBtnW = (panelW - 8) / CATEGORIES.length;
        int hlX = panelX + 4 + selectedCategory * catBtnW;
        ctx.fill(hlX, panelY + 22, hlX + catBtnW - 2, panelY + 38, 0x44FFFFFF);

        // Feature descriptions on hover
        String cat = CATEGORIES[selectedCategory].toLowerCase();
        int y = panelY + 42 - scrollOffset;
        for (FeatureEntry f : features) {
            if (!f.category.equals(cat)) continue;
            if (mouseX >= panelX + 4 && mouseX <= panelX + panelW - 90
                && mouseY >= y && mouseY <= y + itemH) {
                ctx.drawTooltip(textRenderer,
                    Text.literal("§7" + f.description), mouseX, mouseY);
            }
            y += itemH + 2;
        }

        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollOffset = Math.max(0, scrollOffset - (int)(verticalAmount * (itemH + 2)));
        buildWidgets();
        return true;
    }

    @Override
    public boolean shouldPause() { return false; }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private void setTime(int time) {
        IronConfig.timeOfDay = time;
        if (client != null && client.player != null) {
            client.player.networkHandler.sendCommand("time set " + time);
        }
    }

    private void openTexturePacks() {
        if (client != null) {
            client.setScreen(new net.minecraft.client.gui.screen.pack.PackScreen(
                client.getResourcePackManager(),
                manager -> {
                    client.options.refreshResourcePacks(manager);
                    client.setScreen(this);
                },
                client.getResourcePackDir(),
                Text.literal("Texture Packs")
            ));
        }
    }

    private void openAccountSwitcher() {
        if (client != null) {
            client.setScreen(new AccountSwitcherScreen(this));
        }
    }

    private void openSkinChanger() {
        if (client != null) {
            client.setScreen(new SkinCapeScreen(this));
        }
    }

    // ── Inner types ──────────────────────────────────────────────────────────

    @FunctionalInterface interface Getter { boolean get(); }
    @FunctionalInterface interface Setter { void accept(boolean v); }

    private static class FeatureEntry {
        final String category, name, description;
        final Getter getter;
        final Setter setter;
        FeatureEntry(String category, String name, String description,
                     Getter getter, Setter setter) {
            this.category    = category;
            this.name        = name;
            this.description = description;
            this.getter      = getter;
            this.setter      = setter;
        }
    }
}
