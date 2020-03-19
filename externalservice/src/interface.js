const grpc = require('grpc');
const protoLoader = require('@grpc/proto-loader');

var packageDefinition = protoLoader.loadSync(__dirname + '/../UpperCaseTaskService.proto',
    {
        keepCase: true,
        longs: String,
        enums: String,
        defaults: true,
        oneofs: true
    });

var upperCaseTaskServiceProto = grpc.loadPackageDefinition(packageDefinition);

module.exports = upperCaseTaskServiceProto.es.codeurjc.mastercloudapps.p3.uppertask.UpperCaseService;