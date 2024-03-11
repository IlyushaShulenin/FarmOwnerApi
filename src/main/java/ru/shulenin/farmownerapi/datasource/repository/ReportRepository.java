package ru.shulenin.farmownerapi.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.shulenin.farmownerapi.datasource.entity.Report;
import ru.shulenin.farmownerapi.dto.ProductReport;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    public List<Report> findAllByProductId(Long productId);
}