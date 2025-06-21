package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;

public class ColorRuneItem extends SpellModifierItem {

    private final String color;
    public String getColor() {
        return this.color;
    }

    public ColorRuneItem(Properties pProperties, int cost, String color) {
        super(pProperties, cost);
        this.color = color;
    }
}
