package net.mindoth.ancientmagicks.client.gui.inventory;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.castingitem.AMBagType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class AMBagManager extends SavedData {
    private static final String NAME = AncientMagicks.MOD_ID + "_ambag_data";

    private static final HashMap<UUID, AMBagData> data = new HashMap<>();

    public static final AMBagManager blankClient = new AMBagManager();

    public HashMap<UUID, AMBagData> getMap() { return data; }

    public static AMBagManager get() {
        if ( Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER )
            return ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(AMBagManager::load, AMBagManager::new, NAME);
        else return blankClient;
    }

    public Optional<AMBagData> getAMBag(UUID uuid) {
        if ( data.containsKey(uuid) ) {
            return Optional.of(data.get(uuid));
        }
        return Optional.empty();
    }

    public AMBagData getOrCreateAMBag(UUID uuid, AMBagType tier) {
        return data.computeIfAbsent(uuid, id -> {
            setDirty();
            return new AMBagData(id, tier);
        });
    }

    public LazyOptional<IItemHandler> getCapability(ItemStack stack) {
        if ( stack.getOrCreateTag().contains("UUID") ) {
            UUID uuid = stack.getTag().getUUID("UUID");
            if ( data.containsKey(uuid) ) {
                return data.get(uuid).getOptional();
            }
        }
        return LazyOptional.empty();
    }

    public static AMBagManager load(CompoundTag nbt) {
        if ( nbt.contains("AMBags") ) {
            ListTag list = nbt.getList("AMBags", Tag.TAG_COMPOUND);
            list.forEach((ambagNBT) -> AMBagData.fromNBT((CompoundTag)ambagNBT).ifPresent((ambag) -> data.put(ambag.getUuid(), ambag)));
        }
        return new AMBagManager();
    }

    @Override
    @Nonnull
    public CompoundTag save(CompoundTag compound) {
        ListTag ambags = new ListTag();
        data.forEach(((uuid, AMBagData) -> ambags.add(AMBagData.toNBT())));
        compound.put("AMBags", ambags);
        return compound;
    }
}
