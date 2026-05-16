package cn.bugstack.ai.domain.agent.service.armory.node.workflow;

import cn.bugstack.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import cn.bugstack.ai.domain.agent.model.valobj.enums.AgentTypeEnum;
import cn.bugstack.ai.domain.agent.service.armory.AbstractArmorySupport;
import cn.bugstack.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LoopAgent;
import com.google.adk.agents.ParallelAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Xuyifeng
 * @Description:
 * @Date: 2026/5/15 18:00
 */
@Slf4j
@Service("parallelAgentNode")
public class ParallelAgentNode extends AbstractArmorySupport {

    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 装配操作 - ParallelAgentNode");

        //List<AiAgentConfigTableVO.Module.AgentWorkflow> agentWorkflows = dynamicContext.getAgentWorkflows();
        //AiAgentConfigTableVO.Module.AgentWorkflow agentWorkflow = agentWorkflows.remove(0);
        AiAgentConfigTableVO.Module.AgentWorkflow currentAgentWorkflow = dynamicContext.getCurrentAgentWorkflow();

        List<String> subAgentNames = currentAgentWorkflow.getSubAgents();
        List<BaseAgent> subAgents = dynamicContext.queryAgentList(subAgentNames);

        ParallelAgent parallelAgent = ParallelAgent.builder()
                .name(currentAgentWorkflow.getName())
                .description(currentAgentWorkflow.getDescription())
                .subAgents(subAgents)
                .build();

        dynamicContext.getAgentGroup().put(currentAgentWorkflow.getName(), parallelAgent);

        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        return getBean("agentWorkflowNode");
        /**
         List<AiAgentConfigTableVO.Module.AgentWorkflow> agentWorkflows = dynamicContext.getAgentWorkflows();
         if (null == agentWorkflows || agentWorkflows.isEmpty()) {
         return defaultStrategyHandler;
         }

         AiAgentConfigTableVO.Module.AgentWorkflow agentWorkflow = agentWorkflows.get(0);

         String type = agentWorkflow.getType();
         AgentTypeEnum agentTypeEnum = AgentTypeEnum.fromType(type);

         if (null == agentTypeEnum) {
         throw new RuntimeException("agentWorkflow type is null");
         }

         String node = agentTypeEnum.getNode();

         return switch (node) {
         case "loopAgentNode" -> getBean("loopAgentNode");
         case "sequentialAgentNode" -> getBean("sequentialAgentNode");
         default -> defaultStrategyHandler;
         };
         */
    }
}
