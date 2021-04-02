package br.com.grillo.repository;

import br.com.grillo.model.entity.Category;
import br.com.grillo.util.DateUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class CategoryRepositoryTest {

    static final Long CODE = 39L;
    static final String NAME = "Bebidas";
    static final String DESCRIPTION = "Bebida sem alcool como Refrigerantes e Sucos";
    static final String STATUS = "A";
    static final String TRANSACTION_DATE = "2020-08-21T18:32:04.150";
    static final String URL = "/categories";

    @Autowired
    private CategoryRepository repository;

    @Test
    @DisplayName("Teste repository salvar categoria")
    @Order(1)
    void testSave() throws ParseException {

        Category category = Category.builder()
                .code(CODE)
                .name(NAME)
                .description(DESCRIPTION)
                .status(STATUS.charAt(0))
                .createdDate(DateUtils.getLocalDateTimeFromString
                        (TRANSACTION_DATE.concat("Z")))
                .updatedDate(DateUtils.getLocalDateTimeFromString
                        (TRANSACTION_DATE.concat("Z")))
                .build();

        Category response = repository.save(category);
        assertNotNull(response);
    }
}
