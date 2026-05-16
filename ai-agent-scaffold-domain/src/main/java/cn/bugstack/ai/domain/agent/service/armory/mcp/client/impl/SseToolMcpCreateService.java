package cn.bugstack.ai.domain.agent.service.armory.mcp.client.impl;

import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import cn.bugstack.ai.domain.agent.service.armory.mcp.client.ToolMcpCreateService;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * @Author: Xuyifeng
 * @Description:
 * @Date: 2026/5/16 11:14
 */
@Slf4j
@Service
public class SseToolMcpCreateService implements ToolMcpCreateService {

    @Override
    public ToolCallback[] buildToolCallback(AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp) throws Exception {
        AiAgentConfigTableVO.Module.ChatModel.ToolMcp.SSEServerParameters sseConfig = toolMcp.getSse();
        String originBaseUri = sseConfig.getBaseUri();
        String baseUri = originBaseUri;
        String sseEndpoint = sseConfig.getSseEndpoint();

        if (StringUtils.isBlank(sseEndpoint)) {
            URL url = new URL(originBaseUri);

            String protocol = url.getProtocol();
            String host = url.getHost();
            int port = url.getPort();

            String baseUrl = port == -1 ? protocol + "://" + host : protocol + "://" + host + ":" + port;

            int index = originBaseUri.indexOf(baseUrl);
            if (index != -1) {
                sseEndpoint = originBaseUri.substring(index + baseUrl.length());
            }

            baseUri = baseUrl;
        }

        sseEndpoint = StringUtils.isBlank(sseEndpoint) ? "/sse" : sseEndpoint;

        HttpClientSseClientTransport sseClientTransport = HttpClientSseClientTransport
                .builder(baseUri)
                .sseEndpoint(sseEndpoint)
                .build();

        McpSyncClient mcpSyncClient = McpClient.sync(sseClientTransport).requestTimeout(Duration.ofMillis(sseConfig.getRequestTimeout())).build();
        McpSchema.InitializeResult initialize = mcpSyncClient.initialize();

        log.info("tool sse mcp initialize {}", initialize);

        return SyncMcpToolCallbackProvider.builder()
                .mcpClients(mcpSyncClient)
                .build()
                .getToolCallbacks();
    }

}
