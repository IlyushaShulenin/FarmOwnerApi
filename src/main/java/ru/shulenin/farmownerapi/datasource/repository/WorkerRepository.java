package ru.shulenin.farmownerapi.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.shulenin.farmownerapi.datasource.entity.Worker;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    @Modifying
    @Query(
            value = "UPDATE worker SET is_working = false WHERE id = :id",
            nativeQuery = true
    )
    public void retireWorker(Long id);

    @Query(
            value = "SELECT * FROM worker WHERE is_working = true",
            nativeQuery = true
    )
    public List<Worker> findAll();

    @Query(
            value = "SELECT * FROM worker WHERE is_working = true AND id = :id",
            nativeQuery = true
    )
    public Optional<Worker> findById(Long id);
}
