package com.gptchat.turbobot.service.chat;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.gptchat.turbobot.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ChatWithXiaoDa {

    private static final Logger logger = LoggerFactory.getLogger(ChatWithXiaoDa.class);

    private static final String error_msg = "嗯？我是一个人工智能模型，对于这个问题我无法给出准确的答案";

    /**
     *
     */
    @Value("${chatgpt.api:}")
    private String api;

    /**
     *
     */
    @Value("${chatgpt.skey:}")
    private String skey;

    /**
     *
     */
    @Value("${chatgpt.model:}")
    private String model;

    /**对话api
     * @param input 输入
     * @return 输出
     */
    public String getResponse(String input) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(api);
            // 创建请求内容
            JSONObject requestBody = new JSONObject();
            requestBody.putOpt("model", model);
            List<JSONObject> list = new ArrayList<>();
            list.add(new JSONObject().putOpt("role", "user").putOpt("content", input));
            requestBody.putOpt("messages", list);
            String json = requestBody.toString();
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            httpPost.setHeader("Authorization", EncryptUtil.decrypt(skey));
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
            if (JSONUtil.parseObj(resultString).containsKey("error")) {
                logger.error("resp--------------/n" + resultString);
                return error_msg;
            }
            logger.debug("resp--------------/n" + resultString);
            resultString = JSONUtil.parseObj(JSONUtil.parseObj(JSONUtil.parseArray(JSONUtil.parseObj(resultString)
                    .get("choices"))
                    .get(0))
                    .get("message"))
                    .get("content").toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return resultString;
    }

}
