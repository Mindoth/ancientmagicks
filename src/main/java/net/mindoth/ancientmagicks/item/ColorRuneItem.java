package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;

public class ColorRuneItem extends SpellModifierItem {

    private final String color;
    public String getColor() {
        return this.color;
    }

    public ColorRuneItem(Properties pProperties, int manaCost, int cooldown, String color) {
        super(pProperties, manaCost, cooldown);
        this.color = color;
    }
}
