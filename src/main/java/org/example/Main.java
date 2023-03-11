package org.example;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String queueUrl = "https://sqs.us-east-1.amazonaws.com/784454363308/MyQueue";
        String queueUrl1 = "https://sqs.us-east-1.amazonaws.com/784454363308/MyQueue1";
        String region = Regions.US_EAST_1.getName();

        AmazonSQS queueSqs = AmazonSQSClientBuilder.standard()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(queueUrl, region))
                .build();

        //Send one message
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody("Teste de mensagem");

        queueSqs.sendMessage(send_msg_request);

        List<SendMessageBatchRequestEntry> list = new ArrayList<>();

        SendMessageBatchRequestEntry sendMessageBatchRequestEntry0 = new SendMessageBatchRequestEntry()
                .withMessageBody("Mensagem em lote 0")
                .withDelaySeconds(10);

        SendMessageBatchRequestEntry sendMessageBatchRequestEntry1 = new SendMessageBatchRequestEntry()
                .withMessageBody("Mensagem em lote 1")
                .withDelaySeconds(5);

        list.add(sendMessageBatchRequestEntry0);
        list.add(sendMessageBatchRequestEntry1);

        //Send Batch message - method 1

        queueSqs.sendMessageBatch(queueUrl, list);

        //Send Batch message - method 2

        SendMessageBatchRequest sendMessageBatchRequest = new SendMessageBatchRequest()
                .withQueueUrl(queueUrl1)
                .withEntries(list);

        queueSqs.sendMessageBatch(sendMessageBatchRequest);


        try {
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMaxNumberOfMessages(2);

            List<Message> messages = queueSqs.receiveMessage(receiveMessageRequest).getMessages();
            System.out.println(messages);
        }catch (AmazonSQSException e){

        }

    }
}