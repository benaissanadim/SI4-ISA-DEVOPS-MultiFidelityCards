import { Injectable, Logger } from '@nestjs/common';

import {PaymentDto} from './dto/payment.dto';
import {PaymentRejectedException} from './exceptions/payment-rejected-exception';

@Injectable()
export class AppService {

  private static readonly magicKey : string = '896983'; // ASCII code for 'YES'

  private logger = new Logger('AppService');

  private transactions : Array<PaymentDto>;

  constructor() {
    this.transactions = [];
  }

  findAll(): PaymentDto[] {
    return this.transactions;
  }

  pay(paymentDto: PaymentDto): PaymentDto {
    if (paymentDto.creditCard.includes(AppService.magicKey)) {
        this.transactions.push(paymentDto);
        this.logger.log(`Payment accepted: ${JSON.stringify(paymentDto)}`);
        return paymentDto;
      } else {
        this.logger.log(`Payment rejected: ${JSON.stringify(paymentDto)}`);
        throw new PaymentRejectedException(paymentDto.amount);
      }
    }

}
