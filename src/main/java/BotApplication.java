import cn.fightingguys.kaiheila.*;
import cn.fightingguys.kaiheila.core.action.InviteDuration;
import cn.fightingguys.kaiheila.core.action.InviteTimes;
import cn.fightingguys.kaiheila.core.action.Operation;
import cn.fightingguys.kaiheila.entity.cardmessage.CardMessage;
import cn.fightingguys.kaiheila.entity.cardmessage.CardMessageBuilder;
import cn.fightingguys.kaiheila.event.message.TextMessageEvent;
import cn.fightingguys.kaiheila.hook.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 用户机器人应用
 */
public class BotApplication {
    public static void main(String[] args) {
        Logger Log = LoggerFactory.getLogger(BotApplication.class);
        String apiToken = "1/MTA1OTc=/xdFVXmTl2I6FvdsYr/QdOQ==";

        Log.info("开始启动机器人...");
        Rabbit rabbit = RabbitBuilder.builder()
                .createDefault(apiToken) // 使用默认配置构建 Rabbit 实例
                .build(); // 创建实例
        // 添加事件处理器
        rabbit.addEventListener(new UserEventHandler());

        // 登录实例
        Log.info("开始登录...");
        if (rabbit.login()) {
            Log.info("登录成功");
            Log.info(rabbit.getGuild("7106853317977817").getName());
            try {
                TimeUnit.SECONDS.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                rabbit.shutdown();
            }
        } else {
            Log.error("登录失败");
        }
    }

    /**
     * 创建用户事件处理器
     */
    public static class UserEventHandler extends EventListener {
        Logger Log = LoggerFactory.getLogger(UserEventHandler.class);
        /**
         * 接收文本消息事件
         *
         * @param rabbit Rabbit 实例
         * @param event  文本消息事件内容
         */
        @Override
        public void onTextMessageEvent(Rabbit rabbit, TextMessageEvent event) {
            // 监听文本消息事件内容
            if (event.type == TextMessageEvent.Type.Group) Log.info("[{} | {}]{}",event.getChannel().getName(), event.getEventAuthorId().getUsername(), event.getEventContent());
            else Log.info("[{}]{}",event.getEventAuthorId().getUsername(), event.getEventContent());

            Operation.ChatOperation userOperation = event.action();
            userOperation.reply(CardMessageBuilder.start().append(
                CardMessage.create()
                    .add(CardMessage.Module.Header()
                        .setText(CardMessage.Element.PlainText.Text()
                            .setContent("欢迎使用 KaiHeiLa 微信机器人")
                        )
                    )
                )
            );
        }
    }
}