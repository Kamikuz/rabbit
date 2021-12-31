package cn.fightingguys.kaiheila.core.action;

import cn.fightingguys.kaiheila.RabbitImpl;
import cn.fightingguys.kaiheila.api.Channel;
import cn.fightingguys.kaiheila.api.Guild;
import cn.fightingguys.kaiheila.api.Role;
import cn.fightingguys.kaiheila.api.User;
import cn.fightingguys.kaiheila.client.http.HttpCall;
import cn.fightingguys.kaiheila.client.http.RequestBuilder;
import cn.fightingguys.kaiheila.entity.cardmessage.CardMessage;
import cn.fightingguys.kaiheila.entity.cardmessage.CardMessageBuilder;
import cn.fightingguys.kaiheila.entity.kmarkdown.KMarkdown;
import cn.fightingguys.kaiheila.event.message.TextMessageEvent;
import cn.fightingguys.kaiheila.restful.RestRoute;
import cn.fightingguys.kaiheila.restful.RestfulService;
import com.fasterxml.jackson.databind.JsonNode;

public abstract class Operation extends RestfulService {
  public Operation(RabbitImpl rabbit) {
    super(rabbit);
  }

  protected boolean handleResult(JsonNode res){
    return res != null && res.get("code").asInt() == 0;
  }

  public static class ChatOperation extends Operation {
    TextMessageEvent message;

    public ChatOperation(RabbitImpl rabbit, TextMessageEvent message) {
      super(rabbit);
      this.message = message;
    }

