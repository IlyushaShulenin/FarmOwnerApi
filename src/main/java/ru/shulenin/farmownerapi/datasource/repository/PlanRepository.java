package ru.shulenin.farmownerapi.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shulenin.farmownerapi.datasource.entity.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    public void deleteAllByWorkerId(Long workerId);
}
