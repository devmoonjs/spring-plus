package org.example.expert.log.repository;

import org.example.expert.log.entity.ManagerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<ManagerLog, Long> {

}
