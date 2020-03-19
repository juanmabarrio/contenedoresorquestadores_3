var express = require('express');
var app = express();
var expressWs = require('express-ws')(app);
var amqp = require('amqplib/callback_api');
var mongoose = require("mongoose");
require('dotenv').config();

app.use(express.static('public'));
app.use(express.json());

// mongo config
mongoose.Promise = global.Promise;
mongoose.connect(process.env.MONGO_URL+process.env.MONGO_DB_NAME, {
    "auth": { "authSource": "admin" },
    useNewUrlParser: true,
    "user": process.env.MONGO_DB_USER,
    "pass": process.env.MONGO_DB_PASSWORD
});
var taskSchema = new mongoose.Schema({
    id: Number,
    text: String,
    progress: String,
    completed: Boolean
});
var Task = mongoose.model("Task", taskSchema);

var task;
var userWs;
var amqpChannel;

amqp.connect(process.env.RABBIT_MQ_CONN_STRING, async function (err, conn) {

    if(err){
        return console.log('Error connecting to RabbitMQ: '+err);
    }

    amqpChannel = await conn.createChannel();
    
    console.log("Connected to RabbitMQ");

    // queues creation
    await amqpChannel.assertQueue('tasksProgress');
    await amqpChannel.assertQueue('newTasks');

    amqpChannel.consume('tasksProgress', function(msg){
        
        var message = JSON.parse(msg.content.toString());

        console.log("Message received: "+ JSON.stringify(message));
        
        sendWs(message);

    }, { noAck: true });

});

function sendWs(message) {
    if (!!userWs) {
        userWs.send(JSON.stringify(message));
    }
}

async function processTask() {

    if(!amqpChannel){
        console.error("Not connected to RabbitMQ");
    } else {

        var newTask = {
            id: task.id,
            text: task.text
        }

        amqpChannel.sendToQueue("newTasks", Buffer.from(JSON.stringify(newTask)));
    }
}

app.route('/tasks/:id')
    .get(function (req, res, next) {

        if (!!task && req.params.id == task.id) {
            res.send(task);
        } else {
            res.status(404).end();
        }

    }).delete(function (req, res) {

        if (!!task && req.params.id == task.id) {
            res.send(task);
            task = undefined;
        } else {
            res.status(404).end();
        }
    });

app.post('/tasks/', function (req, res) {

    if (!!task) {
        res.status(409).send('Tasks already created');
    } else {

        task = {
            id: 1,
            text: req.body.text,
            progress: 0,
            completed: false
        }
        console.log({task});
        console.log('Persist task to mongo');
        var newTask = new Task(task);
        newTask.save();

        res.status(201).send(task);

        processTask();
    }

})

app.ws('/taskProgress', async function (ws, req) {
    console.log('User connected');
    userWs = ws;
});

app.listen(8080);