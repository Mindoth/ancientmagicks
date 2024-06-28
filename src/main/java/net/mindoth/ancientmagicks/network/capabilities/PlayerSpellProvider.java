package net.mindoth.ancientmagicks.network.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSpellProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerSpell> PLAYER_SPELL = CapabilityManager.get(new CapabilityToken<PlayerSpell>() {});

    private PlayerSpell spell = null;
    private final LazyOptional<PlayerSpell> optional = LazyOptional.of(this::createPlayerSpell);

    private PlayerSpell createPlayerSpell() {
        if ( this.spell == null ) this.spell = new PlayerSpell();
        return this.spell;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if ( cap == PLAYER_SPELL ) return optional.cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createPlayerSpell().saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        createPlayerSpell().loadNBTData(tag);
    }
}
