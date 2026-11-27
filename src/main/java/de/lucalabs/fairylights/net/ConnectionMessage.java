package de.lucalabs.fairylights.net;

import de.lucalabs.fairylights.connection.Connection;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.fastener.FastenerType;
import de.lucalabs.fairylights.fastener.accessor.FastenerAccessor;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public final class ConnectionMessage {

    private ConnectionMessage() {
    }

    @SuppressWarnings("unchecked")
    public static <C extends Connection> Optional<C> getConnection(
            final FastenerAccessor accessor,
            final UUID id,
            final Predicate<? super Connection> typePredicate,
            final Level level) {
        return accessor.get(level, false).flatMap(fastener -> (Optional<C>) fastener.get(id).filter(typePredicate));
    }

    public static FastenerAccessor deserializeAccessor(final CompoundTag tag) {
        return FastenerType.deserialize(tag);
    }

    public static CompoundTag serializeAccessor(final FastenerAccessor accessor) {
        return FastenerType.serialize(accessor);
    }
}
