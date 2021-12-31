package cn.fightingguys.kaiheila.entity.kmarkdown;

public class KMarkdownBuilder {
  KMarkdown kMarkdown = new KMarkdown();

  public KMarkdownBuilder() {}

  public static KMarkdownBuilder create() { return new KMarkdownBuilder(); }

  public KMarkdownBuilder append(KMarkdown component) {
    kMarkdown.value += component.value;
    return this;
  }
}
