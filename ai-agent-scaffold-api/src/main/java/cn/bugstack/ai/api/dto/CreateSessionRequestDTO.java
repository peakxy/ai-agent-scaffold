package cn.bugstack.ai.api.dto;

import lombok.Data;

/**
 * @Author: Xuyifeng
 * @Description:
 * @Date: 2026/5/16 19:57
 */
@Data
public class CreateSessionRequestDTO {

    private String agentId;
    private String userId;

}
