package firealarm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class FireAlarmController {
    private final AlarmMessageFileDao alarmMessageFileDao;

    @Value("${alarm.message.timezone}")
    private String timezone;

    @Autowired
    public FireAlarmController(AlarmMessageFileDao alarmMessageFileDao)
    {
        this.alarmMessageFileDao = alarmMessageFileDao;
    }

    @GetMapping("/messages")
    @ResponseStatus(HttpStatus.OK)
    public List<AlarmMessageDTO> getAlarmMessages() throws IOException, ClassNotFoundException
    {
        return alarmMessageFileDao.getAllMessages();
    }

    @RequestMapping(value = "/submit", method = POST, consumes = APPLICATION_JSON_VALUE, produces =
        APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void postAlarmMessage(@RequestBody AlarmMessageDTO request) throws ClassNotFoundException, IOException
    {
        //TODO filter out profanity
        String sentAt = ZonedDateTime.now(ZoneId.of(this.timezone))
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        String id = UUID.randomUUID().toString();
        AlarmMessageDTO newAlarmMessage = new AlarmMessageDTO(id, request.isFireDepartmentCalled(),
            request.apartment(), request.comment(), sentAt);

        alarmMessageFileDao.save(newAlarmMessage);
        //TODO call whatsapp service
    }

    @ExceptionHandler({IOException.class, ClassNotFoundException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleFileExceptions() {
        return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();
    }
}
