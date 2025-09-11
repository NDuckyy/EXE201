package exe.exe201be.config;

import exe.exe201be.utils.GenderReadConverter;
import exe.exe201be.utils.GenderWriteConverter;
import exe.exe201be.utils.StatusReadConverter;
import exe.exe201be.utils.StatusWriteConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(
                List.of(
                        new StatusReadConverter(),
                        new StatusWriteConverter(),

                        new GenderReadConverter(),
                        new GenderWriteConverter()
                )
        );
    }

}
