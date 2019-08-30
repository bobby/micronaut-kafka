package io.micronaut.configuration.kafka.streams;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.processor.StateRestoreListener;
import org.apache.kafka.streams.processor.StateStore;
import org.apache.kafka.streams.processor.StreamPartitioner;
import org.apache.kafka.streams.processor.ThreadMetadata;
import org.apache.kafka.streams.state.*;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class KafkaStreamsManager implements StreamsManager {

    /**
     *
     */
    private final KafkaStreams kafkaStreams;

    /**
     *
     * @param kafkaStreams
     */
    KafkaStreamsManager(KafkaStreams kafkaStreams) {
        this.kafkaStreams = kafkaStreams;
    }

    @Override
    public void close() {
        kafkaStreams.close();
    }

    @Override
    public Map<MetricName, ? extends Metric> metrics() {
        return kafkaStreams.metrics();
    }

    @Override
    public Collection<StreamsMetadata> allMetadata() {
        return kafkaStreams.allMetadata();
    }

    @Override
    public Collection<StreamsMetadata> allMetadataForStore(String storeName) {
        return kafkaStreams.allMetadataForStore(storeName);
    }

    @Override
    public void cleanUp() {
        kafkaStreams.cleanUp();
    }

    @Override
    public void close(Duration timeout) {
        kafkaStreams.close(timeout);
    }

    @Override
    public Set<ThreadMetadata> localThreadsMetadata() {
        return kafkaStreams.localThreadsMetadata();
    }

    @Override
    public <K> StreamsMetadata metadataForKey(String storeName, K key, Serializer<K> keySerializer) {
        return kafkaStreams.metadataForKey(storeName, key, keySerializer);
    }

    @Override
    public <K> StreamsMetadata metadataForKey(String storeName, K key, StreamPartitioner<? super K, ?> partitioner) {
        return kafkaStreams.metadataForKey(storeName, key, partitioner);
    }

    @Override
    public void setGlobalStateRestoreListener(StateRestoreListener globalStateRestoreListener) {
        kafkaStreams.setGlobalStateRestoreListener(globalStateRestoreListener);
    }

    @Override
    public void setStateListener(KafkaStreams.StateListener listener) {
        kafkaStreams.setStateListener(listener);
    }

    @Override
    public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler eh) {
        kafkaStreams.setUncaughtExceptionHandler(eh);
    }

    @Override
    public void start() {
        kafkaStreams.start();
    }

    @Override
    public KafkaStreams.State state() {
        return kafkaStreams.state();
    }

    @Override
    public <T> T store(String storeName, QueryableStoreType<T> queryableStoreType) {
        return kafkaStreams.store(storeName, queryableStoreType);
    }

    @Override
    public void advanceWallClockTime(long advanceMs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, StateStore> getAllStateStores() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, V> KeyValueStore<K, V> getKeyValueStore(String name) {
        return (KeyValueStore<K, V>) kafkaStreams.store(name, QueryableStoreTypes.keyValueStore());
    }

    @Override
    public <K, V> SessionStore<K, V> getSessionStore(String name) {
        return (SessionStore<K, V>) kafkaStreams.store(name, QueryableStoreTypes.sessionStore());
    }

    @Override
    public StateStore getStateStore(String name) {
        StateStore store = getKeyValueStore(name);
        if (store == null) {
            store = getSessionStore(name);
        }
        if (store == null) {
            store = getWindowStore(name);
        }
        return store;
    }

    @Override
    public <K, V> WindowStore<K, V> getWindowStore(String name) {
        return (WindowStore<K, V>) kafkaStreams.store(name, QueryableStoreTypes.windowStore());
    }

    @Override
    public void pipeInput(ConsumerRecord<byte[], byte[]> consumerRecord) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void pipeInput(List<ConsumerRecord<byte[], byte[]>> records) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProducerRecord<byte[], byte[]> readOutput(String topic) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K, V> ProducerRecord<K, V> readOutput(String topic, Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer) {
        throw new UnsupportedOperationException();
    }
}
