package io.github.myifeng.swan.demo.dao;

import io.github.myifeng.swan.demo.entity.DemoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoDao extends JpaRepository<DemoEntity, Long> {
    <T> Page<T> findAllBy(Pageable pageable, Class<T> type);
}
