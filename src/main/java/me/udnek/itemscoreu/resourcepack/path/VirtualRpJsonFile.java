package me.udnek.itemscoreu.resourcepack.path;

import com.google.common.base.Charsets;
import me.udnek.itemscoreu.resourcepack.VirtualResourcePack;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public class VirtualRpJsonFile extends RpPath{

    protected String data;

    public VirtualRpJsonFile(@Nullable VirtualResourcePack resourcePack, @NotNull String data, @NotNull String path) {
        super(resourcePack, path);
        this.data = data;
    }

    @Override
    public @NotNull InputStream getInputStream() {
        return IOUtils.toInputStream(data, Charsets.UTF_8);
    }
}
