package uk.gov.digital.ho.systemregister.io.database;

import edu.umd.cs.findbugs.annotations.NonNull;

public abstract class AbstractVersionedCodec implements Codec {
    private final int version;

    protected AbstractVersionedCodec(int version) {
        this.version = version;
    }

    @Override
    public int compareTo(@NonNull Codec other) {
        if (!(other instanceof AbstractVersionedCodec)) {
            throw new IllegalArgumentException("unable to determine version of codec: " + other);
        }
        return this.version - ((AbstractVersionedCodec) other).version;
    }
}
