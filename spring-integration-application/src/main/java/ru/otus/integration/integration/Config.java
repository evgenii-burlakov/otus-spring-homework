package ru.otus.integration.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.GenericSelector;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.integration.domain.Bar;

@Configuration
public class Config {
    @Bean
    public QueueChannel morningChannel() {
        return MessageChannels.queue(10).get();
    }

    @Bean
    public PublishSubscribeChannel barChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public GenericSelector<Bar> freeBars() {
        return new GenericSelector<Bar>() {

            @Override
            public boolean accept(Bar bar) {
                if (!bar.isFree()) {
                    System.out.println(bar.getName() + " successfully completed the working day");
                }

                return bar.isFree();
            }
        };
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2).get();
    }

    @Bean
    public IntegrationFlow dayFlow() {
        return IntegrationFlows.from("morningChannel")
                .handle("hardWorkerHolidayService", "tryingFindFreeBar")
                .split()
                .handle("barService", "isBarFree")
                .filter(freeBars())
                .aggregate(a -> {
                    a.groupTimeout(1L);
                    a.sendPartialResultOnExpiry(true);
                })
                .handle("barService", "barDayResult")
                .get();
    }
}