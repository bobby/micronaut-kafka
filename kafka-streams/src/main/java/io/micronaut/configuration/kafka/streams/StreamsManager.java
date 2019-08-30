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
import java.util.Map;
import java.util.Set;

/**
 *
 */
public interface StreamsManager extends AutoCloseable {

    /**
     *
     */
    void close();

    /**
     *
     * @return Map<MetricName, ? extends Metric>
     */
    Map<MetricName, ? extends Metric> metrics();

    /**
     *
     * @return streams metadata collection
     */
    Collection<StreamsMetadata> allMetadata();

    /**
     *
     * @param storeName the store name
     * @return streams metadata collection
     */
    Collection<StreamsMetadata> allMetadataForStore(String storeName);

    /**
     *
     */
    void cleanUp();

    /**
     *
     * @param timeout the closing stream timeout
     */
    void close(Duration timeout);

    /**
     *
     * @return set of thread metadata
     */
    Set<ThreadMetadata> localThreadsMetadata();

    /**
     *
     * @param storeName the store name
     * @param key metadata key
     * @param keySerializer the key serializer
     * @param <K> key serializer type param
     * @return the streams metadata
     */
    <K> StreamsMetadata metadataForKey(String storeName, K key, Serializer<K> keySerializer);

    /**
     *
     * @param storeName the store name
     * @param key metadata key
     * @param partitioner the stream partioner
     * @param <K> the stream partitioner key type parameter
     * @return the metadata
     */
    <K> StreamsMetadata metadataForKey(String storeName, K key, StreamPartitioner<? super K, ?> partitioner);

    /**
     *
     * @param globalStateRestoreListener The global restore state listener
     */
    void setGlobalStateRestoreListener(StateRestoreListener globalStateRestoreListener);

    /**
     *
     * @param listener the stream state listener
     */
    void setStateListener(KafkaStreams.StateListener listener);

    /**
     *
     * @param eh the uncaught exception handler
     */
    void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler eh);

    /**
     *
     */
    void start();

    /**
     *
     * @return the stream state
     */
    KafkaStreams.State state();

    /**
     *
     * @param storeName the store name
     * @param queryableStoreType The store type
     * @param <T> The store param type
     * @return The store
     */
    <T> T store(String storeName, QueryableStoreType<T> queryableStoreType);

    /**
     *
     * @param advanceMs amount to advance wall clock
     */
    void advanceWallClockTime(long advanceMs);

    /**
     *
     * @return map of state stores
     */
    Map<String, StateStore> getAllStateStores();

    /**
     *
     * @param name The KV store name
     * @param <K> The key param type
     * @param <V> The value param type
     * @return the Key Value Store
     */
    <K, V> KeyValueStore<K, V> getKeyValueStore(String name);

    /**
     *
     * @param name The session store name
     * @param <K> The key param type
     * @param <V> The value param type
     * @return The session store
     */
    <K, V> SessionStore<K, V> getSessionStore(String name);

    /**
     *
     * @param name The state store name
     * @return The state store
     */
    StateStore getStateStore(String name);

    /**
     *
     * @param name The window store name
     * @param <K> The key param type
     * @param <V> The value param type
     * @return The window store tuple
     */
    <K, V> WindowStore<K, V> getWindowStore(String name);

    /**
     *
     * @param consumerRecord The consumer record
     */
    void pipeInput(ConsumerRecord<byte[], byte[]> consumerRecord);

    /**
     *
     * @param records The list of consumer records
     */
    void pipeInput(java.util.List<ConsumerRecord<byte[], byte[]>> records);

    /**
     *
     * @param topic The output topic
     * @return The producer record
     */
    ProducerRecord<byte[], byte[]> readOutput(java.lang.String topic);

    /**
     *
     * @param topic The producer topic
     * @param keyDeserializer The key deserializer
     * @param valueDeserializer The value deserializer
     * @param <K> The producer key param type
     * @param <V> The producer value param type
     * @return The producer record key value pair
     */
    <K, V> ProducerRecord<K, V> readOutput(java.lang.String topic, Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer);
}
