package br.com.grillo.repository;

import br.com.grillo.manager.LifeCycleManager;
import br.com.grillo.model.entity.Category;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("integration-test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(initializers = {LifeCycleManager.class})
public class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeAll
    void setUp() {

        Category category = new Category(null, "Teste nome", "Teste descricao", 'A', LocalDateTime.now(), LocalDateTime.now());

        categoryRepository.save(category);
    }

    @Test
    @Order(1)
    public void testSave() {

        Category category = new Category(null, "Teste nome", "Teste descricao", 'A', LocalDateTime.now(), LocalDateTime.now());

        Category response = categoryRepository.save(category);

        assertNotNull(response);
    }

    @Test
    @Order(1)
    public void testFindById() {

        Category category = new Category(null, "Teste nome", "Teste descricao", 'A', LocalDateTime.now(), LocalDateTime.now());
        Category save = categoryRepository.save(category);

        Optional<Category> response = categoryRepository.findById(save.getCode());

        assertNotNull(response.isPresent() ? response.get() : Optional.empty());
    }

    @AfterAll
    void tearDown() {
        categoryRepository.deleteAll();
    }
}
