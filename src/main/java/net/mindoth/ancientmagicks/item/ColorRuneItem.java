package net.mindoth.ancientmagicks.item;

import net.minecraft.world.item.Item;

public class ColorRuneItem extends Item {

    public String color;

    public ColorRuneItem(Properties pProperties, String color) {
        super(pProperties);
        this.color = color;
    }
}
