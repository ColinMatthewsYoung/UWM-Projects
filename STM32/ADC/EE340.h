/*
* EE340.h
*
* Created on: Jun 7, 2023
* Author: wfd
*/
#ifndef INC_EE340_H_
#define INC_EE340_H_
#define filterSize 16

struct queue{
	uint16_t items[filterSize];
	uint16_t sum;
	int front;
	int back;
};

int isEmpty(struct queue* q);
int isFull(struct queue* q);
int enqueue(struct queue* q,uint16_t item);
int dequeue(struct queue* q);
void processADC(ADC_HandleTypeDef* hadc1, UART_HandleTypeDef* hlpuart1);

#endif /* INC_EE340_H_ */
