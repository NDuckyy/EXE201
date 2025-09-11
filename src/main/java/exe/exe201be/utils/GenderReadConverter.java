package exe.exe201be.utils;

import exe.exe201be.pojo.type.Gender;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class GenderReadConverter implements Converter<String, Gender> {
    @Override
    public Gender convert(String source) {
        return Gender.fromValue(source);
    }

}
