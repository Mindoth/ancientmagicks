package net.mindoth.ancientmagicks.client.gui.inventory;

import net.mindoth.ancientmagicks.item.castingitem.WandType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.util.Optional;
import java.util.UUID;

public class WandData {
    private final UUID uuid;
    private WandType tier;
    private final WandItemHandler inventory;
    private final LazyOptional<IItemHandler> optional;
    public final Metadata meta = new Metadata();

    public LazyOptional<IItemHandler> getOptional() {
        return this.optional;
    }

    public IItemHandler getHandler() {
        return this.inventory;
    }

    public WandType getTier() {
        return this.tier;
    }

    public void updateAccessRecords(String player, long time) {
        if ( this.meta.firstAccessedTime == 0 ) {

            //New wand, set creation data
            this.meta.firstAccessedTime = time;
            this.meta.firstAccessedPlayer = player;
        }

        this.meta.setLastAccessedTime(time);
        this.meta.setLastAccessedPlayer(player);
    }

    public WandData(UUID uuid, WandType tier) {
        this.uuid = uuid;
        this.tier = tier;

        this.inventory = new WandItemHandler(tier.slots);
        this.optional = LazyOptional.of(() -> this.inventory);
    }

    public WandData(UUID uuid, CompoundNBT incomingNBT) {
        this.uuid = uuid;
        this.tier = WandType.values()[Math.min(incomingNBT.getInt("Tier"), WandType.BEGINNER_WAND.ordinal())];

        this.inventory = new WandItemHandler(this.tier.slots);

        if ( incomingNBT.getCompound("Inventory").contains("Size") ) {
            if (incomingNBT.getCompound("Inventory").getInt("Size") != tier.slots)
                incomingNBT.getCompound("Inventory").putInt("Size", tier.slots);
        }
        this.inventory.deserializeNBT(incomingNBT.getCompound("Inventory"));
        this.optional = LazyOptional.of(() -> this.inventory);

        if ( incomingNBT.contains("Metadata") )
            this.meta.deserializeNBT(incomingNBT.getCompound("Metadata"));
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public static Optional<WandData> fromNBT(CompoundNBT nbt) {
        if ( nbt.contains("UUID") ) {
            UUID uuid = nbt.getUUID("UUID");
            return Optional.of(new WandData(uuid, nbt));
        }
        return Optional.empty();
    }

    public INBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putUUID("UUID", this.uuid);
        nbt.putInt("Tier", this.tier.ordinal());

        nbt.put("Inventory", this.inventory.serializeNBT());

        nbt.put("Metadata", this.meta.serializeNBT());

        return nbt;
    }



    public static class Metadata implements INBTSerializable<CompoundNBT> {
        private String firstAccessedPlayer = "";

        private long firstAccessedTime = 0;
        private String lastAccessedPlayer = "";
        private long lastAccessedTime = 0;

        public void setLastAccessedTime(long lastAccessedTime) {
            this.lastAccessedTime = lastAccessedTime;
        }

        public void setLastAccessedPlayer(String lastAccessedPlayer) {
            this.lastAccessedPlayer = lastAccessedPlayer;
        }

        public String getFirstAccessedPlayer() {
            return this.firstAccessedPlayer;
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();

            nbt.putString("firstPlayer", this.firstAccessedPlayer);
            nbt.putLong("firstTime", this.firstAccessedTime);
            nbt.putString("lastPlayer", this.lastAccessedPlayer);
            nbt.putLong("lastTime", this.lastAccessedTime);

            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            this.firstAccessedPlayer = nbt.getString("firstPlayer");
            this.firstAccessedTime = nbt.getLong("firstTime");
            this.lastAccessedPlayer = nbt.getString("lastPlayer");
            this.lastAccessedTime = nbt.getLong("lastTime");
        }
    }
}
