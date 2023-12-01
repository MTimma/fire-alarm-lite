package firealarm;

import java.io.Serializable;

public record AlarmMessageDTO(String id, boolean isFireDepartmentCalled, int apartment, String comment, String sentAt)
    implements Serializable {
}
