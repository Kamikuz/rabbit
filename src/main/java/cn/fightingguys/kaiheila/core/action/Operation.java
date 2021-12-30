package cn.fightingguys.kaiheila.core.action;

import cn.fightingguys.kaiheila.RabbitImpl;
import cn.fightingguys.kaiheila.api.Channel;
import cn.fightingguys.kaiheila.api.User;
import cn.fightingguys.kaiheila.client.http.HttpCall;
import cn.fightingguys.kaiheila.client.http.HttpHeaders;
import cn.fightingguys.kaiheila.event.message.MessageExtra;
import cn.fightingguys.kaiheila.restful.RestRoute;
import cn.fightingguys.kaiheila.restful.RestfulService;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public abstract class Operation extends RestfulService {
  final HttpHeaders defaultHeaders = new HttpHeaders();

  public Operation(RabbitImpl rabbit) {
    super(rabbit);
  }

  public static class UserOperation extends Operation {
    protected final User user;

    public UserOperation(RabbitImpl rabbit, User user) {
      super(rabbit);
      this.user = user;
    }

    public void sendMessage(String message) {
      RestRoute.CompiledRoute sendDirectChatRoute = RestRoute.DirectMessage.SEND_DIRECT_MESSAGE.compile().withQueryParam("target_id", user.getId());
      HttpCall guildIdRequest = HttpCall.createRequest(sendDirectChatRoute.getMethod(), getCompleteUrl(sendDirectChatRoute), this.defaultHeaders);
      try{
        List<JsonNode> data = getRestJsonResponse(sendDirectChatRoute, guildIdRequest);
        if (data != null && data.size() > 0) {
          JsonNode jsonNode = data.get(0);
          if (jsonNode.get("code").asInt() != 0) {
            Log.error("Failed to send message to user: " + user.getFullName());
          }else
            Log.info("Successfully send message to user: " + user.getFullName());
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    public void reply(String message, String msgId) {
      RestRoute.CompiledRoute sendDirectChatRoute = RestRoute.DirectMessage.SEND_DIRECT_MESSAGE.compile()
          .withQueryParam("target_id", user.getId())
          .withQueryParam("content", message)
          .withQueryParam("quote", msgId);
      HttpCall guildIdRequest = HttpCall.createRequest(sendDirectChatRoute.getMethod(), getCompleteUrl(sendDirectChatRoute), this.defaultHeaders);
    }
  }

  public static class ServerOperation extends UserOperation {
    private final MessageExtra.ChannelMessageExtra server;

    public ServerOperation(RabbitImpl rabbit, User user, MessageExtra.ChannelMessageExtra server) {
      super(rabbit, user);
      this.server = server;
    }

    public void sendMessage(String message) {
      RestRoute.CompiledRoute sendDirectChatRoute = RestRoute.DirectMessage.SEND_DIRECT_MESSAGE.compile().withQueryParam("target_id", user.getId());
      HttpCall guildIdRequest = HttpCall.createRequest(sendDirectChatRoute.getMethod(), getCompleteUrl(sendDirectChatRoute), this.defaultHeaders);
      try{
        List<JsonNode> data = getRestJsonResponse(sendDirectChatRoute, guildIdRequest);
        if (data != null && data.size() > 0) {
          JsonNode jsonNode = data.get(0);
          if (jsonNode.get("code").asInt() != 0) {
            Log.error("Failed to send message to user: " + user.getFullName());
          }else
            Log.info("Successfully send message to user: " + user.getFullName());
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    public void reply(String message, String msgId) {
      RestRoute.CompiledRoute sendDirectChatRoute = RestRoute.DirectMessage.SEND_DIRECT_MESSAGE.compile()
          .withQueryParam("target_id", user.getId())
          .withQueryParam("content", message)
          .withQueryParam("quote", msgId);
      HttpCall guildIdRequest = HttpCall.createRequest(sendDirectChatRoute.getMethod(), getCompleteUrl(sendDirectChatRoute), this.defaultHeaders);
    }


  }

  public static class ChannelOperation extends ServerOperation {
    final Channel channel = null;
    public ChannelOperation(RabbitImpl rabbit, User user, MessageExtra.ChannelMessageExtra server) {
      super(rabbit, user, server);

    }
  }

}
