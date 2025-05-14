package com.korutil.server.repository.jpa.user;

import com.korutil.server.domain.user.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long>, TodoQuerydslRepository {
    List<TodoEntity> findAllByUserId(Long userid);
    Optional<TodoEntity> findByIdAndUserId(Long userid, Long userId);
}
