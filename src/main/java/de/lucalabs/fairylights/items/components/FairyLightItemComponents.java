package de.lucalabs.fairylights.items.components;

import com.mojang.serialization.Codec;
import de.lucalabs.fairylights.FairyLights;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public final class FairyLightItemComponents {

    public static final DataComponentType<List<Integer>> COLORS = register(
            "colors",
            DataComponentType.<List<Integer>>builder()
                    .persistent(Codec.INT.listOf())
                    .networkSynchronized(ByteBufCodecs.<io.netty.buffer.ByteBuf, Integer>list().apply(ByteBufCodecs.VAR_INT))
                    .build()
    );

    public static final DataComponentType<Boolean> TWINKLE = register(
            "twinkle",
            DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build()
    );

    public static final DataComponentType<Identifier> STRING = register(
            "string",
            DataComponentType.<Identifier>builder()
                    .persistent(Identifier.CODEC)
                    .networkSynchronized(Identifier.STREAM_CODEC)
                    .build()
    );

    public static final DataComponentType<List<ItemStack>> PATTERN = register(
            "pattern",
            DataComponentType.<List<ItemStack>>builder()
                    .persistent(ItemStack.CODEC.listOf())
                    .networkSynchronized(ByteBufCodecs.<RegistryFriendlyByteBuf, ItemStack>list().apply(ItemStack.STREAM_CODEC))
                    .build()
    );

    public static final DataComponentType<CompoundTag> TEXT = register(
            "text",
            DataComponentType.<CompoundTag>builder()
                    .persistent(CompoundTag.CODEC)
                    .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                    .build()
    );

    private FairyLightItemComponents() {
    }

    private static <T> DataComponentType<T> register(final String name, final DataComponentType<T> component) {
        return Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                Identifier.fromNamespaceAndPath(FairyLights.ID, name),
                component
        );
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Registering item components");
    }

    public static CompoundTag saveStack(final ItemStack stack) {
        final Tag tag = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack).result().orElseGet(CompoundTag::new);
        return tag instanceof CompoundTag compound ? compound : new CompoundTag();
    }

    public static ItemStack loadStack(final CompoundTag tag) {
        return ItemStack.CODEC.parse(NbtOps.INSTANCE, tag).result().orElse(ItemStack.EMPTY);
    }
}
