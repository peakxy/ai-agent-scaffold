package cn.bugstack.ai.api.dto;

import lombok.Data;

/**
 * @Author: Xuyifeng
 * @Description:
 * @Date: 2026/5/16 19:58
 */
@Data
public class ChatRequestDTO {

    private String agentId;
    private String userId;
    private String sessionId;
    private String message;

}
