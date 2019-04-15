package testcases;


import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableTest;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@FlowableTest
public class StuckInclusiveGatewayTest {

    private Logger log = LoggerFactory.getLogger(StuckInclusiveGatewayTest.class);

    private ProcessEngine processEngine;
    private RuntimeService runtimeService;
    private TaskService taskService;

    @BeforeEach
    void setUp(ProcessEngine processEngine) {
        this.processEngine = processEngine;
        this.runtimeService = processEngine.getRuntimeService();
        this.taskService = processEngine.getTaskService();
    }

    @Test
    @Deployment
    void testStuckInclusiveGateway() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("option1", true);
        variables.put("option2", false);
        variables.put("option3", true);
        variables.put("retry", false);

        runtimeService.startProcessInstanceByKey("stuck-inclusive-gateway", variables);

        List<Task> tasks = taskService.createTaskQuery().list();
        log.info("tasks: {}", tasks);
        List<Execution> executions = runtimeService.createExecutionQuery().list();
        log.info("executions: {}", executions);

        taskService.complete(tasks.get(0).getId());

        tasks = taskService.createTaskQuery().list();
        log.info("tasks: {}", tasks);
        executions = runtimeService.createExecutionQuery().list();
        log.info("executions: {}", executions);

        taskService.complete(tasks.get(0).getId());

        tasks = taskService.createTaskQuery().list();
        log.info("tasks: {}", tasks);
        executions = runtimeService.createExecutionQuery().list();
        log.info("executions: {}", executions);

        taskService.complete(tasks.get(0).getId());

        tasks = taskService.createTaskQuery().list();
        log.info("tasks: {}", tasks);
        executions = runtimeService.createExecutionQuery().list();
        log.info("executions: {}", executions);

        taskService.complete(tasks.get(0).getId());

        variables.put("option3", false);

        tasks = taskService.createTaskQuery().list();
        log.info("tasks: {}", tasks);
        executions = runtimeService.createExecutionQuery().list();
        log.info("executions: {}", executions);

        taskService.complete(tasks.get(0).getId(), variables);

        tasks = taskService.createTaskQuery().list();
        log.info("tasks: {}", tasks);
        executions = runtimeService.createExecutionQuery().list();
        log.info("executions: {}", executions);

    }


}
