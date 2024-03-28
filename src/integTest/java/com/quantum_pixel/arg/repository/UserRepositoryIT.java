package com.quantum_pixel.arg.repository;

import com.quantum_pixel.arg.ConfigTest;
import com.quantum_pixel.arg.user.model.User;
import com.quantum_pixel.arg.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.INFERRED;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = "/db/users.sql",
        config = @SqlConfig(transactionMode = INFERRED),
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class UserRepositoryIT extends ConfigTest {


    @Autowired
    private UserRepository repository;


    @Test
    public void testContainer() {
        Assertions.assertTrue(postgresContainer.isCreated());
        System.out.println("true");
        Assertions.assertTrue(postgresContainer.isRunning());
        User user = new User();
        user.setFirstName("Luka");
        user.setLastName("Buziu");
        repository.save(user);
        Assertions.assertEquals(31, repository.findAll().size());
        System.out.println(repository.findAll().size());

    }

    @Test
    public void testContainerSecond() {
        User user = new User();
        user.setFirstName("Luka");
        user.setLastName("Buziu");
        User save = repository.save(user);
        List<User> users = repository.findAll();
        assertThat(users)
                .hasSize(31)
                .contains(save);

    }

    @Override
    protected List<String> getTableToTruncate() {
        return Collections.emptyList();
    }
}
