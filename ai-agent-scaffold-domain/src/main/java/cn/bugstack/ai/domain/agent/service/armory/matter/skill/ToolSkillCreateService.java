package cn.bugstack.ai.domain.agent.service.armory.matter.skill;

import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.springframework.ai.tool.ToolCallback;

/**
 * @Author: Xuyifeng
 * @Description:
 * @Date: 2026/5/16 21:18
 */

public interface ToolSkillCreateService {

    ToolCallback[] buildToolCallback(AiAgentConfigTableVO.Module.ChatModel.ToolSkill toolSkill) throws Exception;

}
