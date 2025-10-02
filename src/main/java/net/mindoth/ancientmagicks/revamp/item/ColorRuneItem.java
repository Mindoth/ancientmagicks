package net.mindoth.ancientmagicks.revamp.item;

import net.minecraft.world.item.Item;

public class ColorRuneItem extends Item {

    private final String color;
    public String getColor() {
        return this.color;
    }

    public ColorRuneItem(Properties pProperties, String color) {
        super(pProperties);
        this.color = color;
    }
}
