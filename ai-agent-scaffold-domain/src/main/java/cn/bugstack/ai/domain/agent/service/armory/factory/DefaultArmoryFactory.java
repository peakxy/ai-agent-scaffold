package cn.bugstack.ai.domain.agent.service.armory.factory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

        private Map<String, Object> dataObject = new HashMap<>();

        public <T> void setValue(String key, T value) {
            dataObject.put(key, value);
        }

        public <T> T getValue(String key) {
            return (T) dataObject.get(key);
        }

    }

}
