package uk.gov.digital.ho.systemregister.io.database;

import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;

@SuppressWarnings("CdiInjectionPointsInspection")
public interface Codec {
    boolean canEncode(Object event);
    EncodedData encode(Object event);

    boolean canDecode(String type);
    SR_Event decode(byte[] data, String typeId);

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
