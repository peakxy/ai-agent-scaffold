package cn.bugstack.ai.api.dto;

import lombok.Data;

/**
 * @Author: Xuyifeng
 * @Description: 智能体配置响应对象
 * @Date: 2026/5/16 19:56
 */
@Data
public class AiAgentConfigResponseDTO {

    /**
     * 智能体Id
     */
    private String agentId;
    /**
     * 智能体名称
     */
    private String agentName;
    /**
     * 智能体描述
     */
    private String agentDesc;

}
