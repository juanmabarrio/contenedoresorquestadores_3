package es.codeurjc.mastercloudapps.p3.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TaskManager {

	private static final String NEW_TASKS_QUEUE = "newTasks";
	private static final String TASKS_PROGRESS_QUEUE = "tasksProgress";

	Logger logger = LoggerFactory.getLogger(TaskManager.class);

	static class NewTask {
		public int id;
		public String text;
	}

	static class TaskProgress {
		public int id;
		public boolean completed;
		public int progress;
		public String result;

		public TaskProgress(int id, boolean completed, int progress, String result) {
			super();
			this.id = id;
			this.completed = completed;
			this.progress = progress;
			this.result = result;
		}
	}

	ObjectMapper json = new ObjectMapper();

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	UpperCaseTask upperCaseTask;

	@Autowired
	TaskRepository taskRepository;

	@RabbitListener(queues = NEW_TASKS_QUEUE, ackMode = "AUTO")
	public void received(String message) throws Exception {

		logger.info("Processing new Task: " + message);

		NewTask newTask = json.readValue(message, NewTask.class);

		logger.info("Persist task to mysql: " + newTask);
		Task newEntityTask = new Task();
		newEntityTask.setId(newTask.id);
		newEntityTask.setText(newTask.text);
		taskRepository.save(newEntityTask);

		String result = upperCaseTask.toUpperCase(newTask.text);
		String taskProgressMsg = null;
		TaskProgress taskProgress = null;

		// Simulate long processing
		for (int i = 0; i < 10; i++) {

			int progress = i * 10;

			taskProgress = new TaskProgress(newTask.id, false, progress, null);
			taskProgressMsg = json.writeValueAsString(taskProgress);

			logger.info("Task updated: " + taskProgressMsg);

			rabbitTemplate.convertAndSend(TASKS_PROGRESS_QUEUE, taskProgressMsg);

			Thread.sleep(500);
		}

		taskProgress = new TaskProgress(newTask.id, true, 100, result);
		taskProgressMsg = json.writeValueAsString(taskProgress);
		rabbitTemplate.convertAndSend(TASKS_PROGRESS_QUEUE, taskProgressMsg);

		logger.info("Task completed: " + message);
	}

}
