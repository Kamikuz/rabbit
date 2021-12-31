package cn.fightingguys.kaiheila.client.http;

import cn.fightingguys.kaiheila.RabbitImpl;
import cn.fightingguys.kaiheila.core.RabbitObject;
import cn.fightingguys.kaiheila.restful.RestRoute;
import okhttp3.FormBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestBuilder extends RabbitObject {
  protected final Logger Log = LoggerFactory.getLogger(this.getClass());
  RestRoute.CompiledRoute route;
  FormBody.Builder formBuilder;
  protected final HttpHeaders headers = new HttpHeaders();

  private RequestBuilder(RabbitImpl rabbit, RestRoute.CompiledRoute route) {
    super(rabbit);
    this.route = route;
    if (getRabbitImpl().getConfiguration().getApiConfigurer().getToken().isEmpty()){
      Log.warn("Token is empty");
    }
    headers.addHeader("Authorization", "Bot " + getRabbitImpl().getConfiguration().getApiConfigurer().getToken());
  }

  public static RequestBuilder create(RabbitImpl rabbit, RestRoute route) {
    return new RequestBuilder(rabbit, route.compile());
  }

  public RequestBuilder withData(String key, Object value) {
    if (value == null) return this;
    if (formBuilder == null) {
      formBuilder = new FormBody.Builder();
    }
    formBuilder.add(key, String.valueOf(value));
    return this;
  }

  public RequestBuilder withQuery(String key, String value) {
    route.withQueryParam(key, value);
    return this;
  }

  public HttpCall build() {
    return formBuilder != null ? HttpCall.createRequest(getRabbitImpl().getConfiguration().getApiConfigurer().getBaseUrl() + route.getQueryStringCompleteRoute(), headers, formBuilder.build()) :
            HttpCall.createRequest(route.getMethod(),getRabbitImpl().getConfiguration().getApiConfigurer().getBaseUrl() + route.getQueryStringCompleteRoute(), headers);
  }
}
