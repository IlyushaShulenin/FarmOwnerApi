package ru.shulenin.farmownerapi.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import ru.shulenin.farmownerapi.dto.PlanSaveEditDto;

import java.time.Instant;
import java.time.LocalDate;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class PlanServiceTest {
    private final PlanService planService;

    @Test
    public void saveTest() {
        PlanSaveEditDto dto = new PlanSaveEditDto(1L, 1L, 1000, LocalDate.now());
        var read = planService.save(dto).get();
        Assertions.assertThat(read.getWorker().getName()).isEqualTo("Jhon");
        Assertions.assertThat(read.getProduct().getName()).isEqualTo("Milk");
    }
}
