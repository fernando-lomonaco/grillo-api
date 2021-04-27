package br.com.grillo.controller;

import br.com.grillo.dto.CategoryDTO;
import br.com.grillo.dto.resource.CategoryModelAssembler;
import br.com.grillo.model.Category;
import br.com.grillo.service.CategoryService;
import br.com.grillo.util.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryControllerTest {

    static final Long CODE = 39L;
    static final String NAME = "Bebidas";
    static final String DESCRIPTION = "Bebida sem alcool como Refrigerantes e Sucos";
    static final String STATUS = "A";
    static final String TRANSACTION_DATE = "2020-08-21T18:32:04.150";
    static final String URL = "/categories";
    static final String EXTERNAL_CODE = "05ae9332-394f-4d6a-8823-2245f6d52bce";

    private HttpHeaders headers;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private CategoryModelAssembler assembler;

    @BeforeAll
    void setUp() {
        headers = new HttpHeaders();
        headers.set("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsb21vbmFjbyIsImlhdCI6MTYwMDk2Mzk0NiwiZXhwIjoxNjAxMDUwMzQ2fQ.MF6xpM8qFLqOb8yPi3nCppFdVMpADU4uZlLvTktJXZQtTQPnt2B_kAwBS8w17Xr6_nysmaxLPOpmrLOlbKaJ-Q");
    }

    @AfterAll
    void tearDown() {
        categoryService.delete(39L);
    }

    @Test
    @DisplayName("Teste da acao - salvar categoria")
    @Order(1)
    void testEndpointSave() throws Exception {

        Category category = getMockCategory();

        BDDMockito.given(categoryService.save(Mockito.any(Category.class))).willReturn(category);

        mockMvc.perform(post(URL).content(getJsonPayload(category))
                .contentType(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.code").value(CODE))
                .andExpect(jsonPath("$.data.name").value(NAME))
                .andExpect(jsonPath("$.data.description").value(DESCRIPTION))
                .andExpect(jsonPath("$.data.status").value(STATUS))
                .andExpect(jsonPath("$.data.externalCode").value(EXTERNAL_CODE))
                .andExpect(jsonPath("$.data.createdDate").value(TRANSACTION_DATE))
                .andExpect(jsonPath("$.data.updatedDate").value(TRANSACTION_DATE));
    }

    @Test
    @DisplayName("Teste da acao - salvar categoria com campo invalido")
    @Order(2)
    void testEndpointSaveInvalidTransaction() throws Exception {

        BDDMockito.given(categoryService.save(Mockito.any(Category.class))).willReturn(getMockCategory());

        Category category = Category.builder()
                .code(CODE)
                .name(null)
                .description(DESCRIPTION)
                .status(STATUS.charAt(0))
                .createdDate(DateUtils.getLocalDateTimeFromString
                        (TRANSACTION_DATE.concat("Z")))
                .updatedDate(DateUtils.getLocalDateTimeFromString
                        (TRANSACTION_DATE.concat("Z")))
                .externalCode(UUID.fromString(EXTERNAL_CODE))
                .build();

        mockMvc.perform(post(URL).content(getJsonPayload(category))
                .contentType(MediaTypes.HAL_JSON)
                .headers(headers))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request has invalids fields"));

    }

    @Test
    @DisplayName("Teste da acao - capturar categoria com id")
    @Order(3)
    void getByCode() throws Exception {
        Category category = getMockCategory();
        BDDMockito.given(categoryService.get(category.getCode())).willReturn(Optional.of(category));

        mockMvc.perform(get(URL + "/{code}", category.getCode()).content(getJsonPayload(category))
                .contentType(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.code").value(CODE))
                .andExpect(jsonPath("$.data.name").value(NAME))
                .andExpect(jsonPath("$.data.description").value(DESCRIPTION))
                .andExpect(jsonPath("$.data.status").value(STATUS))
                .andExpect(jsonPath("$.data.externalCode").value(EXTERNAL_CODE))
                .andExpect(jsonPath("$.data.createdDate").value(TRANSACTION_DATE))
                .andExpect(jsonPath("$.data.updatedDate").value(TRANSACTION_DATE));
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

    private String getJsonPayload(Category category) throws JsonProcessingException {

        CategoryDTO model = assembler.toModel(category);
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(model);
    }



}