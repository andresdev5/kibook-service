package ec.edu.espe.kibook.config;

import ec.edu.espe.kibook.dto.BookDto;
import ec.edu.espe.kibook.entity.Book;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        TypeMap<Book, BookDto> bookDtoTypeMap = modelMapper.createTypeMap(Book.class, BookDto.class);
        bookDtoTypeMap.addMappings(mapper -> {
            mapper.skip(BookDto::setImage);
            mapper.map(Book::getImage, BookDto::setImageUrl);
        });

        return modelMapper;
    }
}
