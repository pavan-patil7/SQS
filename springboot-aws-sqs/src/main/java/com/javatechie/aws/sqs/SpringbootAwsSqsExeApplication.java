package com.javatechie.aws.sqs;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication(exclude = {ContextStackAutoConfiguration.class})
@RestController
public class SpringbootAwsSqsExeApplication {


    Logger logger= LoggerFactory.getLogger(SpringbootAwsSqsExeApplication.class);

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Value("${cloud.aws.end-point.uri}")
    private String endpoint;

    @GetMapping("/send/{message}")
    public void sendMessageToQueue(@PathVariable String message) {
//        queueMessagingTemplate.send(endpoint, MessageBuilder.withPayload(message).build());

        PublishRequest publishRequest = new PublishRequest().withTopicArn("arn:aws:sns:ap-south-1:182684745098:Test")
                .withMessageAttributes(Map.of(
                        "notificationType", new MessageAttributeValue().withDataType("String").withStringValue("email")

                ));
    }

    @SqsListener("emailQueue")
    public void loadMessageFromEmailSQS(String message)  {
        logger.info("message from email SQS Queue {}",message);
    }

    @SqsListener("SMSQueue")
    public void loadMessageFromSMSSQS(String message)  {
        logger.info("message from  sms SQS Queue {}",message);
    }



    public static void main(String[] args) {
        SpringApplication.run(SpringbootAwsSqsExeApplication.class, args);
    }

}
