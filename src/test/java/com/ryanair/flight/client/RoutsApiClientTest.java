package com.ryanair.flight.client;

import com.ryanair.flight.config.RoutsApiClientConfig;
import com.ryanair.flight.domain.Rout;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RoutsApiClientConfig.class})
class RoutsApiClientTest {

    @Autowired
    private RoutsApiClient client;

    @Test
    public void shouldReturnDataFromExternalSource() {
        List<Rout> routs = client.getRouts();
        Assertions.assertThat(routs.isEmpty()).isFalse();
    }
}