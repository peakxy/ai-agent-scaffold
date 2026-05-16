package cn.bugstack.ai.domain.agent.service.armory.matter.skill.impl;

import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import cn.bugstack.ai.domain.agent.service.armory.matter.skill.ToolSkillCreateService;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.agent.tools.SkillsTool;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Xuyifeng
 * @Description:
 * @Date: 2026/5/16 21:19
 */
@Slf4j
@Service
public class DefaultToolSkillCreateService implements ToolSkillCreateService {

    @Override
    public ToolCallback[] buildToolCallback(AiAgentConfigTableVO.Module.ChatModel.ToolSkill toolSkill) throws Exception {
        String type = toolSkill.getType();
        String path = toolSkill.getPath();

        List<ToolCallback> toolCallbackList = new ArrayList<>();

        if ("directory".equalsIgnoreCase(type)) {
            ToolCallback toolCallback = SkillsTool.builder()
                    .addSkillsDirectory(path)
                    .build();
            toolCallbackList.add(toolCallback);
        }

        if ("resource".equalsIgnoreCase(type)) {
            ToolCallback toolCallback = SkillsTool.builder()
                    .addSkillsResource(new ClassPathResource(path))
                    .build();
            toolCallbackList.add(toolCallback);
        }

        return toolCallbackList.toArray(new ToolCallback[0]);
    }

}
