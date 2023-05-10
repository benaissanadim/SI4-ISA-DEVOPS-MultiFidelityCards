import {Body, Controller, Get, HttpException, HttpStatus, Post} from '@nestjs/common';

import { AppService } from './app.service';

import { MailerService } from '@nestjs-modules/mailer';

import * as QRCode  from 'qrcode';
import * as fs from 'fs';
import { ParkingDto } from './dto/parking.dto';




@Controller('parking')
export class AppController {
  constructor(private readonly appService: AppService,
    private readonly mailerService: MailerService) {}

  @Post('')
  async sendEmailWithQRCode(@Body() ParkingDto: ParkingDto) {
    const minutes = ParkingDto.minutes;
    const email =  await this.appService.sendEmailConfirmation(ParkingDto);
    try {
      await this.mailerService.sendMail(email);
      console.log('Email sent.');
      if(minutes>5){
        setTimeout(() => {
          console.log('5 minutes left sending email reminder');
          const email2 = this.appService.sendEmailReminder(ParkingDto);
          this.mailerService.sendMail(email2);
                },(minutes-5)  * 60 * 1000);
      }
      
      setTimeout(() => {
        console.log('duration reservation finished');
        const email3 = this.appService.sendEmailFinish(ParkingDto);
        this.mailerService.sendMail(email3);
        
      }, minutes*60 * 1000);

      return "Parking ticket generated with succeess, please check your email"
    } catch (error) {
      console.log(error);
    }
  }
}