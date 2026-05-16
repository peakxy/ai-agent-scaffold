package cn.bugstack.ai.api;

import cn.bugstack.ai.api.dto.*;
import cn.bugstack.ai.api.response.Response;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;

/**
 * @Author: Xuyifeng
 * @Description: 智能体服务接口
 * @Date: 2026/5/16 19:54
 */

public interface IAgentService {

    Response<List<AiAgentConfigResponseDTO>> queryAiAgentConfigList();

    Response<CreateSessionResponseDTO> createSession(CreateSessionRequestDTO createSessionRequestDTO);

    Response<ChatResponseDTO> chat(ChatRequestDTO chatRequestDTO);

    ResponseBodyEmitter chatStream(ChatRequestDTO chatRequestDTO);

}
