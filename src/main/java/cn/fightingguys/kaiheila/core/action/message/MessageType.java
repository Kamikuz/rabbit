package cn.fightingguys.kaiheila.core.action.message;

public enum MessageType {
  TEXT(1),
  KMARKDOWN(9),
  CARD(10);
  public final int value;

  MessageType(int value) {
    this.value = value;
  }
}