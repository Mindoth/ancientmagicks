package net.mindoth.ancientmagicks.client.gui.inventory;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.castingitem.WandType;
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

public class WandManager extends SavedData {
    private static final String NAME = AncientMagicks.MOD_ID + "_wand_data";

    private static final HashMap<UUID, WandData> data = new HashMap<>();

    public static final WandManager blankClient = new WandManager();

    public HashMap<UUID, WandData> getMap() { return data; }

    public static WandManager get() {
        if ( Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER )
            return ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(WandManager::load, WandManager::new, NAME);
        else return blankClient;
    }

    public Optional<WandData> getWand(UUID uuid) {
        if ( data.containsKey(uuid) ) {
            return Optional.of(data.get(uuid));
        }
        return Optional.empty();
    }

    public WandData getOrCreateWand(UUID uuid, WandType tier) {
        return data.computeIfAbsent(uuid, id -> {
            setDirty();
            return new WandData(id, tier);
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

    public static WandManager load(CompoundTag nbt) {
        if ( nbt.contains("Wands") ) {
            ListTag list = nbt.getList("Wands", Tag.TAG_COMPOUND);
            list.forEach((wandNBT) -> WandData.fromNBT((CompoundTag)wandNBT).ifPresent((wand) -> data.put(wand.getUuid(), wand)));
        }
        return new WandManager();
    }

    @Override
    @Nonnull
    public CompoundTag save(CompoundTag compound) {
        ListTag wands = new ListTag();
        data.forEach(((uuid, WandData) -> wands.add(WandData.toNBT())));
        compound.put("Wands", wands);
        return compound;
    }
}
