/*
 * (C) 2007-2012 Alibaba Group Holding Limited.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Authors:
 *   wuhua <wq163@163.com> , boyan <killme2008@gmail.com>
 */
package test.mq;

import java.util.concurrent.Executor;

import com.taobao.metamorphosis.Message;
import com.taobao.metamorphosis.client.MetaClientConfig;
import com.taobao.metamorphosis.client.consumer.ConsumerConfig;
import com.taobao.metamorphosis.client.consumer.MessageConsumer;
import com.taobao.metamorphosis.client.consumer.MessageListener;
import com.taobao.metamorphosis.client.extension.BroadcastMessageSessionFactory;
import com.taobao.metamorphosis.client.extension.MetaBroadcastMessageSessionFactory;
import com.taobao.metamorphosis.utils.ZkUtils.ZKConfig;

public class BroadcastAsyncConsumer {
    public static void main(final String[] args) throws Exception {
    	final MetaClientConfig metaClientConfig = new MetaClientConfig();
		final ZKConfig zkConfig = new ZKConfig();
		// 设置zookeeper地址
		zkConfig.zkConnect = "localhost:2181";
		metaClientConfig.setZkConfig(zkConfig);
        final BroadcastMessageSessionFactory sessionFactory = new MetaBroadcastMessageSessionFactory(metaClientConfig);

        // subscribed topic
        final String topic = "platform.guys";
        // consumer group
        final String group = "meta-example";
        final MessageConsumer consumer = sessionFactory.createBroadcastConsumer(new ConsumerConfig(group));
        // subscribe topic
        consumer.subscribe(topic, 1024 * 1024, new MessageListener() {

            @Override
            public void recieveMessages(final Message message) {
                System.out.println("Receive message " + new String(message.getData()));
            }


            @Override
            public Executor getExecutor() {
                // Thread pool to process messages,maybe null.
                return null;
            }
        });
        // complete subscribe
        consumer.completeSubscribe();

    }
}