    public ChatOperation replyOnChannel(String content, boolean isTemp, boolean isReply) {
      Log.info("Reply on channel {}", message.getChannel().getName());
      if (message.type == TextMessageEvent.Type.Group) {
        HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
            .withData("channel_id", message.getChannel().getId())
            .withData("nonce", "bot-message")
            .withData("content", content)
            .withData("quote", isReply ? message.messageID : null)
            .withData("temp_target_id", isTemp ? message.getEventAuthorId().getId() : null)
            .build();
        try{
          JsonNode data = getRestActionJsonResponse(req);
          if (handleResult(data)) Log.info("Successfully reply message to channel {} @ {}", message.getChannel().getName(), message.getEventAuthorId().getFullName());
          else Log.error("Failed reply message to channel {} @ {}, reason: {}", message.getChannel().getName(), message.getEventAuthorId().getFullName(), data.get("message").asText());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      return this;
    }

    public ChatOperation replyOnPerson(String content, boolean isReply) {
      Log.info("Reply on person {}", message.getEventAuthorId().getFullName());
      if (message.type == TextMessageEvent.Type.Person) {
        HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.DirectMessage.SEND_DIRECT_MESSAGE)
            .withData("target_id", message.getEventAuthorId().getId())
            .withData("nonce", "bot-message")
            .withData("content", content)
            .withData("quote", isReply ? message.messageID : null)
            .build();
        try{
          JsonNode data = getRestActionJsonResponse(req);
          if (handleResult(data)) Log.info("Successfully reply message to {}", message.asPrivateMessageEvent().getExtra().getAuthor().getFullName());
          else Log.error("Failed to reply message to {}, reason: {}", message.asPrivateMessageEvent().getExtra().getAuthor().getFullName(), data.get("message").asText());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      return this;
    }

    public ChatOperation replyOnChannel(CardMessageBuilder content, boolean isTemp, boolean isReply) {
      Log.info("Reply on channel {}", message.getChannel().getName());
      if (message.type == TextMessageEvent.Type.Group) {
        HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
            .withData("channel_id", message.getChannel().getId())
            .withData("nonce", "bot-message")
            .withData("type", 10)
            .withData("content", content)
            .withData("quote", isReply ? message.messageID : null)
            .withData("temp_target_id", isTemp ? message.getEventAuthorId().getId() : null)
            .build();
        try{
          JsonNode data = getRestActionJsonResponse(req);
          if (handleResult(data)) Log.info("Successfully reply message to channel {} @ {}", message.getChannel().getName(), message.getEventAuthorId().getFullName());
          else Log.error("Failed reply message to channel {} @ {}, reason: {}", message.getChannel().getName(), message.getEventAuthorId().getFullName(), data.get("message").asText());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      return this;
    }

    public ChatOperation replyOnPerson(CardMessageBuilder content, boolean isReply) {
      Log.info("Reply on person {}", message.getEventAuthorId().getFullName());
      if (message.type == TextMessageEvent.Type.Person) {
        HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.DirectMessage.SEND_DIRECT_MESSAGE)
            .withData("target_id", message.getEventAuthorId().getId())
            .withData("nonce", "bot-message")
            .withData("type", 10)
            .withData("content", content)
            .withData("quote", isReply ? message.messageID : null)
            .build();
        try{
          JsonNode data = getRestActionJsonResponse(req);
          if (handleResult(data)) Log.info("Successfully reply message to {}", message.asPrivateMessageEvent().getExtra().getAuthor().getFullName());
          else Log.error("Failed to reply message to {}, reason: {}", message.asPrivateMessageEvent().getExtra().getAuthor().getFullName(), data.get("message").asText());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      return this;
    }

    public ChatOperation replyOnChannel(KMarkdown kMarkdown, boolean isTemp, boolean isReply) {
      Log.info("Reply on channel {}", message.getChannel().getName());
      if (message.type == TextMessageEvent.Type.Group) {
        HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
            .withData("channel_id", message.getChannel().getId())
            .withData("nonce", "bot-message")
            .withData("type", 9)
            .withData("content", kMarkdown)
            .withData("quote", isReply ? message.messageID : null)
            .withData("temp_target_id", isTemp ? message.getEventAuthorId().getId() : null)
            .build();
        try{
          JsonNode data = getRestActionJsonResponse(req);
          if (handleResult(data)) Log.info("Successfully reply message to channel {} @ {}", message.getChannel().getName(), message.getEventAuthorId().getFullName());
          else Log.error("Failed reply message to channel {} @ {}, reason: {}", message.getChannel().getName(), message.getEventAuthorId().getFullName(), data.get("message").asText());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      return this;
    }

    public ChatOperation replyOnPerson(KMarkdown kMarkdown, boolean isReply) {
      Log.info("Reply on person {}", message.getEventAuthorId().getFullName());
      if (message.type == TextMessageEvent.Type.Person) {
        HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.DirectMessage.SEND_DIRECT_MESSAGE)
            .withData("target_id", message.getEventAuthorId().getId())
            .withData("nonce", "bot-message")
            .withData("type", 9)
            .withData("content", kMarkdown.value)
            .withData("quote", isReply ? message.messageID : null)
            .build();
        try{
          JsonNode data = getRestActionJsonResponse(req);
          if (handleResult(data)) Log.info("Successfully reply message to {}", message.asPrivateMessageEvent().getExtra().getAuthor().getFullName());
          else Log.error("Failed to reply message to {}, reason: {}", message.asPrivateMessageEvent().getExtra().getAuthor().getFullName(), data.get("message").asText());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      return this;
    }

    public ChatOperation reply(String content) {
      if (message.type == TextMessageEvent.Type.Group) {
        return replyOnChannel(content, false, false);
      }
      return replyOnPerson(content, false);
    }

    public ChatOperation reply(CardMessageBuilder cardMessage) {
      if (message.type == TextMessageEvent.Type.Group) {
        return replyOnChannel(cardMessage, false, false);
      }
      return replyOnPerson(cardMessage, false);
    }

    public ChatOperation reply(KMarkdown kMarkdown) {
      if (message.type == TextMessageEvent.Type.Group) {
        return replyOnChannel(kMarkdown, false, false);
      }
      return replyOnPerson(kMarkdown, false);
    }

    public UserOperation getUser() {
      return message.type == TextMessageEvent.Type.Group ? new UserOperation(getRabbitImpl(), message.getEventAuthorId(), message.getChannel()) : new UserOperation(getRabbitImpl(), message.getEventAuthorId());
    }

    public ChannelOperation getChannel() {
      if (message.type == TextMessageEvent.Type.Person) return null; //todo: throw exception
      return new ChannelOperation(getRabbitImpl(), message.getChannel());
    }

    public ServerOperation getGuild() {
      if (message.type == TextMessageEvent.Type.Person) return null; //todo: throw exception
      return new ServerOperation(getRabbitImpl(), message.getChannel().getGuild());
    }
  }

  public static class UserOperation extends Operation {
    protected final User user;
    public final boolean isPersonal;
    protected final Channel channel;

    public UserOperation(RabbitImpl rabbit, User user) {
      super(rabbit);
      this.user = user;
      this.isPersonal = true;
      channel = null;
    }

    public UserOperation(RabbitImpl rabbit, User user, Channel channel) {
      super(rabbit);
      this.user = user;
      this.isPersonal = false;
      this.channel = channel;
    }

    public UserOperation sendMessage(String message) {
      HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.DirectMessage.SEND_DIRECT_MESSAGE)
          .withData("target_id", user.getId())
          .withData("nonce", "bot-message")
          .withData("content", message)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        Log.debug("{}", data);
        if (handleResult(data)) Log.info("Successfully send message to user: " + user.getFullName());
        else Log.error("Failed to send message to user: {}. Reason: {}",user.getFullName() ,data.get("message").asText());
      } catch (InterruptedException e) {
        e.printStackTrace();
        Log.error("Failed to send message to user: {}",user.getFullName());
      }
      return this;
    }

    public UserOperation reply(String message, String msgId) {
      HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.DirectMessage.SEND_DIRECT_MESSAGE)
          .withData("target_id", user.getId())
          .withData("nonce", "bot-message")
          .withData("content", message)
          .withData("quote", msgId)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) Log.info("Successfully reply message to user: " + user.getFullName());
        else Log.error("Failed to reply message to user: {}. Reason: {}",user.getFullName() ,data.get("message").asText());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return this;
    }

    public UserOperation updateIntimacy(int value) {
      RestRoute.CompiledRoute route = RestRoute.Intimacy.UPDATE_USER_INTIMACY.compile()
          .withQueryParam("user_id", user.getId())
          .withQueryParam("score", value);
      HttpCall req = HttpCall.createRequest(route.getMethod(), getCompleteUrl(route), this.defaultHeaders);
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) Log.info("Successfully update user[{}] intimacy!",user.getFullName());
        else Log.error("Failed to update user[{}] intimacy! Reason: {}",user.getFullName() ,data.get("message").asText());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return this;
    }

    public int getUserIntimacy(){
      HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.Intimacy.INTIMACY_LIST)
          .withQuery("user_id", user.getId())
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) {
          return data.get("score").asInt();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return -1;
    }

    public ServerOperation getServer(){
      if (isPersonal) return null; //todo throw exception
      return new ServerOperation(getRabbitImpl(), channel.getGuild());
    }

    public ChannelOperation getChannel(){
      if (!isPersonal) return null; //todo throw exception
      return new ChannelOperation(getRabbitImpl(), channel);
    }
  }

  public static class ServerOperation extends Operation {
    private final Guild server;

    public ServerOperation(RabbitImpl rabbit, Guild server) {
      super(rabbit);
      this.server = server;
    }

    public ChannelOperation getChannel(String id){
      return new ChannelOperation(getRabbitImpl(), getRabbitImpl().getCacheManager().getChannelCache().getElementById(id));
    }

    public UserOperation getMember(String id){
      return new UserOperation(getRabbitImpl(), getRabbitImpl().getCacheManager().getUserCache().getElementById(id));
    }

    public RoleOperation getMember(int id){
      return new RoleOperation(getRabbitImpl(), server, getRabbitImpl().getCacheManager().getRoleCache().getElementById(id));
    }

    public String createServerInvite(InviteDuration duration, InviteTimes times){
      HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.Invite.CREATE_INVITE)
          .withData("guild_id", server.getId())
          .withData("duration", duration)
          .withData("setting_times", times)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) {
          return data.get("data").get("url").asText();
        }else{
          Log.error("Failed to create server invite! Reason: {}",data.get("message").asText());
        }
      } catch (InterruptedException e) {
        Log.error("Failed to create server invite! Reason: {}", e.getMessage());
        e.printStackTrace();
      }
      return null;
    }
  }

