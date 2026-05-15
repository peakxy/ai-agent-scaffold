package cn.bugstack.ai.domain.agent.service;

import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVO;

import java.util.List;

/**
 * @Author: Xuyifeng
 * @Description:
 * @Date: 2026/5/15 15:26
 */

public interface IArmoryService {

    void acceptArmoryAgent(List<AiAgentConfigTableVO> tables) throws Exception;

}
