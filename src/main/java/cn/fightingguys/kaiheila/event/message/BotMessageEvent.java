package cn.fightingguys.kaiheila.event.message;

import cn.fightingguys.kaiheila.RabbitImpl;
import cn.fightingguys.kaiheila.core.action.Operation;
import cn.fightingguys.kaiheila.event.AbstractEvent;
import cn.fightingguys.kaiheila.event.IEvent;
import com.fasterxml.jackson.databind.JsonNode;

public class BotMessageEvent extends AbstractEvent {

  public BotMessageEvent(RabbitImpl rabbit, JsonNode node) {
    super(rabbit, node);
  }

  @Override
  public Operation action() {
    return null;
  }

  @Override
  public IEvent handleSystemEvent(JsonNode body) {
    return this;
  }
}
