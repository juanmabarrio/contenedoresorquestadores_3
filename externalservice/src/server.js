const grpc = require('grpc');
const UpperCaseService = require('./interface');
const helloServiceImpl = require('./upperCaseService');

const server = new grpc.Server();

server.addService(UpperCaseService.service, helloServiceImpl);
server.bind('0.0.0.0:9090', grpc.ServerCredentials.createInsecure());

console.log('gRPC server running at http://0.0.0.0:9090');

server.start();