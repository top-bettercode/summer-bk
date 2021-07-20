package top.bettercode.simpleframework.data.test.test;

import top.bettercode.simpleframework.data.test.domain.User;
import top.bettercode.simpleframework.data.test.repository.UserRepository;
import com.github.pagehelper.PageHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MybatisTest {

  @Autowired
  UserRepository repository;
  @Autowired
  SqlSession sqlSession;
  Integer carterId;

  @BeforeEach
  public void setUp() {
    User dave = new User("Dave", "Matthews");
    repository.save(dave);
    User dave1 = new User("Dave", "Matthews");
    repository.save(dave1);
    User carter = new User("Carter", "Beauford1");
    repository.save(carter);
    carter = new User("Carter", "Beauford2");
    repository.save(carter);
    carterId = carter.getId();
    repository.delete(dave);
  }

  @AfterEach
  public void tearDown() {
    repository.deleteAll();
    repository.cleanRecycleBin();
  }

  @Test
  public void findByMybatis() {
    List<User> users = repository.findByMybatis();
    System.err.println(users);
    org.junit.jupiter.api.Assertions.assertEquals(3, users.size());
  }

  @Test
  public void insert() {
    int insert = repository.insert("Wu", "Peter");
    List<User> peter = repository.findByLastname("Peter");
    System.err.println(peter);
    org.junit.jupiter.api.Assertions.assertEquals(1, insert);
    org.junit.jupiter.api.Assertions.assertEquals(1, peter.size());
  }

  @Test
  public void update() {
    int update = repository.update(carterId, "Peter");
    Optional<User> userOptional = repository.findById(carterId);
    org.junit.jupiter.api.Assertions.assertTrue(userOptional.isPresent());
    User peter = userOptional.get();
    System.err.println(peter);
    org.junit.jupiter.api.Assertions.assertEquals(1, update);
    org.junit.jupiter.api.Assertions.assertEquals("Peter", peter.getLastname());
  }

  @Test
  public void deleteBy() {
    int delete = repository.deleteBy(carterId);
    Optional<User> userOptional = repository.findById(carterId);
    org.junit.jupiter.api.Assertions.assertFalse(userOptional.isPresent());
    org.junit.jupiter.api.Assertions.assertEquals(1, delete);
  }

  @Test
  public void findByMybatis2() {
    List<User> users = repository.findByMybatis2("Carter", "Beauford1");
    System.err.println(users);
    org.junit.jupiter.api.Assertions.assertEquals(1, users.size());
  }

  @Test
  public void findByMybatis222() {
    Map<String, String> params = new HashMap<>();
    params.put("firstname", "Carter");
    params.put("param2", "Beauford1");
    List<User> users = repository.findByMybatis2(params);
    System.err.println(users);
    org.junit.jupiter.api.Assertions.assertEquals(1, users.size());
  }

  @Test
  public void findByMybatis2222() {
    Map<String, String> params = new HashMap<>();
    params.put("firstname", "Carter");
    Page<User> users = repository
        .findByMybatis2(PageRequest.of(0, 1, Sort.by(Direction.ASC, "lastname")), params);
    System.err.println(users);
    org.junit.jupiter.api.Assertions.assertEquals(2, users.getTotalElements());
    List<User> userList = users.getContent();
    org.junit.jupiter.api.Assertions.assertEquals(1, userList.size());
    org.junit.jupiter.api.Assertions.assertEquals("Beauford1", userList.get(0).getLastname());
    org.junit.jupiter.api.Assertions.assertEquals("Beauford2", repository
        .findByMybatis2(PageRequest.of(0, 1, Sort.by(Direction.DESC, "lastname")), params)
        .getContent().get(0).getLastname());
    List<User> users2 = repository.findByMybatis2(params);
    System.err.println(users2);
    org.junit.jupiter.api.Assertions.assertEquals(2, users2.size());
    org.junit.jupiter.api.Assertions.assertEquals("Beauford1", users2.get(0).getLastname());
  }

  @Test
  public void findByMybatis22() {
    List<User> users = repository.findByMybatis22(new User("Carter", "Beauford1"));
    System.err.println(users);
    org.junit.jupiter.api.Assertions.assertEquals(1, users.size());
  }


  @Test
  public void findByMybatis2212() {
    User user = new User("Carter", null);
    Page<User> users = repository
        .findByMybatis22(user, PageRequest.of(0, 1, Sort.by(Direction.ASC, "lastname")));
    System.err.println(users);
    org.junit.jupiter.api.Assertions.assertEquals(2, users.getTotalElements());
    org.junit.jupiter.api.Assertions.assertEquals(1, users.getContent().size());
    org.junit.jupiter.api.Assertions
        .assertEquals("Beauford1", users.getContent().get(0).getLastname());
    users = repository
        .findByMybatis22(user, PageRequest.of(0, 1, Sort.by(Direction.DESC, "lastname")));
    System.err.println(users);
    org.junit.jupiter.api.Assertions.assertEquals(2, users.getTotalElements());
    org.junit.jupiter.api.Assertions.assertEquals(1, users.getContent().size());
    org.junit.jupiter.api.Assertions
        .assertEquals("Beauford2", users.getContent().get(0).getLastname());
  }

  @Test
  public void findByMybatis22122() {
    Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
          User user = new User("Carter", null);
          repository
              .findByMybatis222(user, PageRequest.of(0, 1, Sort.by(Direction.ASC, "lastname")));
        },
        "当包含org.springframework.data.domain.Pageable参数时返回类型必须为org.springframework.data.domain.Page");

  }

  @Test
  public void findOneByMybatis() {
    User user = repository.findOneByMybatis("Dave");
    System.err.println(user);
    org.junit.jupiter.api.Assertions.assertNotNull(user);
    user = repository.findOneByMybatis("Dave2");
    System.err.println(user);
    org.junit.jupiter.api.Assertions.assertNull(user);
  }

  @Test
  public void findByMybatis3() {
    List<User> users = repository.findByMybatis3("Carter", Sort.by(Direction.ASC, "lastname"));
    System.err.println(users);
    org.junit.jupiter.api.Assertions.assertEquals(2, users.size());
    org.junit.jupiter.api.Assertions.assertEquals("Beauford1", users.get(0).getLastname());
    users = repository.findByMybatis3("Carter", Sort.by(Direction.DESC, "lastname"));
    System.err.println(users);
    org.junit.jupiter.api.Assertions.assertEquals(2, users.size());
    org.junit.jupiter.api.Assertions.assertEquals("Beauford2", users.get(0).getLastname());
  }

  @Test
  public void findByMybatis30() {
    Page<User> users = repository
        .findByFirstname("Carter", PageRequest.of(0, 1, Sort.by(Direction.ASC, "lastname")));
    System.err.println(users);
    org.junit.jupiter.api.Assertions.assertEquals(2, users.getTotalElements());
    org.junit.jupiter.api.Assertions.assertEquals(1, users.getContent().size());
    org.junit.jupiter.api.Assertions
        .assertEquals("Beauford1", users.getContent().get(0).getLastname());
    users = repository
        .findByFirstname("Carter", PageRequest.of(0, 1, Sort.by(Direction.DESC, "lastname")));
    System.err.println(users);
    org.junit.jupiter.api.Assertions.assertEquals(2, users.getTotalElements());
    org.junit.jupiter.api.Assertions.assertEquals(1, users.getContent().size());
    org.junit.jupiter.api.Assertions
        .assertEquals("Beauford2", users.getContent().get(0).getLastname());
    users = repository
        .findByFirstname("Carter", PageRequest.of(1, 1, Sort.by(Direction.DESC, "lastname")));
    System.err.println(users);
    org.junit.jupiter.api.Assertions.assertEquals(2, users.getTotalElements());
    org.junit.jupiter.api.Assertions.assertEquals(1, users.getContent().size());
    org.junit.jupiter.api.Assertions
        .assertEquals("Beauford1", users.getContent().get(0).getLastname());
  }

  @Test
  public void findByMybatis31() {
    Page<User> users = repository
        .findByMybatis3("Carter", PageRequest.of(0, 1, Sort.by(Direction.ASC, "lastname")));
    System.err.println(users);
    org.junit.jupiter.api.Assertions.assertEquals(2, users.getTotalElements());
    org.junit.jupiter.api.Assertions.assertEquals(1, users.getContent().size());
    org.junit.jupiter.api.Assertions
        .assertEquals("Beauford1", users.getContent().get(0).getLastname());
    users = repository
        .findByMybatis3("Carter", PageRequest.of(0, 1, Sort.by(Direction.DESC, "lastname")));
    System.err.println(users);
    org.junit.jupiter.api.Assertions.assertEquals(2, users.getTotalElements());
    org.junit.jupiter.api.Assertions.assertEquals(1, users.getContent().size());
    org.junit.jupiter.api.Assertions
        .assertEquals("Beauford2", users.getContent().get(0).getLastname());
    users = repository
        .findByMybatis3("Carter", PageRequest.of(1, 1, Sort.by(Direction.DESC, "lastname")));
    System.err.println(users);
    org.junit.jupiter.api.Assertions.assertEquals(2, users.getTotalElements());
    org.junit.jupiter.api.Assertions.assertEquals(1, users.getContent().size());
    org.junit.jupiter.api.Assertions
        .assertEquals("Beauford1", users.getContent().get(0).getLastname());
  }

  @Test
  public void findByMybatis2233() {
    com.github.pagehelper.Page<Object> page = PageHelper.startPage(2, 2).doSelectPage(
        () -> sqlSession
            .selectList(UserRepository.class.getName() + ".findByMybatis"));
    System.err.println(page.getResult());
    System.err.println(page.getTotal());
  }

}