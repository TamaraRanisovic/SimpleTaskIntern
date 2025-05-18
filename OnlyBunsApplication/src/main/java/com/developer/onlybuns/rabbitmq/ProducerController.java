package com.developer.onlybuns.rabbitmq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api")
public class ProducerController {

    @Autowired
    private Producer producer;

    @PostMapping(value="/{queue}", consumes = "text/plain")
    public ResponseEntity<String> sendMessage(@PathVariable("queue") String queue, @RequestBody String message) {
        producer.sendTo(queue, message);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value="/fanout/{exchange}", consumes = "text/plain")
    public ResponseEntity<String> sendFanoutMessage(@PathVariable("exchange") String exchange, @RequestBody String message) {
        producer.sendFanout(exchange, message);
        return ResponseEntity.ok().build();
    }


}