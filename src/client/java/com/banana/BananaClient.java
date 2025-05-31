package com.banana;

import com.banana.config.BananaClientConfig;
import com.banana.features.nightvision.NightVision;
import com.banana.features.togglesprint.ToggleSprint;
import com.banana.features.autoclicker.AutoClicker;
import com.banana.features.nofall.NoFall;
import com.banana.features.fly.Fly;
import com.banana.features.autofishing.AutoFishingMod;
import com.banana.features.tp.TP;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.util.math.Vec3d;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class BananaClient implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("BananaClient");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Banana Client initializing client features...");

        BananaClientConfig.getInstance(); // Load config early

        // Initialize features
        new NightVision().onInitializeClient();
        new ToggleSprint().onInitializeClient();
        new AutoClicker().onInitializeClient();
        new NoFall().onInitializeClient();
        new Fly().onInitializeClient();
        new AutoFishingMod().onInitializeClient();
        new TP().onInitializeClient();

        registerCommands();

        LOGGER.info("Banana Client client features initialized.");
    }

    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(literal("banana")
                    // Autoclicker Command (no change)
                    .then(literal("autoclicker")
                            // No arguments: toggle autoclicker
                            .executes(context -> {
                                boolean newState = !BananaClientConfig.getInstance().isAutoclickerEnabled();
                                BananaClientConfig.getInstance().setAutoclickerEnabled(newState);
                                AutoClicker.sendToggleMessage(context.getSource().getClient(), newState);
                                return 1;
                            })
                            // Specific ON/OFF boolean toggle
                            .then(argument("enabled", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                        BananaClientConfig.getInstance().setAutoclickerEnabled(enabled);
                                        AutoClicker.sendToggleMessage(context.getSource().getClient(), enabled);
                                        return 1;
                                    })
                            )
                            // Arguments: delay, button, hold (e.g., /banana autoclicker 5 left true)
                            .then(argument("delay", IntegerArgumentType.integer(1)) // Delay must be at least 1
                                    .then(argument("button", StringArgumentType.word()) // "left" or "right"
                                            .then(argument("hold", BoolArgumentType.bool())
                                                    .executes(context -> {
                                                        int delay = IntegerArgumentType.getInteger(context, "delay");
                                                        String buttonStr = StringArgumentType.getString(context, "button").toLowerCase();
                                                        boolean hold = BoolArgumentType.getBool(context, "hold");

                                                        int button;
                                                        if (buttonStr.equals("left")) {
                                                            button = 0; // GLFW_MOUSE_BUTTON_LEFT
                                                        } else if (buttonStr.equals("right")) {
                                                            button = 1; // GLFW_MOUSE_BUTTON_RIGHT
                                                        } else {
                                                            context.getSource().sendError(Text.literal("Invalid button. Use 'left' or 'right'."));
                                                            return 0; // Indicate command failed
                                                        }

                                                        BananaClientConfig.getInstance().setAutoclickerDelay(delay);
                                                        BananaClientConfig.getInstance().setAutoclickerMouseButton(button);
                                                        BananaClientConfig.getInstance().setAutoclickerHoldToClick(hold);

                                                        // Send a confirmation message
                                                        if (context.getSource().getClient().player != null) {
                                                            context.getSource().getClient().player.sendMessage(Text.literal("Autoclicker settings updated: Delay §e" + delay + "§r, Button §b" + buttonStr + "§r, Hold §a" + (hold ? "ON" : "OFF")), false);
                                                        }
                                                        return 1;
                                                    })
                                            )
                                    )
                            )
                    )
                    // Fly Command (no change)
                    .then(literal("fly")
                            // No arguments: toggle fly
                            .executes(context -> {
                                boolean newState = !BananaClientConfig.getInstance().isFlyEnabled();
                                BananaClientConfig.getInstance().setFlyEnabled(newState);
                                Fly.sendToggleMessage(context.getSource().getClient(), newState);
                                return 1;
                            })
                            // One argument: set fly speed
                            .then(argument("speed", DoubleArgumentType.doubleArg(0.1, 10.0)) // Min 0.1, Max 10.0
                                    .executes(context -> {
                                        double multiplier = DoubleArgumentType.getDouble(context, "speed");
                                        BananaClientConfig.getInstance().setFlySpeedMultiplier(multiplier);
                                        if (context.getSource().getClient().player != null) {
                                            context.getSource().getClient().player.sendMessage(Text.literal("Fly Speed set to: §b" + String.format("%.1f", multiplier) + "x"), false);
                                        }
                                        return 1;
                                    })
                            )
                    )
                    // ToggleSprint, NoFall, NightVision, AutoFishing (no changes)
                    .then(literal("togglesprint")
                            .executes(context -> {
                                boolean newState = !BananaClientConfig.getInstance().isToggleSprintEnabled();
                                BananaClientConfig.getInstance().setToggleSprintEnabled(newState);
                                ToggleSprint.sendToggleMessage(context.getSource().getClient(), newState);
                                return 1;
                            })
                            .then(argument("enabled", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                        BananaClientConfig.getInstance().setToggleSprintEnabled(enabled);
                                        ToggleSprint.sendToggleMessage(context.getSource().getClient(), enabled);
                                        return 1;
                                    })
                            )
                    )
                    .then(literal("nofall")
                            .executes(context -> {
                                boolean newState = !BananaClientConfig.getInstance().isNoFallEnabled();
                                BananaClientConfig.getInstance().setNoFallEnabled(newState);
                                NoFall.sendToggleMessage(context.getSource().getClient(), newState);
                                return 1;
                            })
                            .then(argument("enabled", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                        BananaClientConfig.getInstance().setNoFallEnabled(enabled);
                                        NoFall.sendToggleMessage(context.getSource().getClient(), enabled);
                                        return 1;
                                    })
                            )
                    )
                    .then(literal("nightvision")
                            .executes(context -> {
                                boolean newState = !BananaClientConfig.getInstance().isNightVisionEnabled();
                                BananaClientConfig.getInstance().setNightVisionEnabled(newState);
                                NightVision.sendToggleMessage(context.getSource().getClient(), newState);
                                return 1;
                            })
                            .then(argument("enabled", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                        BananaClientConfig.getInstance().setNightVisionEnabled(enabled);
                                        NightVision.sendToggleMessage(context.getSource().getClient(), enabled);
                                        return 1;
                                    })
                            )
                    )
                    .then(literal("autofishing")
                            .executes(context -> {
                                boolean newState = !BananaClientConfig.getInstance().isAutoFishEnabled();
                                BananaClientConfig.getInstance().setAutoFishEnabled(newState);
                                AutoFishingMod.sendToggleMessage(context.getSource().getClient(), newState);
                                return 1;
                            })
                            .then(argument("enabled", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                        BananaClientConfig.getInstance().setAutoFishEnabled(enabled);
                                        AutoFishingMod.sendToggleMessage(context.getSource().getClient(), enabled);
                                        return 1;
                                    })
                            )
                    )
                    // TP Command (Modified)
                    .then(literal("tp")
                            // No arguments: toggle TP
                            .executes(context -> {
                                boolean newState = !BananaClientConfig.getInstance().isTpEnabled();
                                BananaClientConfig.getInstance().setTpEnabled(newState);
                                TP.sendToggleMessage(context.getSource().getClient(), newState);
                                return 1;
                            })
                            // With 'enabled' argument (e.g., /banana tp true/false)
                            .then(argument("enabled", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                        BananaClientConfig.getInstance().setTpEnabled(enabled);
                                        TP.sendToggleMessage(context.getSource().getClient(), enabled);
                                        return 1;
                                    })
                            )
                            // NEW: With 1 distance argument (e.g., /banana tp 50)
                            .then(argument("distance", DoubleArgumentType.doubleArg())
                                    .executes(context -> {
                                        double distance = DoubleArgumentType.getDouble(context, "distance");
                                        MinecraftClient client = MinecraftClient.getInstance();

                                        if (client.player == null) {
                                            context.getSource().sendError(Text.literal("Player not found."));
                                            return 0;
                                        }

                                        // Get the player's current position and rotation vector
                                        Vec3d currentPos = client.player.getPos();
                                        // getRotationVec(1.0F) gives a unit vector of the player's look direction
                                        Vec3d lookVec = client.player.getRotationVec(1.0F);

                                        // Calculate new coordinates
                                        double newX = currentPos.x + lookVec.x * distance;
                                        double newY = currentPos.y + lookVec.y * distance;
                                        double newZ = currentPos.z + lookVec.z * distance;

                                        TP.teleportPlayer(client, newX, newY, newZ);
                                        return 1;
                                    })
                            )
                            // MODIFIED: Absolute teleport with "to" literal (e.g., /banana tp to 100 64 200)
                            .then(literal("to")
                                    .then(argument("x", DoubleArgumentType.doubleArg())
                                            .then(argument("y", DoubleArgumentType.doubleArg())
                                                    .then(argument("z", DoubleArgumentType.doubleArg())
                                                            .executes(context -> {
                                                                double x = DoubleArgumentType.getDouble(context, "x");
                                                                double y = DoubleArgumentType.getDouble(context, "y");
                                                                double z = DoubleArgumentType.getDouble(context, "z");
                                                                TP.teleportPlayer(MinecraftClient.getInstance(), x, y, z);
                                                                return 1;
                                                            })
                                                    )
                                            )
                                    )
                            )
                    )
            );
        });
    }
}