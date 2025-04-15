package org.example.omnibeuser.repository;

import org.example.omnibeuser.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    boolean existsByRefresh(String refresh);
    void deleteByLoginId(String loginId);
}
