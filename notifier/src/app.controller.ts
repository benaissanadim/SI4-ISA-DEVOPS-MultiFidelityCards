import {Body, Controller, Get, HttpException, HttpStatus, Post} from '@nestjs/common';

import { MailerService } from '@nestjs-modules/mailer';


@Controller('notifications')
export class AppController {
  constructor(private readonly mailerService: MailerService) {}

  @Post()
  async sendEmail(@Body() emailData: { to: string; subject: string; text: string }) {
    await this.mailerService.sendMail({
      to: emailData.to,
      subject: emailData.subject,
      text: emailData.text,
    });
    return 'Email sent successfully';
  }
}
