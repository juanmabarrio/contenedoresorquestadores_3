function sleep(millis){
    return new Promise(resolve => {
        setTimeout(resolve, millis);
    })
}

function updateTaskStatus(text) {
    document.getElementById("taskStatus").textContent = text;
}

var task;

async function createTask() {

    document.getElementById("createButton").disabled = true;

    updateTaskStatus("Creating...");

    let socket = new WebSocket("ws://" + window.location.host + "/taskProgress");

    socket.onmessage = async function (event) {

        var taskProgress = JSON.parse(event.data);

        if (!taskProgress.completed) {
            updateTaskStatus("Progress " + taskProgress.progress);
        } else {
            updateTaskStatus("Completed: " + taskProgress.result);

            await sleep(1000);

            await deleteTaskInServer();

            updateTaskStatus("Not started");

            document.getElementById("createButton").disabled = false;
        }
    };

    await createTaskInServer();

    updateTaskStatus("Created");

}

async function createTaskInServer() {
    const rawResponse = await fetch('/tasks/', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ text: 'Textual content' })
    });
    task = await rawResponse.json();
}

async function deleteTaskInServer() {
    await fetch('/tasks/' + task.id, {
        method: 'DELETE'
    });

    task = undefined;
}
