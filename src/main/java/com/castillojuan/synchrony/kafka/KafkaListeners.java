package com.castillojuan.synchrony.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.castillojuan.synchrony.entity.KafkaImageData;

@Component
public class KafkaListeners {

	@KafkaListener(topics = "synchrony_topic", groupId = "synchrony")
	public void listen(KafkaImageData data) {
		
	}
}
