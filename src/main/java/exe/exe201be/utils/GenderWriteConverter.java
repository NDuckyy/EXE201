package exe.exe201be.utils;

import exe.exe201be.pojo.type.Gender;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class GenderWriteConverter implements Converter<Gender, String> {
    @Override
    public String convert(Gender source) {
        return source.getValue();
    }
}