package de.lucalabs.fairylights.net;

import de.lucalabs.fairylights.connection.Connection;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.fastener.FastenerType;
import de.lucalabs.fairylights.fastener.accessor.FastenerAccessor;
import io.netty.buffer.Unpooled;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public abstract class ConnectionMessage extends FriendlyByteBuf {

    public ConnectionMessage(final Connection connection) {
        super(Unpooled.buffer());
        final Fastener<?> fastener = connection.getFastener();
        writeBlockPos(fastener.getPos());
        writeNbt(FastenerType.serialize(fastener.createAccessor()));
        writeUUID(connection.getUUID());
    }

    @SuppressWarnings("unchecked")
    public static <C extends Connection> Optional<C> getConnection(
            final FastenerAccessor accessor,
            final UUID id,
            final Predicate<? super Connection> typePredicate,
            final Level world) {
        return accessor.get(world, false).flatMap(f -> (Optional<C>) f.get(id).filter(typePredicate));
    }

    protected static ParsedData parse(FriendlyByteBuf buf) {
        final BlockPos pos = buf.readBlockPos();
        final FastenerAccessor fastenerAcc = FastenerType.deserialize(Objects.requireNonNull(buf.readNbt()));
        final UUID id = buf.readUUID();

        return new ParsedData(pos, fastenerAcc, id);
    }

    protected record ParsedData(BlockPos pos, FastenerAccessor accessor, UUID id) {
    }
}
