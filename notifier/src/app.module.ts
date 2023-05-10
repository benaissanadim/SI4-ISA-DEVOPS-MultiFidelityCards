import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { MailerModule } from '@nestjs-modules/mailer';


@Module({
    imports: [
    MailerModule.forRoot({
      transport: {
        service: 'Gmail',
        auth: {
          user: 'nadim.ben.aissaa@gmail.com', 
          pass: 'mfwtovwxxyvihzte',
        },
      },
    }),
  ],
  controllers: [AppController],
})


export class AppModule {}
