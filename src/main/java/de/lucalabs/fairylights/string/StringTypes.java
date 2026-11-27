package de.lucalabs.fairylights.string;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public class StringTypes {

    public static final StringType BLACK_STRING = register("black_string", () -> new StringType(0x323232));
    public static final StringType WHITE_STRING = register("white_string", () -> new StringType(0xF0F0F0));

    private static StringType register(final String name, Supplier<? extends StringType> supplier) {
        Identifier identifier = Identifier.tryBuild(FairyLights.ID, name);
        return Registry.register(FairyLightRegistries.STRING_TYPES, identifier, supplier.get());
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Initializing string types");
    }
}
