/*
 * Copyright 2017-2019 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.configuration.kafka.streams;

import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.EachBean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import org.apache.kafka.streams.KafkaStreams;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * A factory that constructs the {@link KafkaStreams} bean.
 *
 * @author Graeme Rocher
 * @since 1.0
 */
@Factory
public class KafkaStreamsFactory implements Closeable {
    private final Collection<StreamsManager> streamsManagers = new ConcurrentLinkedDeque<>();

    /**
     * Builds the default {@link StreamsManager} bean from the configuration and the supplied {@link ConfiguredStreamBuilder}.
     *
     * @param builder The builder
     * @return The {@link StreamsManager} bean
     */
    @EachBean(KafkaStreamsConfiguration.class)
    @Requires(notEnv = "mock-kafka-streams")
    @Context
    StreamsManager kafkaStreams(ConfiguredStreamBuilder builder) {
        StreamsManager manager = new KafkaStreamsManager(new KafkaStreams(
                builder.build(),
                builder.getConfiguration()
        ));
        streamsManagers.add(manager);
        manager.start();
        return manager;
    }

    @Override
    @PreDestroy
    public void close() {
        for (StreamsManager manager : streamsManagers) {
            try {
                manager.close(Duration.ofSeconds(3));
            } catch (Exception e) {
                // ignore
            }
        }
    }
}
