package net.mindoth.ancientmagicks.network.capabilities.numbnessdamage;

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

public class NumbnessDamageProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<NumbnessDamage> NUMBNESS_DAMAGE = CapabilityManager.get(new CapabilityToken<NumbnessDamage>() {});

    private NumbnessDamage damage = null;
    private final LazyOptional<NumbnessDamage> optional = LazyOptional.of(this::createNumbnessDamage);

    private NumbnessDamage createNumbnessDamage() {
        if ( this.damage == null ) this.damage = new NumbnessDamage();
        return this.damage;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if ( cap == NUMBNESS_DAMAGE ) return optional.cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createNumbnessDamage().saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        createNumbnessDamage().loadNBTData(tag);
    }
}
