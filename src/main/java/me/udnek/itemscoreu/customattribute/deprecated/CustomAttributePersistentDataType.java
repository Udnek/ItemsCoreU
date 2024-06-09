package me.udnek.itemscoreu.customattribute.deprecated;

import me.udnek.itemscoreu.customattribute.CustomAttributesContainer;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

@Deprecated
public class CustomAttributePersistentDataType implements PersistentDataType<byte[], CustomAttributesContainer> {
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<CustomAttributesContainer> getComplexType() {
        return CustomAttributesContainer.class;
    }


    @Override
    public byte @NotNull [] toPrimitive(@NotNull CustomAttributesContainer customAttributesContainer, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return new byte[]{};
    }

    @NotNull
    @Override
    public CustomAttributesContainer fromPrimitive(@NotNull byte[] bytes, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            return (CustomAttributesContainer) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
