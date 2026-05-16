package cn.bugstack.ai.test.api.tool.skill;

import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.agent.tools.SkillsTool;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;

/**
 * @Author: Xuyifeng
 * @Description:
 * @Date: 2026/5/16 21:03
 */

@Slf4j
public class SpringAiToolTest {

    public static void main(String[] args) {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl("https://api-s.zwenooo.link/")
                .apiKey("sk-6su8zF5T3JagKwX44nwLGzhnK6UwtUIbUeB6atnZ1Qtp3gZk")
                .completionsPath("v1/chat/completions")
                .embeddingsPath("v1/embeddings")
                .build();

        // https://github.com/spring-ai-community/spring-ai-agent-utils
        ToolCallback toolCallback01 = SkillsTool.builder()
                .addSkillsDirectory("/Users/xuyifeng/1024/project/ai-agent-scaffold/ai-agent-scaffold-app/src/main/resources/agent/skills")
                .build();

        ToolCallback toolCallback02 = SkillsTool.builder()
                .addSkillsResource(new ClassPathResource("agent/skills"))
                .build();

        ChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-5.5")
                        .toolCallbacks(new ArrayList<>(){{
                            add(toolCallback02);
                        }})
                        .build())
                .build();

        String call = chatModel.call("你可以做什么？");

        log.info("测试结果:{}", call);
    }

}

