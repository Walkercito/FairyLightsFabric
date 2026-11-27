package de.lucalabs.fairylights.items.crafting;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class LazyRecipeSerializer {

    private LazyRecipeSerializer() {
    }

    public static <T extends Recipe<?>> RecipeSerializer<T> create(final Supplier<T> factory) {
        final MapCodec<T> codec = new MapCodec<>() {
            @Override
            public <R> Stream<R> keys(final DynamicOps<R> ops) {
                return Stream.empty();
            }

            @Override
            public <R> DataResult<T> decode(final DynamicOps<R> ops, final MapLike<R> input) {
                try {
                    return DataResult.success(factory.get());
                } catch (final RuntimeException e) {
                    return DataResult.error(e::getMessage);
                }
            }

            @Override
            public <R> RecordBuilder<R> encode(final T input, final DynamicOps<R> ops, final RecordBuilder<R> prefix) {
                return prefix;
            }
        };
        final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec = new StreamCodec<>() {
            @Override
            public T decode(final RegistryFriendlyByteBuf buf) {
                return factory.get();
            }

            @Override
            public void encode(final RegistryFriendlyByteBuf buf, final T value) {
            }
        };
        return new RecipeSerializer<>(codec, streamCodec);
    }
}
