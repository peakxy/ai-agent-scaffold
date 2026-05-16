package cn.bugstack.ai.domain.agent.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Xuyifeng
 * @Description: 对话命令实体对象
 * @Date: 2026/5/16 19:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatCommandEntity {

    private String agentId;

    private String userId;

    private String sessionId;

    private List<Content.Text> texts;
    private List<Content.File> files;
    private List<Content.InlineData> inlineData;

    @Data
    public static class Content {

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Text {
            private String message;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class File {
            private String fileUri;
            private String mimeType;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class InlineData {
            private byte[] bytes;
            private String mimeType;
        }
    }

    public ChatCommandEntity buildSessionCommand(String agentId, String userId) {
        ChatCommandEntity chatCommandEntity = new ChatCommandEntity();
        chatCommandEntity.setAgentId(agentId);
        chatCommandEntity.setUserId(userId);
        return chatCommandEntity;
    }

    public ChatCommandEntity buildChatCommand(String agentId, String userId, String message) {
        ChatCommandEntity chatCommandEntity = new ChatCommandEntity();
        chatCommandEntity.setAgentId(agentId);
        chatCommandEntity.setUserId(userId);

        List<Content.Text> texts = new ArrayList<>();
        texts.add(new Content.Text(message));

        chatCommandEntity.setTexts(texts);

        return chatCommandEntity;
    }

}
