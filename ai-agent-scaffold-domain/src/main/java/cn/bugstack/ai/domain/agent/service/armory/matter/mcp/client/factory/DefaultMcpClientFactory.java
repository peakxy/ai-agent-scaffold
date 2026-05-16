package cn.bugstack.ai.domain.agent.service.armory.matter.mcp.client.factory;

import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import cn.bugstack.ai.domain.agent.service.armory.matter.mcp.client.ToolMcpCreateService;
import cn.bugstack.ai.domain.agent.service.armory.matter.mcp.client.impl.LocalToolMcpCreateService;
import cn.bugstack.ai.domain.agent.service.armory.matter.mcp.client.impl.SseToolMcpCreateService;
import cn.bugstack.ai.domain.agent.service.armory.matter.mcp.client.impl.StdioToolMcpCreateService;
import cn.bugstack.ai.types.enums.ResponseCode;
import cn.bugstack.ai.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: Xuyifeng
 * @Description:
 * @Date: 2026/5/16 11:29
 */
@Slf4j
@Service
public class DefaultMcpClientFactory {

    @Resource
    private SseToolMcpCreateService sseToolMcpCreateService;
    @Resource
    private StdioToolMcpCreateService stdioToolMcpCreateService;
    @Resource
    private LocalToolMcpCreateService localToolMcpCreateService;

    public ToolMcpCreateService getToolMcpCreateService(AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp) {
        if (null != toolMcp.getLocal()) {
            return localToolMcpCreateService;
        }
        if (null != toolMcp.getSse()) {
            return sseToolMcpCreateService;
        }
        if (null != toolMcp.getStdio()) {
            return stdioToolMcpCreateService;
        }
        throw new AppException(ResponseCode.NOT_FOUNT_METHOD.getCode(), ResponseCode.NOT_FOUNT_METHOD.getInfo());
    }

}
