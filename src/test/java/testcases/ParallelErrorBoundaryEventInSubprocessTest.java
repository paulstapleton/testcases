package testcases;

import java.util.List;

import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
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
public class ParallelErrorBoundaryEventInSubprocessTest {

    private Logger log = LoggerFactory.getLogger(ParallelErrorBoundaryEventInSubprocessTest.class);

    private ProcessEngine processEngine;
    private HistoryService historyService;
    private RuntimeService runtimeService;
    private TaskService taskService;

    @BeforeEach
    void setUp(ProcessEngine processEngine) {
        this.processEngine = processEngine;
        this.historyService = processEngine.getHistoryService();
        this.runtimeService = processEngine.getRuntimeService();
        this.taskService = processEngine.getTaskService();
    }

    @Test
    @Deployment
    void testSameErrorThrownFromBothScriptTasks() {

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("parallel-error-boundary-event");

    }


}
