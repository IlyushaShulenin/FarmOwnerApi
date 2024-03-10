package ru.shulenin.farmownerapi.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shulenin.farmownerapi.datasource.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {}