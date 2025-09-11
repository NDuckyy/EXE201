package exe.exe201be.utils;

import exe.exe201be.pojo.type.Status;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class StatusReadConverter implements Converter<String, Status> {
    @Override
    public Status convert(String source) {
        return Status.fromValue(source); // "active" -> Status.ACTIVE
    }

}




