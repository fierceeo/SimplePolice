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
    public static boolean allowArrestsWithoutContraband;


    public static void initialize(Config config) {
        maxPoliceTpDistance = Integer.parseInt(config.getProperty("MaxPoliceTpDistance"));
        payPoliceOnArrest = Boolean.parseBoolean(config.getProperty("PayPoliceOnArrest"));
        payPoliceOnArrestMode = PayPoliceOnArrestMode.valueOf(config.getProperty("PayPoliceOnArrestMode").toLowerCase());
        payPoliceOnArrestServer = Integer.parseInt(config.getProperty("PayPoliceOnArrestServer"));
        payPoliceOnArrestPlayerPercentage = Integer.parseInt(config.getProperty("PayPoliceOnArrestPlayerPercentage"));
        payPoliceOnArrestPlayerFixed = Integer.parseInt(config.getProperty("PayPoliceOnArrestPlayerFixed"));
        takeMoneyOnArrest = Boolean.parseBoolean(config.getProperty("TakeMoneyOnArrest"));
        takeMoneyOnArrestMode = TakeMoneyOnArrestMode.valueOf(config.getProperty("TakeMoneyOnArrestMode").toLowerCase());
        takeMoneyOnArrestPercentage = Integer.parseInt(config.getProperty("TakeMoneyOnArrestPercentage"));
        takeMoneyOnArrestFixed = Integer.parseInt(config.getProperty("TakeMoneyOnArrestFixed"));
        safeAreaEnabled = Boolean.parseBoolean(config.getProperty("SafeAreaEnabled"));
        safeAreas = config.getProperty("SafeAreas").toLowerCase().split(",");
        batonMaterialType = Material.getMaterial(config.getProperty("BatonMaterialType").toUpperCase().replace(' ', '_'));
        friskingEnabled = Boolean.parseBoolean(config.getProperty("FriskingEnabled"));
        percentOfFindingContraband = Integer.parseInt(config.getProperty("PercentOfFindingContraband"));
        friskStickMaterialType = Material.getMaterial(config.getProperty("FriskStickMaterialType").toUpperCase().replace(' ', '_'));
        markAllGunsAsContraband = Boolean.parseBoolean(config.getProperty("MarkAllGunsAsContraband"));
        friskCooldown = Integer.parseInt(config.getProperty("FriskCooldown"));
        showCords911 = Boolean.parseBoolean(config.getProperty("ShowCords911"));
        usePlayerDisplayNameInPoliceChat = Boolean.parseBoolean(config.getProperty("UsePlayerDisplayNameInPoliceChat"));
        jailGUITimes = Arrays.stream(config.getProperty("JailGUITimes").split(",")).mapToDouble(Double::parseDouble).toArray();
        allowArrestsWithoutContraband = Boolean.parseBoolean(config.getProperty("AllowArrestsWithoutContraband"));
    }

}
