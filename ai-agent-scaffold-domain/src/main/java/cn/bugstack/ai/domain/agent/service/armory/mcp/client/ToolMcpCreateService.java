package cn.bugstack.ai.domain.agent.service.armory.mcp.client;

import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.springframework.ai.tool.ToolCallback;

import java.net.MalformedURLException;

/**
 * @Author: Xuyifeng
 * @Description: 工具 mcp 构建服务
 * @Date: 2026/5/16 11:13
 */

public interface ToolMcpCreateService {

    ToolCallback[] buildToolCallback(AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp) throws Exception;

}
