package testcases;

import java.util.List;

import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessMigrationService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.migration.ActivityMigrationMapping;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableTest;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FlowableTest
public class MigrationOfSubProcessAndParallelGatewayTest {

    private Logger log = LoggerFactory.getLogger(MigrationOfSubProcessAndParallelGatewayTest.class);

    private ProcessEngine processEngine;
    private ProcessMigrationService processMigrationService;
    private RepositoryService repositoryService;
    private RuntimeService runtimeService;
    private TaskService taskService;

    @BeforeEach
    void setUp(ProcessEngine processEngine) {
        this.processEngine = processEngine;
        this.processMigrationService = processEngine.getProcessMigrationService();
        this.runtimeService = processEngine.getRuntimeService();
        this.repositoryService = processEngine.getRepositoryService();
        this.taskService = processEngine.getTaskService();
    }

    @Test
    void testUserTaskAreMigrated() {

        repositoryService.createDeployment()
            .addClasspathResource("testcases/MigrationOfSubProcessAndParallelGatewayTest.version1.bpmn20.xml")
            .deploy();

        repositoryService.createDeployment()
            .addClasspathResource("testcases/MigrationOfSubProcessAndParallelGatewayTest.version2.bpmn20.xml")
            .deploy();

        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
            .orderByProcessDefinitionVersion().asc().list();

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitions.get(0).getId());

        List<Task> beforeTasks = taskService.createTaskQuery().list();
        log.info("Before tasks: {}", beforeTasks);
        List<Execution> beforeExecutions = runtimeService.createExecutionQuery().list();
        log.info("Before executions: {}", beforeExecutions);

        processMigrationService.createProcessInstanceMigrationBuilder()
            .migrateToProcessDefinition(processDefinitions.get(1).getId())
            .addActivityMigrationMapping(ActivityMigrationMapping.createMappingFor("task2", "task3"))
            .migrate(processInstance.getId());

        List<Task> afterTasks = taskService.createTaskQuery().list();
        log.info("After tasks: {}", afterTasks);
        List<Execution> afterExecutions = runtimeService.createExecutionQuery().list();
        log.info("After executions: {}", afterExecutions);

        Assertions.assertEquals(1, afterTasks.stream().filter(task-> task.getTaskDefinitionKey().equals("task1")).count());
        Assertions.assertEquals(1, afterTasks.stream().filter(task-> task.getTaskDefinitionKey().equals("task3")).count());
    }


}
