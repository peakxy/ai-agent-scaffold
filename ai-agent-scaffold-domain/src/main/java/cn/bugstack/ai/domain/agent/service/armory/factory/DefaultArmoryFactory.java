package cn.bugstack.ai.domain.agent.service.armory.factory;

import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import cn.bugstack.ai.domain.agent.model.valobj.properties.AiAgentAutoConfigProperties;
import cn.bugstack.ai.domain.agent.service.armory.node.workflow.SequentialAgentNode;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.SequentialAgent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: Xuyifeng
 * @Description:
 * @Date: 2026/5/15 15:38
 */

@Service
public class DefaultArmoryFactory {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DynamicContext {

        /**
         * llm api
         */
        private OpenAiApi openAiApi;

        /**
         * llm model
         */
        private ChatModel chatModel;

        /**
         * 暂时当作最后一个智能体节点
         */
        private SequentialAgent sequentialAgent;

        /**
         * 智能体配置组
         */
        private Map<String, BaseAgent> agentGroup = new HashMap<>();

        private List<AiAgentConfigTableVO.Module.AgentWorkflow> agentWorkflows = new ArrayList<>();

        private Map<String, Object> dataObject = new HashMap<>();

        public <T> void setValue(String key, T value) {
            dataObject.put(key, value);
        }

        public <T> T getValue(String key) {
            return (T) dataObject.get(key);
        }

        public List<BaseAgent> queryAgentList(List<String> agentNames) {
            if (agentNames == null || agentNames.isEmpty() || agentGroup == null) {
                return Collections.emptyList();
            }
            List<BaseAgent> agents = new ArrayList<>();
            for (String name : agentNames) {
                BaseAgent baseAgent = agentGroup.get(name);
                if (null != baseAgent) {
                    agents.add(baseAgent);
                }
            }

            return agents;
        }

    }

}
