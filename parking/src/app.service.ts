import { Injectable, Logger } from '@nestjs/common';
import { ParkingDto } from './dto/parking.dto';
import * as QRCode  from 'qrcode';
import * as fs from 'fs';

@Injectable()
export class AppService {

  private static readonly magicKey : string = '896983'; // ASCII code for 'YES'

  private logger = new Logger('AppService');

  private transactions : Array<ParkingDto>;

  constructor() {
    this.transactions = [];
  }

  async sendEmailConfirmation(ParkingDto: ParkingDto): Promise<any>{
    const url = ParkingDto.to + ParkingDto.minutes + ParkingDto.name;

    const qrCodeDataURL = await QRCode.toDataURL(url);

    const imageData = qrCodeDataURL.split(',')[1];
    const imageBuffer = Buffer.from(imageData, 'base64');

    fs.writeFile('qr-code.png', imageBuffer, (err) => {
      if (err) throw err;
      console.log('QR code image saved.');
    });


    const email = {
      to:   ParkingDto.to,
      subject: "Parking Ticket confirmation",
      text: "Dear Client,"+
       "\n\nYour reservation for parking "+ ParkingDto.name+" is confirmed for " + ParkingDto.minutes + " minutes"
      + "\n A reminder will be send before finishing the duration."
      +"\n Thanks for your confidence,"
      +" \n\n LsWhereYouParkedLastSummer",
      attachments: [
        {
          filename: 'qr-code.png',
          content: imageBuffer,
        },
      ],
    };
    return email;
  }

  sendEmailReminder(ParkingDto: ParkingDto): any{
    return   {
      to:   ParkingDto.to,
      subject: "Parking Duration Reminder",
      text: "Dear Client,"+
      "\n\n Quick Remider, onlu 5 minutes left for your reservation in parking "+ ParkingDto.name
      +"\n Thanks for your confidence,"
      +" \n\n LsWhereYouParkedLastSummer"
    };
  }

  sendEmailFinish(ParkingDto : ParkingDto) : any{
    return   {
      to:   ParkingDto.to,
      subject: "Parking Reservation finished",
      text: "Dear Client,"+
      "\n\n Thanks for your confidence in reserving in our parking "+ ParkingDto.name
      +"\n See you soon,"
      +" \n\n LsWhereYouParkedLastSummer"
    };
  }




}
