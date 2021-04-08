package br.com.grillo.repository;

import br.com.grillo.model.Category;
import br.com.grillo.util.DateUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
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
    static final String EXTERNAL_CODE = "05ae9332-394f-4d6a-8823-2245f6d52bce";

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
                .externalCode(UUID.fromString(EXTERNAL_CODE))
                .build();

        Category response = repository.save(category);
        assertNotNull(response);
    }

    @AfterAll
    void tearDown() {
        repository.deleteAll();
    }
}
