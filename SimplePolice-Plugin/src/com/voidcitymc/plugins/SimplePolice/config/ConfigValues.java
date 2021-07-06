package com.voidcitymc.plugins.SimplePolice.config;

import com.voidcitymc.plugins.SimplePolice.config.configvalues.PayPoliceOnArrestMode;
import com.voidcitymc.plugins.SimplePolice.config.configvalues.TakeMoneyOnArrestMode;
import org.bukkit.Material;

import java.util.Arrays;

public class ConfigValues {

    public static int maxPoliceTpDistance;
    public static boolean payPoliceOnArrest;
    public static PayPoliceOnArrestMode payPoliceOnArrestMode;
    public static int payPoliceOnArrestServer;
    public static int payPoliceOnArrestPlayerPercentage;
    public static int payPoliceOnArrestPlayerFixed;
    public static boolean takeMoneyOnArrest;
    public static TakeMoneyOnArrestMode takeMoneyOnArrestMode;
    public static int takeMoneyOnArrestPercentage;
    public static int takeMoneyOnArrestFixed;
    public static boolean safeAreaEnabled;
    public static String[] safeAreas;
    public static Material batonMaterialType;
    public static boolean friskingEnabled;
    public static int percentOfFindingContraband;
    public static Material friskStickMaterialType;
    public static boolean markAllGunsAsContraband;
    public static int friskCooldown;
    public static boolean showCords911;
    public static boolean usePlayerDisplayNameInPoliceChat;
    public static double[] jailGUITimes;


    public static void initialize(Config config) {
        maxPoliceTpDistance = Integer.parseInt(config.getConfigProperty("MaxPoliceTpDistance"));
        payPoliceOnArrest = Boolean.parseBoolean(config.getConfigProperty("PayPoliceOnArrest"));
        payPoliceOnArrestMode = PayPoliceOnArrestMode.valueOf(config.getConfigProperty("PayPoliceOnArrestMode").toLowerCase());
        payPoliceOnArrestServer = Integer.parseInt(config.getConfigProperty("PayPoliceOnArrestServer"));
        payPoliceOnArrestPlayerPercentage = Integer.parseInt(config.getConfigProperty("PayPoliceOnArrestPlayerPercentage"));
        payPoliceOnArrestPlayerFixed = Integer.parseInt(config.getConfigProperty("PayPoliceOnArrestPlayerFixed"));
        takeMoneyOnArrest = Boolean.parseBoolean(config.getConfigProperty("TakeMoneyOnArrest"));
        takeMoneyOnArrestMode = TakeMoneyOnArrestMode.valueOf(config.getConfigProperty("TakeMoneyOnArrestMode").toLowerCase());
        takeMoneyOnArrestPercentage = Integer.parseInt(config.getConfigProperty("TakeMoneyOnArrestPercentage"));
        takeMoneyOnArrestFixed = Integer.parseInt(config.getConfigProperty("TakeMoneyOnArrestFixed"));
        safeAreaEnabled = Boolean.parseBoolean(config.getConfigProperty("SafeAreaEnabled"));
        safeAreas = config.getConfigProperty("SafeAreas").toLowerCase().split(",");
        batonMaterialType = Material.getMaterial(config.getConfigProperty("BatonMaterialType").toUpperCase().replace(' ', '_'));
        friskingEnabled = Boolean.parseBoolean(config.getConfigProperty("FriskingEnabled"));
        percentOfFindingContraband = Integer.parseInt(config.getConfigProperty("PercentOfFindingContraband"));
        friskStickMaterialType = Material.getMaterial(config.getConfigProperty("FriskStickMaterialType").toUpperCase().replace(' ', '_'));
        markAllGunsAsContraband = Boolean.parseBoolean(config.getConfigProperty("MarkAllGunsAsContraband"));
        friskCooldown = Integer.parseInt(config.getConfigProperty("FriskCooldown"));
        showCords911 = Boolean.parseBoolean(config.getConfigProperty("ShowCords911"));
        usePlayerDisplayNameInPoliceChat = Boolean.parseBoolean(config.getConfigProperty("UsePlayerDisplayNameInPoliceChat"));
        jailGUITimes = Arrays.stream(config.getConfigProperty("JailGUITimes").split(",")).mapToDouble(Double::parseDouble).toArray();
    }

}
