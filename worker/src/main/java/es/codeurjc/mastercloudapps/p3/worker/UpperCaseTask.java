package es.codeurjc.mastercloudapps.p3.worker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import es.codeurjc.mastercloudapps.p3.uppertask.UpperCaseServiceGrpc.UpperCaseServiceBlockingStub;
import es.codeurjc.mastercloudapps.p3.uppertask.UpperCaseTaskRequest;
import es.codeurjc.mastercloudapps.p3.uppertask.UpperCaseTaskResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class UpperCaseTask {

	@GrpcClient("upperCaseServer")
	private UpperCaseServiceBlockingStub client;
	
	public String toUpperCase(String text) throws Exception {
		
		UpperCaseTaskRequest request = UpperCaseTaskRequest.newBuilder()
	            .setText(text)
	            .build();
	        
		UpperCaseTaskResponse response = client.toUpperCase(request);

		return response.getResult();		
	}	
}
