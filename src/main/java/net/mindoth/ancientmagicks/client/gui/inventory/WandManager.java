package net.mindoth.ancientmagicks.client.gui.inventory;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.weapon.WandType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class WandManager extends WorldSavedData {
    private static final String NAME = AncientMagicks.MOD_ID + "_wand_data";

    private static final HashMap<UUID, WandData> data = new HashMap<>();

    public static final WandManager blankClient = new WandManager();

    public WandManager() {
        super(NAME);
    }

    public HashMap<UUID, WandData> getMap() { return data; }

    public static WandManager get() {
        if ( Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER )
            return ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getDataStorage().computeIfAbsent(WandManager::new, NAME);
        else return blankClient;
    }

    public Optional<WandData> getWand(UUID uuid) {
        if ( data.containsKey(uuid) ) return Optional.of(data.get(uuid));
        return Optional.empty();
    }

    public WandData getOrCreateWand(UUID uuid, WandType tier) {
        return data.computeIfAbsent(uuid, id -> {
            setDirty();
            return new WandData(id, tier);
        });
    }

    public void removeWand(UUID uuid) {
        getWand(uuid).ifPresent(wand -> {
            wand.getOptional().invalidate();
            data.remove(uuid);
            setDirty();
        });
    }

    public LazyOptional<IItemHandler> getCapability(UUID uuid) {
        if ( data.containsKey(uuid) ) return data.get(uuid).getOptional();
        return LazyOptional.empty();
    }

    public LazyOptional<IItemHandler> getCapability(ItemStack stack) {
        if ( stack.getOrCreateTag().contains("UUID") ) {
            UUID uuid = stack.getTag().getUUID("UUID");
            if ( data.containsKey(uuid) ) return data.get(uuid).getOptional();
        }
        return LazyOptional.empty();
    }

    @Override
    public void load(CompoundNBT nbt) {
        if ( nbt.contains("Wands") ) {
            ListNBT list = nbt.getList("Wands", Constants.NBT.TAG_COMPOUND);
            list.forEach((wandNBT) -> WandData.fromNBT((CompoundNBT) wandNBT).ifPresent((wand) -> data.put(wand.getUuid(), wand)));
        }
    }

    @Override
    @Nonnull
    public CompoundNBT save(CompoundNBT compound) {
        ListNBT wands = new ListNBT();
        data.forEach(((uuid, wandData) -> wands.add(wandData.toNBT())));
        compound.put("Wands", wands);
        return compound;
    }
}
