package ru.shulenin.farmownerapi.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shulenin.farmownerapi.datasource.entity.Report;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    public List<Report> findAllByProductId(Long productId);
}