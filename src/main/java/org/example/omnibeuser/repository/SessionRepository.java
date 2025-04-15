package org.example.omnibeuser.repository;

import org.example.omnibeuser.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
