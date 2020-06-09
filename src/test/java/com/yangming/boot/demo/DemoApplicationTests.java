package com.yangming.boot.demo;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.*;
import com.yangming.boot.demo.system.model.ElaUser;
import com.yangming.boot.demo.system.util.RabbitMQUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    void contextLoads() {

    }

    /**
     * 测试索引的创建 Request
     */
    @Test
    void testCreateIndex() throws IOException {
        //创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("yangming_index");
        //执行请求
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

        System.out.println(createIndexResponse);

    }

    /**
     * 只能判断存不存在判断索引是否存在
     */
    @Test
    void testExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("yangming_index");
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);

        System.out.println(exists);
    }

    @Test
    void testProduceUrl() throws IOException{

        String start = "2020-06-08T20:37:18Z";

        String end = "2020-06-09T01:37:18Z";

        String url = "https://www.okex.com/api/swap/v3/instruments/BTC-USDT-SWAP/candles?start="+start+"&end="+end;

        System.out.println(new URL(url));
    }

    /**
     * 测试删除索引
     */
    @Test
    void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("yangming_index");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);

        System.out.println(delete);
    }

    /**
     * 添加文档
     *
     * @throws Exception
     */
    @Test
    void testAddDocument() throws IOException {
        ElaUser user = new ElaUser("杨明", 23);
        //创建请求
        IndexRequest yangming_index = new IndexRequest("yangming_index");

        //规则 put

        yangming_index.id("1");
        yangming_index.timeout(TimeValue.timeValueSeconds(1));
        yangming_index.timeout("1s");

        //将我们的请求转为json
        yangming_index.source(JSON.toJSONString(user), XContentType.JSON);

        //客户端发送请求

        IndexResponse index = restHighLevelClient.index(yangming_index, RequestOptions.DEFAULT);

        System.out.println(index.toString());
        System.out.println(index.status());
    }

    /**
     * 获取文档,判断是否存在
     */
    @Test
    void testIsExist() throws IOException {
        GetRequest request = new GetRequest("yangming_index", "1");
        //不获取返回的 _source的上下文
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");

        boolean exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);

        System.out.println(exists);
    }

    /**
     * 获取文档
     */
    @Test
    void testGetDocument() throws IOException {
        GetRequest request = new GetRequest("yangming_index", "1");
        GetResponse documentFields = restHighLevelClient.get(request, RequestOptions.DEFAULT);

        System.out.println(documentFields.getSourceAsString());

    }


    /**
     * 更新文档
     */
    @Test
    void testUpdateDocument() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("yangming_index","1");
        updateRequest.timeout("1s");

        ElaUser user = new ElaUser("yangming",30);

        updateRequest.doc(JSON.toJSONString(user),XContentType.JSON);

        UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

        System.out.println(update.status());

    }

    /**
     * 删除文档
     */
    @Test
    void testDeleteDocument() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("yangming_index", "1");

        deleteRequest.timeout("1s");

        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);

        System.out.println(deleteResponse.status());
    }


    /**
     * 批量插入数据
     */
    @Test
    void testBulkRequest() throws IOException{
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("1s");

        ArrayList<ElaUser> userArrayList = new ArrayList<>();

        userArrayList.add(new ElaUser("yangming1",102));
        userArrayList.add(new ElaUser("yangming2",122));
        userArrayList.add(new ElaUser("yangming3",122));
        userArrayList.add(new ElaUser("yangming4",126));
        userArrayList.add(new ElaUser("yangming5",125));
        userArrayList.add(new ElaUser("yangming6",127));
        userArrayList.add(new ElaUser("yangming7",129));

        for (int i = 0;i< userArrayList.size();i++){

            //批量删除或者是批量的更新 就是在这里更改api即可
            bulkRequest.add(new IndexRequest("yangming_index").id(""+i+1)
            .source(JSON.toJSONString(userArrayList.get(i)),XContentType.JSON));
        }

        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

        System.out.println(bulkResponse.status());
    }

    @Test
    void testSearch()throws IOException{
        SearchRequest searchRequest = new SearchRequest("yangming_index");

        //构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //查询条件，我们可以使用QueryBuilders 工具来实现
        //QueryBuilders.termQuery 精确查询
        //QueryBuilders.matchAllQuery
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name","yangming");

//        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        sourceBuilder.query(termQueryBuilder);

        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        System.out.println(JSON.toJSONString(searchResponse.getHits()));
        System.out.println("======================================");
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    /**
     * rabbitmq  hello world模式 发送消息
     * @throws IOException
     * @throws TimeoutException
     */
    @Test
    void testSendMessage()throws IOException, TimeoutException {
        //创建连接mq的连接工厂对象
        Connection connection = RabbitMQUtils.getConnection();

        Channel channel = connection.createChannel();

        //通道绑定 消息队列
        /**
         * 1.队列名称  队列不存在 自动创建
         * 2.用来定义队列是否要持久化 true 持久化  false 不持久化
         * 3.exclusive 是都独占队列
         * 4.是否在消费完成后  自动删除队列
         * 5.附加参数
         */
        channel.queueDeclare("aa",true,false,false,null);

        //发布消息
        /**
         * 1.交换机名称
         * 2.队列名称
         * 3.传递消息额外设置  可以在 此处 使消息持久化
         * 4.传递消息内容
         */

        channel.basicPublish("","aa",MessageProperties.PERSISTENT_TEXT_PLAIN,"hello rabbitmq".getBytes());

        channel.close();
        connection.close();
    }

    @Test
    void testCustomerMessage() throws IOException,TimeoutException{
        //创建连接mq的连接工厂对象
        Connection connection = RabbitMQUtils.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare("aa",true,false,false,null);

        /**
         * 1.消费哪个队列消息 队列名称
         * 2.开始消息的自动确认机制
         * 3.消费时的回调接口
         */
        channel.basicConsume("hello",true,new DefaultConsumer(channel){

            @Override  //最后一个参数：消息队列中取出的消息
            public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties,byte[] body)throws IOException{
                System.out.println("new String(body)"+new String(body));
            }
        });

        channel.close();
        connection.close();
    }
}
