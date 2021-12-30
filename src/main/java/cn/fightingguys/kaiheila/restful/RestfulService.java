package cn.fightingguys.kaiheila.restful;

import cn.fightingguys.kaiheila.RabbitImpl;
import cn.fightingguys.kaiheila.cache.CacheManager;
import cn.fightingguys.kaiheila.client.http.HttpCall;
import cn.fightingguys.kaiheila.client.http.HttpHeaders;
import cn.fightingguys.kaiheila.core.RabbitObject;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class RestfulService extends RabbitObject {
  protected final Logger Log = LoggerFactory.getLogger(this.getClass());
  final HttpHeaders defaultHeaders = new HttpHeaders();
  public RestfulService(RabbitImpl rabbit) {
    super(rabbit);
    defaultHeaders.addHeader("Authorization", "Bot " + getRabbitImpl().getConfiguration().getApiConfigurer().getToken());
  }

  protected String getCompleteUrl(RestRoute.CompiledRoute route) {
    return getRabbitImpl().getConfiguration().getApiConfigurer().getBaseUrl() + route.getQueryStringCompleteRoute();
  }

  protected List<JsonNode> getRestJsonResponse(RestRoute.CompiledRoute compiledRoute, HttpCall call) throws InterruptedException {
    ArrayList<JsonNode> result = new ArrayList<>();
    JsonNode root = callRestApi(call);
    if (root == null) {
      Log.error("Not set the root");
      return null;
    }
    result.add(root);
    result.addAll(getRemainPageRestData(compiledRoute, getRestApiData(root)));
    return result;
  }

  private JsonNode getRestApiData(JsonNode node) {
    return node.get("data");
  }

  protected List<JsonNode> getRemainPageRestData(RestRoute.CompiledRoute compiledRoute, JsonNode data) throws InterruptedException {
    ArrayList<JsonNode> result = new ArrayList<>();
    RestPageable pageable = RestPageable.of(getRabbitImpl(), compiledRoute, data);
    while (pageable.hasNext()) {
      RestRoute.CompiledRoute nextRoute = pageable.next();
      HttpCall nextCall = HttpCall.createRequest(nextRoute.getMethod(), getCompleteUrl(nextRoute), this.defaultHeaders);
      JsonNode next = callRestApi(nextCall);
      if (next == null) {
        continue;
      }
      result.add(next);
    }
    return result;
  }

  protected boolean hasRestApiError(JsonNode root) {
    if (root == null) {
      return true;
    }
    if (!root.has("code")) {
      return true;
    }
    return root.get("code").asInt() != 0;
  }

  protected void reportRequestFailed(int retryCount, String url) throws InterruptedException {
    if (Log.isWarnEnabled()) {
      Log.warn("[数据同步] 获取数据失败，3秒后第{}次重试, [{}]", retryCount, url);
    }
    TimeUnit.SECONDS.sleep(3);
  }

  protected JsonNode callRestApi(HttpCall call) throws InterruptedException {
    JsonNode root = null;
    boolean callFailed;
    int callRetry = 0;
    HttpCall.Response response;
    do {
      try {
        response = getRabbitImpl().getHttpClient().execute(call);
        if (response.getCode() != 200) {
          reportRequestFailed(++callRetry, call.getRequest().getUrl());
          callFailed = true;
          continue;
        }
        root = getRabbitImpl().getJsonEngine().readTree(response.getResponseBody().getBuffer().array());
      } catch (IOException e) {
        reportRequestFailed(++callRetry, call.getRequest().getUrl());
        callFailed = true;
        continue;
      }
      callFailed = false;
    } while (callFailed && callRetry != 3);
    if (callFailed || hasRestApiError(root)) {
      return null;
    }
    return root;
  }
}
