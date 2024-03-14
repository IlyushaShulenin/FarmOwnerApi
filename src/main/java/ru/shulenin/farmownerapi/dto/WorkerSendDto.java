package ru.shulenin.farmownerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Dto рабочего для отправки сообщения
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerSendDto extends AbstractDto {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private List<Long> scoreId;
    private List<Long> planId;
    private List<Long> reportId;
}
