package com.ryanair.flight.client;

import com.ryanair.flight.config.SchedulesApiClientConfig;
import com.ryanair.flight.domain.Schedule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SchedulesApiClientConfig.class})
class SchedulesApiClientTest {
    @Autowired
    private SchedulesApiClient client;

    @Test
    public void shouldReturnDataFromExternalSource() {
        Schedule schedule = client.getSchedule("DUB", "WRO", 2019, 3);
        Assertions.assertThat(schedule).isNotNull();
        Assertions.assertThat(schedule.getDays().isEmpty()).isFalse();
    }
}