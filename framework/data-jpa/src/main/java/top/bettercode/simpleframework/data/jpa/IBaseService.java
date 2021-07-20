package top.bettercode.simpleframework.data.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author Peter Wu
 */
public interface IBaseService<T, ID, M extends BaseRepository<T, ID>> {

  M getRepository();

  List<T> findAll();

  List<T> findAll(Sort sort);

  List<T> findAllById(Iterable<ID> ids);

  <S extends T> List<S> saveAll(Iterable<S> entities);

  void deleteAllInBatch(Iterable<T> entities);

  void deleteAllInBatch();

  <S extends T> List<S> findAll(Example<S> example);

  <S extends T> List<S> findAll(Example<S> example, Sort sort);

  Page<T> findAll(Pageable pageable);

  <S extends T> S save(S s);

  /**
   * 动态更新，只更新非Null字段
   *
   * @param s   对象
   * @param <S> 类型
   * @return 结果
   */
  <S extends T> S dynamicSave(S s);

  /**
   * 动态更新，只更新非Null 及 非空（""）字段
   *
   * @param s   对象
   * @param <S> 类型
   * @return 结果
   */
  <S extends T> S dynamicBSave(S s);

  Optional<T> findById(ID id);

  boolean existsById(ID id);

  long count();

  void deleteById(ID id);

  void delete(T t);

  void deleteAll(Iterable<? extends T> iterable);

  void deleteAll();

  <S extends T> Optional<S> findOne(Example<S> example);

  <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

  <S extends T> long count(Example<S> example);

  <S extends T> boolean exists(Example<S> example);

  <S extends T> Page<S> findAll(Example<S> example, Pageable pageable, Sort sort);

  Page<T> findAll(Pageable pageable, Sort sort);
}
