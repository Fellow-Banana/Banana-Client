package com.banana;

import com.banana.config.BananaClientConfig;
import com.banana.features.nightvision.NightVision;
import com.banana.features.nohunger.NoHunger;
import com.banana.features.togglesprint.ToggleSprint;
import com.banana.features.autoclicker.AutoClicker;
import com.banana.features.nofall.NoFall;
import com.banana.features.fly.Fly;
import com.banana.features.autofishing.AutoFishingMod;
import com.banana.features.tp.TP;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

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
        new NoHunger().onInitializeClient();
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
        // Changed '_' to 'registryAccess' to resolve the unnamed variables preview feature error.
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("banana")
                // Autoclicker Command
                .then(literal("autoclicker")
                        .executes(context -> {
                            boolean newState = !BananaClientConfig.getInstance().isAutoclickerEnabled();
                            BananaClientConfig.getInstance().setAutoclickerEnabled(newState);
                            AutoClicker.sendToggleMessage(context.getSource().getClient(), newState);
                            return 1;
                        })
                        .then(argument("enabled", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                    BananaClientConfig.getInstance().setAutoclickerEnabled(enabled);
                                    AutoClicker.sendToggleMessage(context.getSource().getClient(), enabled);
                                    return 1;
                                })
                        )
                        .then(argument("delay", IntegerArgumentType.integer(1))
                                .then(argument("button", StringArgumentType.word())
                                        .then(argument("hold", BoolArgumentType.bool())
                                                .executes(context -> {
                                                    int delay = IntegerArgumentType.getInteger(context, "delay");
                                                    String buttonStr = StringArgumentType.getString(context, "button").toLowerCase();
                                                    boolean hold = BoolArgumentType.getBool(context, "hold");

                                                    int button;
                                                    if (buttonStr.equals("left")) {
                                                        button = 0;
                                                    } else if (buttonStr.equals("right")) {
                                                        button = 1;
                                                    } else {
                                                        context.getSource().sendError(Text.literal("Invalid button. Use 'left' or 'right'."));
                                                        return 0;
                                                    }

                                                    BananaClientConfig.getInstance().setAutoclickerDelay(delay);
                                                    BananaClientConfig.getInstance().setAutoclickerMouseButton(button);
                                                    BananaClientConfig.getInstance().setAutoclickerHoldToClick(hold);

                                                    if (context.getSource().getClient().player != null) {
                                                        context.getSource().getClient().player.sendMessage(Text.literal("Autoclicker settings updated: Delay §e" + delay + "§r, Button §b" + buttonStr + "§r, Hold §a" + (hold ? "ON" : "OFF")), false);
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
                // Fly Command
                .then(literal("fly")
                        .executes(context -> {
                            boolean newState = !BananaClientConfig.getInstance().isFlyEnabled();
                            BananaClientConfig.getInstance().setFlyEnabled(newState);
                            Fly.sendToggleMessage(context.getSource().getClient(), newState);
                            return 1;
                        })
                        .then(argument("speed", DoubleArgumentType.doubleArg(0.1, 10.0))
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
                // ToggleSprint Command
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
                // NoFall Command
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
                // NightVision Command
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
                // NoHunger Command
                .then(literal("nohunger")
                        .executes(context -> {
                            boolean newState = !BananaClientConfig.getInstance().isNoHungerEnabled();
                            BananaClientConfig.getInstance().setNoHungerEnabled(newState);
                            NoHunger.sendToggleMessage(context.getSource().getClient(), newState);
                            return 1;
                        })
                        .then(argument("enabled", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                    BananaClientConfig.getInstance().setNoHungerEnabled(enabled);
                                    NoHunger.sendToggleMessage(context.getSource().getClient(), enabled);
                                    return 1;
                                })
                        )
                )
                // AutoFishing Command
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
                // TP Command
                .then(literal("tp")
                        .executes(context -> {
                            boolean newState = !BananaClientConfig.getInstance().isTpEnabled();
                            BananaClientConfig.getInstance().setTpEnabled(newState);
                            TP.sendToggleMessage(context.getSource().getClient(), newState);
                            return 1;
                        })
                        .then(argument("enabled", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                    BananaClientConfig.getInstance().setTpEnabled(enabled);
                                    TP.sendToggleMessage(context.getSource().getClient(), enabled);
                                    return 1;
                                })
                        )
                )
        ));
    }
}
