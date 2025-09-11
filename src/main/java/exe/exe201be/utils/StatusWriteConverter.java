package exe.exe201be.utils;

import exe.exe201be.pojo.type.Status;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class StatusWriteConverter implements Converter<Status, String> {
    @Override
    public String convert(Status source) {
        return source.getValue(); // Status.ACTIVE -> "active"
    }
}