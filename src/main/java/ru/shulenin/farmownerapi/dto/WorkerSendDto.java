package ru.shulenin.farmownerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto рабочего для отправки сообщения
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerSendDto extends AbstractDto {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String surname;
}
