package com.ironclient.util;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.*;

public class IronConfig {

    // ── Combat toggles ──────────────────────────────────────────────────────
    public static boolean fovEffectDisabled = false;
    public static boolean noCrystalParticles = false;
    public static boolean damageTiltDisabled = false;
    // All three above share one keybind (Right Shift opens GUI, these toggled together)
    public static boolean combatToggle = false; // master for fov+crystal+tilt

    public static boolean scoreboardVisible = true;

    // ── HUD ─────────────────────────────────────────────────────────────────
    public static boolean armorHudEnabled = true;
    public static boolean keystrokesEnabled = true;

    // ── Visual ──────────────────────────────────────────────────────────────
    public static boolean motionBlurEnabled = false;
    public static float  motionBlurStrength = 0.5f;

    public static boolean fullbrightEnabled = false;
    public static float   fullbrightLevel   = 1.0f; // 0.0 - 1.0

    public static boolean nightVisionEnabled = false; // linked with fullbright

    public static int hitColor = 0xFFFF0000; // red default (ARGB)

    // ── Block highlight ─────────────────────────────────────────────────────
    public static boolean blockHighlightEnabled = true;
    public static int     blockHighlightColor   = 0xFF000000;
    public static float   blockHighlightWidth   = 2.0f;

    // ── Time ────────────────────────────────────────────────────────────────
    public static int timeOfDay = 6000; // noon default

    // ── Spotify ─────────────────────────────────────────────────────────────
    public static boolean spotifyWidgetVisible = false;
    public static String  spotifyToken         = "";

    // ── Discord ─────────────────────────────────────────────────────────────
    public static boolean discordWidgetVisible = false;
    public static String  discordToken         = "";

    // ── Skin/Cape ───────────────────────────────────────────────────────────
    public static String customSkinUrl = "";
    public static String customCapeUrl = "";

    private static final Path CONFIG_FILE =
        FabricLoader.getInstance().getConfigDir().resolve("ironclient.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void load() {
        try {
            if (Files.exists(CONFIG_FILE)) {
                JsonObject obj = JsonParser.parseReader(
                    new FileReader(CONFIG_FILE.toFile())).getAsJsonObject();
                combatToggle         = getBool(obj, "combatToggle",         combatToggle);
                fovEffectDisabled    = getBool(obj, "fovEffectDisabled",    fovEffectDisabled);
                noCrystalParticles   = getBool(obj, "noCrystalParticles",   noCrystalParticles);
                damageTiltDisabled   = getBool(obj, "damageTiltDisabled",   damageTiltDisabled);
                scoreboardVisible    = getBool(obj, "scoreboardVisible",    scoreboardVisible);
                armorHudEnabled      = getBool(obj, "armorHudEnabled",      armorHudEnabled);
                keystrokesEnabled    = getBool(obj, "keystrokesEnabled",    keystrokesEnabled);
                motionBlurEnabled    = getBool(obj, "motionBlurEnabled",    motionBlurEnabled);
                motionBlurStrength   = getFloat(obj,"motionBlurStrength",   motionBlurStrength);
                fullbrightEnabled    = getBool(obj, "fullbrightEnabled",    fullbrightEnabled);
                fullbrightLevel      = getFloat(obj,"fullbrightLevel",      fullbrightLevel);
                nightVisionEnabled   = getBool(obj, "nightVisionEnabled",   nightVisionEnabled);
                hitColor             = getInt(obj,  "hitColor",             hitColor);
                blockHighlightEnabled= getBool(obj, "blockHighlightEnabled",blockHighlightEnabled);
                blockHighlightColor  = getInt(obj,  "blockHighlightColor",  blockHighlightColor);
                blockHighlightWidth  = getFloat(obj,"blockHighlightWidth",  blockHighlightWidth);
                timeOfDay            = getInt(obj,  "timeOfDay",            timeOfDay);
                spotifyToken         = getString(obj,"spotifyToken",        spotifyToken);
                discordToken         = getString(obj,"discordToken",        discordToken);
                customSkinUrl        = getString(obj,"customSkinUrl",       customSkinUrl);
                customCapeUrl        = getString(obj,"customCapeUrl",       customCapeUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("combatToggle",          combatToggle);
            obj.addProperty("fovEffectDisabled",     fovEffectDisabled);
            obj.addProperty("noCrystalParticles",    noCrystalParticles);
            obj.addProperty("damageTiltDisabled",    damageTiltDisabled);
            obj.addProperty("scoreboardVisible",     scoreboardVisible);
            obj.addProperty("armorHudEnabled",       armorHudEnabled);
            obj.addProperty("keystrokesEnabled",     keystrokesEnabled);
            obj.addProperty("motionBlurEnabled",     motionBlurEnabled);
            obj.addProperty("motionBlurStrength",    motionBlurStrength);
            obj.addProperty("fullbrightEnabled",     fullbrightEnabled);
            obj.addProperty("fullbrightLevel",       fullbrightLevel);
            obj.addProperty("nightVisionEnabled",    nightVisionEnabled);
            obj.addProperty("hitColor",              hitColor);
            obj.addProperty("blockHighlightEnabled", blockHighlightEnabled);
            obj.addProperty("blockHighlightColor",   blockHighlightColor);
            obj.addProperty("blockHighlightWidth",   blockHighlightWidth);
            obj.addProperty("timeOfDay",             timeOfDay);
            obj.addProperty("spotifyToken",          spotifyToken);
            obj.addProperty("discordToken",          discordToken);
            obj.addProperty("customSkinUrl",         customSkinUrl);
            obj.addProperty("customCapeUrl",         customCapeUrl);
            Files.writeString(CONFIG_FILE, GSON.toJson(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean getBool(JsonObject o, String k, boolean def) {
        return o.has(k) ? o.get(k).getAsBoolean() : def;
    }
    private static float getFloat(JsonObject o, String k, float def) {
        return o.has(k) ? o.get(k).getAsFloat() : def;
    }
    private static int getInt(JsonObject o, String k, int def) {
        return o.has(k) ? o.get(k).getAsInt() : def;
    }
    private static String getString(JsonObject o, String k, String def) {
        return o.has(k) ? o.get(k).getAsString() : def;
    }
}
