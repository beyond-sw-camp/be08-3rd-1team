package illvent.backend.event.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class LocalDateRange{
    private LocalDate startDate;
    private LocalDate endDate;
}
