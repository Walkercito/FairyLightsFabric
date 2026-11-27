package de.lucalabs.fairylights.components;

import de.lucalabs.fairylights.fastener.Fastener;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;

public final class FastenerComponent extends GenericComponent<Fastener<?>> implements AutoSyncedComponent {

    @Override
    public void readFromNbt(CompoundTag nbtCompound) {
        if (delegate != null) {
            delegate.readFromNbt(nbtCompound);
        }
    }

    @Override
    public void writeToNbt(CompoundTag nbtCompound) {
        if (delegate != null) {
            delegate.writeToNbt(nbtCompound);
        }
    }

    public FastenerComponent setFastener(Fastener<?> fastener) {
        return (FastenerComponent) super.set(fastener);
    }
}
