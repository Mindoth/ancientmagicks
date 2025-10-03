package net.mindoth.ancientmagicks.capability.playermagic;

public class ClientMagicData {

    private static double currentMana;
    public static double getCurrentMana() {
        return currentMana;
    }
    public static void setCurrentMana(double currentMana) {
        ClientMagicData.currentMana = currentMana;
    }
}
