package com.example.gccoffee.repository;

import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.Product;
import com.wix.mysql.EmbeddedMysql;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.distribution.Version.v5_7_10;
import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.MysqldConfig.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
class ProductJdbcRepositoryTest {
    static EmbeddedMysql embeddedMysql;

    @BeforeAll
    static void setUp(){
        var mysqldConfig = aMysqldConfig(v5_7_10)
                .withCharset(UTF8)
                .withPort(2215)
                .withUser("test", "test1234!")
                .withTimeZone("Asia/Seoul")
                .build();

        embeddedMysql = anEmbeddedMysql(mysqldConfig)
                .addSchema("test-order_mgmt", classPathScript("schema.sql"))
                .start();
    }

    @AfterAll
    static void cleanUp(){
        embeddedMysql.stop();
    }

    @AfterEach
    void cleanDB(){
        repository.deleteAll();
    }

    @Autowired
    ProductRepository repository;

    @Test
    @DisplayName("상품을 추가할 수 있다.")
    public void testInsert(){
        //given
        Product newProduct = new Product(UUID.randomUUID(),"new-product", Category.COFFEE_BEAN_PACKAGE,10000);

        //when
        repository.insert(newProduct);

        //then
        List<Product> all = repository.findAll();
        assertThat(all.isEmpty(),is(false));

    }

    @Test
    @DisplayName("상품을 id로 조회할 수 있다.")
    public void testFindById() {
        //given
        UUID productId = UUID.randomUUID();
        Product newProduct = new Product(productId,"new-product", Category.COFFEE_BEAN_PACKAGE,10000);
        repository.insert(newProduct);

        //when
        Optional<Product> findProduct = repository.findById(productId);

        //then
        assertThat(findProduct.get(),samePropertyValuesAs(newProduct));
    }

    @Test
    @DisplayName("상품을 이름으로 조회할 수 있다.")
    public void testFindByName() {
        //given
        String productName = "new-product";
        Product newProduct = new Product(UUID.randomUUID(), productName, Category.COFFEE_BEAN_PACKAGE,10000);
        repository.insert(newProduct);

        //when
        Optional<Product> findProduct = repository.findByName(productName);

        //then
        assertThat(findProduct.get(),samePropertyValuesAs(newProduct));
    }

    @Test
    @DisplayName("상품을 카테고리별로 조회할 수 있다.")
    public void testFindByCategory() {
        //given
        Product newProduct1 = new Product(UUID.randomUUID(), "new-product1", Category.COFFEE_BEAN_PACKAGE,10000);
        Product newProduct2 = new Product(UUID.randomUUID(), "new-product2", Category.COFFEE_BEAN_PACKAGE,20000);
        repository.insert(newProduct1);
        repository.insert(newProduct2);

        //when
        List<Product> products = repository.findByCategory(Category.COFFEE_BEAN_PACKAGE);

        //then
        assertThat(products,hasSize(2));
    }

    @Test
    @DisplayName("상품을 전체 삭제한다.")
    public void testDeleteAll() {
        //given
        Product newProduct1 = new Product(UUID.randomUUID(), "new-product1", Category.COFFEE_BEAN_PACKAGE,10000);
        Product newProduct2 = new Product(UUID.randomUUID(), "new-product2", Category.COFFEE_BEAN_PACKAGE,20000);
        repository.insert(newProduct1);
        repository.insert(newProduct2);

        //when
        repository.deleteAll();

        //then
        List<Product> products = repository.findAll();
        assertThat(products,hasSize(0));
    }

    @Test
    @DisplayName("상품 정보를 수정할 수 있다.")
    public void testUpdate() {
        //given
        Product newProduct = new Product(UUID.randomUUID(), "new-product", Category.COFFEE_BEAN_PACKAGE,10000);
        repository.insert(newProduct);

        //when
        newProduct.setProductName("update-product");
        repository.update(newProduct);

        //then
        Optional<Product> findProduct = repository.findById(newProduct.getProductId());
        assertThat(findProduct.get(), samePropertyValuesAs(newProduct));
    }
}