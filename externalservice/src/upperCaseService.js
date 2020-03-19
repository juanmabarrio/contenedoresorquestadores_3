function toUpperCase(call, callback){

    console.log('External server request received: '+JSON.stringify(call.request));

    var { text } = call.request;

    callback(null, { result: text.toUpperCase() });
}

exports.toUpperCase = toUpperCase;