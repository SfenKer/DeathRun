package pl.mrstudios.deathrun.config.serializer;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.serializer.SerializerException;
import pl.mrstudios.deathrun.api.arena.trap.ITrap;
import pl.mrstudios.deathrun.api.arena.trap.annotations.Serializable;

import java.lang.reflect.Field;
import java.util.Collection;

import static java.lang.Class.forName;
import static java.util.Arrays.asList;
import static pl.mrstudios.deathrun.util.ReflectionUtil.readField;
import static pl.mrstudios.deathrun.util.ReflectionUtil.writeField;

public class TrapSerializer implements ObjectSerializer<ITrap> { // TODO: Complete refactor of this class.

    @Override
    public void serialize(
            @NotNull ITrap object,
            @NotNull SerializationData data,
            @NotNull GenericsDeclaration generics
    ) {

        Collection<Field> fields = asList(object.getClass().getDeclaredFields());

        data.add("button", object.getButton());
        data.addCollection("locations", object.getLocations(), Location.class);
        fields.stream()
                .filter((field) -> field.isAnnotationPresent(Serializable.class))
                .peek((field) -> field.setAccessible(true))
                .forEach((field) -> data.add(field.getName(), readField(field, object)));

        data.add("class", object.getClass().getName());

    }

    @Override
    public @NotNull ITrap deserialize(
            @NotNull DeserializationData data,
            @NotNull GenericsDeclaration generics
    ) {

        try {

            ITrap object = (ITrap) forName(data.get("class", String.class)).getDeclaredConstructor().newInstance();
            Collection<Field> fields = asList(object.getClass().getDeclaredFields());

            object.setButton(data.get("button", Location.class));
            object.setLocations(data.getAsList("locations", Location.class));
            fields.stream()
                    .filter((field) -> field.isAnnotationPresent(Serializable.class))
                    .peek((field) -> field.setAccessible(true))
                    .forEach((field) -> writeField(field, object, data.get(field.getName(), field.getType())));

            return object;

        } catch (@NotNull Exception ignored) {}

        throw new SerializerException("Unable to deserialize '" + data.get("class", String.class) + "', if this is bug please contact with support.");

    }

    @Override
    public boolean supports(
            @NotNull Class<? super ITrap> type
    ) {
        return ITrap.class.isAssignableFrom(type);
    }

}
