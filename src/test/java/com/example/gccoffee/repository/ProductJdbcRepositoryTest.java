package com.example.gccoffee.repository;

import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.Product;
import com.wix.mysql.EmbeddedMysql;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
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
}