  public static class RoleOperation extends Operation {
    private final Role role;
    private final Guild guild;

    public RoleOperation(RabbitImpl rabbit, Guild guild, Role role) {
      super(rabbit);
      this.role = role;
      this.guild = guild;
    }

    public void grantUser(String uid){
      HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.GuildRole.GRANT_GUILD_ROLE)
          .withData("guild_id", guild.getId())
          .withData("user_id", uid)
          .withData("role_id", String.valueOf(role.getId()))
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) Log.info("Successfully grant role '{}' to user", role.getName());
        else Log.error("Failed to grant role '{}' to user. Reason: {}", role.getName() ,data.get("message").asText());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    public void revokeUser(String uid){
      HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.GuildRole.REVOKE_GUILD_ROLE)
          .withData("guild_id", guild.getId())
          .withData("user_id", uid)
          .withData("role_id", String.valueOf(role.getId()))
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) Log.info("Successfully revoke role '{}' to user", role.getName());
        else Log.error("Failed to revoke role '{}' to user. Reason: {}", role.getName() ,data.get("message").asText());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static class ChannelOperation extends Operation {
    final Channel channel;

    public ChannelOperation(RabbitImpl rabbit, Channel channel) {
      super(rabbit);
      this.channel = channel;
    }

    public ChannelOperation broadcastMessage(String message) {
      HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
          .withData("target_id", channel.getId())
          .withData("nonce", "bot-message")
          .withData("content", message)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) Log.info("Successfully broadcast message to channel {}", channel.getName());
        else Log.error("Failed to broadcast message to channel {}, reason: {}", channel.getName(), data.get("message").asText());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return this;
    }

    public ChannelOperation sendTempMessage(String message, String uid){
      HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
          .withData("target_id", channel.getId())
          .withData("nonce", "bot-message")
          .withData("content", message)
          .withData("temp_target_id", uid)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) Log.info("Successfully send temp message to channel {}", channel.getName());
        else Log.error("Failed to send temp message to channel {}, reason: {}", channel.getName(), data.get("message").asText());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return this;
    }

    public ChannelOperation broadcastMessage(CardMessageBuilder message) {
      HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
          .withData("target_id", channel.getId())
          .withData("nonce", "bot-message")
          .withData("type", 10)
          .withData("content", message)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) Log.info("Successfully broadcast message to channel {}", channel.getName());
        else Log.error("Failed to broadcast message to channel {}, reason: {}", channel.getName(), data.get("message").asText());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return this;
    }

    public ChannelOperation sendTempMessage(CardMessageBuilder message, String uid){
      HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
          .withData("target_id", channel.getId())
          .withData("nonce", "bot-message")
          .withData("type", 10)
          .withData("content", message)
          .withData("temp_target_id", uid)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) Log.info("Successfully send temp message to channel {}", channel.getName());
        else Log.error("Failed to send temp message to channel {}, reason: {}", channel.getName(), data.get("message").asText());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return this;
    }

    public ChannelOperation broadcastMessage(KMarkdown message) {
      HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
          .withData("target_id", channel.getId())
          .withData("nonce", "bot-message")
          .withData("type", 9)
          .withData("content", message)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) Log.info("Successfully broadcast message to channel {}", channel.getName());
        else Log.error("Failed to broadcast message to channel {}, reason: {}", channel.getName(), data.get("message").asText());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return this;
    }

    public ChannelOperation sendTempMessage(KMarkdown message, String uid){
      HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
          .withData("target_id", channel.getId())
          .withData("nonce", "bot-message")
          .withData("type", 9)
          .withData("content", message)
          .withData("temp_target_id", uid)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) Log.info("Successfully send temp message to channel {}", channel.getName());
        else Log.error("Failed to send temp message to channel {}, reason: {}", channel.getName(), data.get("message").asText());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return this;
    }

    public String createChannelInvite(InviteDuration duration, InviteTimes times){
      HttpCall req = RequestBuilder.create(getRabbitImpl(), RestRoute.Invite.CREATE_INVITE)
          .withData("channel_id", channel.getId())
          .withData("duration", duration)
          .withData("setting_times", times)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) {
          return data.get("url").asText();
        }else{
          Log.error("Failed to create server invite! Reason: {}",data.get("message").asText());
        }
      } catch (InterruptedException e) {
        Log.error("Failed to create server invite! Reason: {}", e.getMessage());
        e.printStackTrace();
      }
      return null;
    }

    public ServerOperation getServer(){
      return new ServerOperation(getRabbitImpl(), channel.getGuild());
    }
  }
}
