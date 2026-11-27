package de.lucalabs.fairylights.components;

import de.lucalabs.fairylights.fastener.Fastener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public final class FastenerComponent extends GenericComponent<Fastener<?>> implements AutoSyncedComponent {

    @Override
    public void readData(final ValueInput input) {
        input.read("data", CompoundTag.CODEC).ifPresent(tag -> {
            if (this.delegate != null) {
                this.delegate.readFromNbt(tag);
            }
        });
    }

    @Override
    public void writeData(final ValueOutput output) {
        if (this.delegate != null) {
            final CompoundTag tag = new CompoundTag();
            this.delegate.writeToNbt(tag);
            output.store("data", CompoundTag.CODEC, tag);
        }
    }

    public FastenerComponent setFastener(Fastener<?> fastener) {
        return (FastenerComponent) super.set(fastener);
    }
}
