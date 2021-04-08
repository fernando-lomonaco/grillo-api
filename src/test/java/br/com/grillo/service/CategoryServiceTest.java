package br.com.grillo.service;

import br.com.grillo.model.Category;
import br.com.grillo.repository.CategoryRepository;
import br.com.grillo.util.DateUtils;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryServiceTest {

    static final Long CODE = 39L;
    static final String NAME = "Bebidas";
    static final String DESCRIPTION = "Bebida sem alcool como Refrigerantes e Sucos";
    static final String STATUS = "A";
    static final String TRANSACTION_DATE = "2020-08-21T18:32:04.150";
    static final String EXTERNAL_CODE = "05ae9332-394f-4d6a-8823-2245f6d52bce";

    @Autowired
    private CategoryService service;

    @MockBean
    private CategoryRepository repository;

    @Test
    @Order(1)
    void testSave() throws ParseException {

        BDDMockito.given(repository.save(Mockito.any(Category.class)))
                .willReturn(getMockCategory());
        Category response = service.save(new Category());

        assertNotNull(response);
        assertEquals(39L, response.getCode());
        assertEquals("Bebidas", response.getName());
    }

    private Category getMockCategory() throws ParseException {

        return Category.builder()
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

    }

    @AfterAll
    private void tearDown() {
        repository.deleteAll();
    }
}
