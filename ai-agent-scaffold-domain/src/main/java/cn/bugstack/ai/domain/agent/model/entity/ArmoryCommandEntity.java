package cn.bugstack.ai.domain.agent.model.entity;

import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Xuyifeng
 * @Description:
 * @Date: 2026/5/15 15:36
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArmoryCommandEntity {

    private AiAgentConfigTableVO aiAgentConfigTableVO;

}
