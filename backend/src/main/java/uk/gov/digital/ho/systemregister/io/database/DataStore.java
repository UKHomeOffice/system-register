package uk.gov.digital.ho.systemregister.io.database;

import java.time.Instant;
import java.util.List;

import static java.time.Instant.EPOCH;

@SuppressWarnings("CdiInjectionPointsInspection")
public interface DataStore {
    void save(byte[] data, String typeId, Instant timestamp) throws DataStoreException;

    default List<EncodedData> getData() throws DataStoreException {
        return getData(EPOCH);
    }

    List<EncodedData> getData(Instant after) throws DataStoreException;

    class EncodedData {
        private final byte[] data;
        private final String typeId;

        public EncodedData(byte[] data, String typeId) {
            this.data = data.clone();
            this.typeId = typeId;
        }

        public byte[] getData() {
            return data.clone();
        }

        public String getTypeId() {
            return typeId;
        }
    }
}
