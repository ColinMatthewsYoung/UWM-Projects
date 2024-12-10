/*
 * EE340.c
 *
 *  Created on: Oct 18, 2024
 *      Author: young
 */

#include "main.h"
#include "EE340.h"
#include <stdio.h>
#include <string.h>

char msg[10];
struct queue q = {{},0,-1,-1};

int isEmpty(struct queue* q){
	if(q->front == -1)
		return 1;
	return 0;
}

int isFull(struct queue* q){
	if((q->back+1) % filterSize == q->front)
		return 1;
	return 0;
}

int enqueue(struct queue* q,uint16_t item){
	if(isFull(q))
		return -1;
	else{
		if(isEmpty(q)){
			q->front = 0;
		}
		q->back = (q->back + 1) % filterSize;
		q->items[q->back] = item;

		q->sum+= item;
	}
	return 0;
}

int dequeue(struct queue* q){
	if(isEmpty(q))
		return -1;
	else{
		uint16_t item = q->items[q->front];
		q->sum -= q->items[q->front];
		if(q->front == q->back){
			q->front = -1;
			q->back = -1;
		}
		else{
			q->front = (q->front + 1) % filterSize;
		}

		return item;
	}
}

uint16_t processADC(ADC_HandleTypeDef* hadc1, UART_HandleTypeDef* hlpuart1){
	HAL_ADC_Start(hadc1);
	HAL_ADC_PollForConversion(hadc1, HAL_MAX_DELAY);
	if(isFull(&q)){
		//sprintf(msg, "%hu\r\n", q.sum >> 4); //lab says to use bitshift, but we could divide by filterSize to make it easy to adjust.
		//HAL_UART_Transmit(hlpuart1, (uint8_t*)msg,strlen(msg),HAL_MAX_DELAY); //transmit average sum.
		uint16_t average = q.sum/filterSize;
		dequeue(&q);
		enqueue(&q,HAL_ADC_GetValue(hadc1));
		return average;
	}
	//q is not full so we are just returning -1.
	else{
		enqueue(&q,HAL_ADC_GetValue(hadc1));
		return -1;
	}

}

