package com.ironclient.client;

import com.ironclient.gui.IronLibraryScreen;
import com.ironclient.hud.ArmorHud;
import com.ironclient.hud.KeystrokesHud;
import com.ironclient.util.IronConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class IronClient implements ClientModInitializer {

    public static final String MOD_ID = "ironclient";
    public static final String MOD_NAME = "Iron Client";

    // Keybind: Right Shift opens the library GUI
    private static KeyBinding openGuiKey;
    // Keybind: Toggle FOV effect + No crystal particles + Damage tilt
    private static KeyBinding combatToggleKey;
    // Keybind: Toggle scoreboard
    private static KeyBinding scoreboardKey;

    @Override
    public void onInitializeClient() {
        IronConfig.load();

        // Register keybindings
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.ironclient.open_gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.ironclient"
        ));

        combatToggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.ironclient.combat_toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "category.ironclient"
        ));

        scoreboardKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.ironclient.scoreboard",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "category.ironclient"
        ));

        // Tick events for keybind handling
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openGuiKey.wasPressed()) {
                client.setScreen(new IronLibraryScreen());
            }

            if (combatToggleKey.wasPressed()) {
                IronConfig.combatToggle       = !IronConfig.combatToggle;
                IronConfig.fovEffectDisabled  = IronConfig.combatToggle;
                IronConfig.noCrystalParticles = IronConfig.combatToggle;
                IronConfig.damageTiltDisabled = IronConfig.combatToggle;
                IronConfig.save();
            }

            if (scoreboardKey.wasPressed()) {
                IronConfig.scoreboardVisible = !IronConfig.scoreboardVisible;
                IronConfig.save();
            }

            // Apply fullbright + night vision every tick
            if (client.player != null) {
                if (IronConfig.fullbrightEnabled) {
                    client.options.getGamma().setValue((double) IronConfig.fullbrightLevel * 16.0);
                }
                if (IronConfig.nightVisionEnabled && IronConfig.fullbrightEnabled) {
                    client.player.addStatusEffect(
                        new net.minecraft.entity.effect.StatusEffectInstance(
                            net.minecraft.registry.Registries.STATUS_EFFECT.getEntry(
                                net.minecraft.entity.effect.StatusEffects.NIGHT_VISION),
                            400, 0, false, false, false));
                }
                // Apply time changer
                if (client.world != null && client.player.hasPermissionLevel(2)) {
                    // Time changer is done via command, handled in GUI
                }
            }
        });

        // HUD rendering
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.world == null) return;

            // Armor HUD
            if (IronConfig.armorHudEnabled) {
                ArmorHud.render(drawContext, client);
            }

            // Keystrokes
            if (IronConfig.keystrokesEnabled) {
                KeystrokesHud.render(drawContext, client);
            }
        });
    }
}
