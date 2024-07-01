package net.mindoth.ancientmagicks.network.capabilities.playerspell;

public class ClientSpellData {
    private static String PLAYER_SPELL;

    public static void set(String spell) {
        ClientSpellData.PLAYER_SPELL = spell;
    }

    public static String getPlayerSpell() {
        return PLAYER_SPELL;
    }
}
