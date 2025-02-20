package net.mindoth.ancientmagicks.item;

import net.minecraft.world.item.Item;

public class ComponentItem extends Item {

    private final int manaCost;
    public int getManaCost() {
        return this.manaCost;
    }

    private final int cooldown;
    public int getCooldown() {
        return this.cooldown;
    }

    public ComponentItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties);
        this.manaCost = manaCost;
        this.cooldown = cooldown;
    }
}
