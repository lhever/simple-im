package com.lhever.common.kafka;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/16 19:01
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/16 19:01
 * @modify by reason:{方法名}:{原因}
 */


public class SimpleKafkaManager {

    private Properties properties;

    public SimpleKafkaManager(String servers) {
        this.properties = new Properties();
        properties.put("bootstrap.servers", servers);
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", StringSerializer.class.getName());

    }


    public void createTopic(String name, Optional<Integer> numPartitions, Optional<Short> replicationFactor) {
        AdminClient adminClient = null;
        try {
            adminClient = KafkaAdminClient.create(properties);
            NewTopic newTopic = new NewTopic(name, numPartitions, replicationFactor);
            Collection<NewTopic> newTopicList = new ArrayList<>();
            newTopicList.add(newTopic);
            adminClient.createTopics(newTopicList);
        } finally {
            close(adminClient);
        }
    }

    private void close(AdminClient adminClient) {
        adminClient.close();

    }


    public void deleteTopic(String... topics) {
        AdminClient adminClient = null;
        try {
            adminClient = KafkaAdminClient.create(properties);
            adminClient.deleteTopics(Arrays.asList(topics));
        } finally {
            close(adminClient);
        }
    }

    public Collection<String> listAllTopic() {
        AdminClient adminClient = null;
        try {
            adminClient = KafkaAdminClient.create(properties);
            ListTopicsResult topicsResult = adminClient.listTopics();
            KafkaFuture<Set<String>> names = topicsResult.names();
            Set<String> strings = names.get();
            strings.forEach(k -> System.out.println(k));
            return strings;
        } catch (Throwable e) {
            e.printStackTrace();

        } finally {
            close(adminClient);
        }
        return null;

    }


    public void getTopic(String... topics) {
        AdminClient adminClient = null;
        try {
            adminClient = KafkaAdminClient.create(properties);


            DescribeTopicsResult describeTopics = adminClient.describeTopics(Arrays.asList(topics));

            Collection<KafkaFuture<TopicDescription>> values = describeTopics.values().values();

            if (values.isEmpty()) {
                System.out.println("找不到描述信息");
            } else {
                for (KafkaFuture<TopicDescription> value : values) {
                    System.out.println(value);
                }
            }
        } finally {
            close(adminClient);
        }
    }
}