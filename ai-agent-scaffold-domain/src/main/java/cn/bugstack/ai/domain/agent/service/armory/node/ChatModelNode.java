package cn.bugstack.ai.domain.agent.service.armory.node;

import cn.bugstack.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import cn.bugstack.ai.domain.agent.service.armory.AbstractArmorySupport;
import cn.bugstack.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Xuyifeng
 * @Description:
 * @Date: 2026/5/15 16:14
 */
@Slf4j
@Service
public class ChatModelNode extends AbstractArmorySupport {

    @Resource
    private AgentNode agentNode;

    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 装配操作 - ChatModelNode");

        OpenAiApi openAiApi = dynamicContext.getOpenAiApi();

        AiAgentConfigTableVO aiAgentConfigTableVO = requestParameter.getAiAgentConfigTableVO();
        AiAgentConfigTableVO.Module.ChatModel chatModelConfig = aiAgentConfigTableVO.getModule().getChatModel();

        List<McpSyncClient> mcpSyncClients = new ArrayList<>();
        List<AiAgentConfigTableVO.Module.ChatModel.ToolMcp> toolMcpList = chatModelConfig.getToolMcpList();
        if (null != toolMcpList && !toolMcpList.isEmpty()) {
            for (AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp : toolMcpList) {
                mcpSyncClients.add(createMcpSyncClient(toolMcp));
            }
        }

        OpenAiChatOptions.Builder optionsBuilder = OpenAiChatOptions.builder()
                .model(chatModelConfig.getModel());

        if (!mcpSyncClients.isEmpty()) {
            optionsBuilder.toolCallbacks(SyncMcpToolCallbackProvider.builder()
                    .mcpClients(mcpSyncClients)
                    .build()
                    .getToolCallbacks());
        }

        ChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(optionsBuilder.build())
                .build();

        dynamicContext.setChatModel(chatModel);

        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        return agentNode;
    }

    // mcp 构建
    private McpSyncClient createMcpSyncClient(AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp) throws Exception {
        AiAgentConfigTableVO.Module.ChatModel.ToolMcp.SSEServerParameters sseConfig = toolMcp.getSse();
        AiAgentConfigTableVO.Module.ChatModel.ToolMcp.StdioServerParameters stdioConfig = toolMcp.getStdio();

        if (null != sseConfig) {
            // http://appbuilder.baidu.com/v2/ai_search/mcp/
            // sse?api_key=bce-v3/ALTAK-wSdZlznYmRrBYLRTFh5C3/f358dc37a271943e3e2822d4e722952593f818db
            // String fullUrl = "http://appbuilder.baidu.com/v2/ai_search/mcp/sse?api_key=bce-v3/ALTAK-wSdZlznYmRrBYLRTFh5C3/f358dc37a271943e3e2822d4e722952593f818db";

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

            return mcpSyncClient;
        }

        if (null != stdioConfig) {
            AiAgentConfigTableVO.Module.ChatModel.ToolMcp.StdioServerParameters.ServerParameters serverParameters = stdioConfig.getServerParameters();

            ServerParameters stdioParams = ServerParameters.builder(serverParameters.getCommand())
                    .args(serverParameters.getArgs())
                    .env(serverParameters.getEnv())
                    .build();

            McpSyncClient mcpSyncClient = McpClient.sync(new StdioClientTransport(stdioParams, new JacksonMcpJsonMapper(new ObjectMapper())))
                    .requestTimeout(Duration.ofSeconds(stdioConfig.getRequestTimeout())).build();

            McpSchema.InitializeResult initialize = mcpSyncClient.initialize();

            log.info("tool stdio mcp initialize {}", initialize);

            return mcpSyncClient;
        }

        throw new RuntimeException("tool mcp sse and stdio is null!");
    }

}
