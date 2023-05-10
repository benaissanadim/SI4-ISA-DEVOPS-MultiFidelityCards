import { IsNotEmpty, IsPositive, IsString } from 'class-validator';

export class ParkingDto {

    @IsNotEmpty()
    @IsString()
    to: string;

    @IsNotEmpty()
    @IsPositive()
    minutes: number;

    @IsNotEmpty()
    @IsString()
    name: string;

}